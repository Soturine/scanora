package com.soturine.scanora.core.data.ocr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.soturine.scanora.core.common.repository.OcrRepository
import java.io.File
import kotlin.math.max
import kotlin.math.roundToInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultOcrRepository(
    private val context: Context,
) : OcrRepository {
    override suspend fun recognizeText(imageUri: String): String = withContext(Dispatchers.IO) {
        val bitmap = loadBitmap(imageUri, maxDimension = 2200) ?: return@withContext ""
        val prepared = prepareForOcr(bitmap)
        val inputImage = InputImage.fromBitmap(prepared, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val result = recognizer.process(inputImage).awaitResult()
        recognizer.close()
        formatRecognizedText(result)
    }

    private fun prepareForOcr(bitmap: Bitmap): Bitmap {
        val grayscale = toGrayscale(bitmap)
        val background = createBackgroundLuma(grayscale)
        val width = grayscale.width
        val height = grayscale.height
        val source = IntArray(width * height)
        grayscale.getPixels(source, 0, width, 0, 0, width, height)
        val balanced = IntArray(source.size)

        for (index in source.indices) {
            val sourceGray = Color.red(source[index])
            val shift = ((216 - background[index]) * 0.9f).roundToInt()
            val corrected = (sourceGray + shift).coerceIn(0, 255)
            balanced[index] = Color.rgb(corrected, corrected, corrected)
        }

        val luma = IntArray(balanced.size) { index -> Color.red(balanced[index]) }
        val lower = percentile(luma, 0.05f)
        val upper = percentile(luma, 0.99f).coerceAtLeast(lower + 24)
        val output = IntArray(balanced.size)

        for (index in balanced.indices) {
            val value = Color.red(balanced[index])
            val stretched = (((value - lower) * 255f) / (upper - lower)).roundToInt().coerceIn(0, 255)
            output[index] = Color.rgb(stretched, stretched, stretched)
        }

        return Bitmap.createBitmap(output, width, height, Bitmap.Config.ARGB_8888)
    }

    private fun formatRecognizedText(result: Text): String {
        val formattedBlocks = result.textBlocks
            .mapNotNull { block ->
                block.lines
                    .map { it.text.trim() }
                    .filter { it.isNotBlank() }
                    .joinToString("\n")
                    .trim()
                    .takeIf { it.isNotBlank() }
            }
        return formattedBlocks.joinToString("\n\n").ifBlank { result.text.trim() }
    }

    private fun toGrayscale(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val source = IntArray(width * height)
        bitmap.getPixels(source, 0, width, 0, 0, width, height)
        val output = IntArray(source.size)
        for (index in source.indices) {
            val color = source[index]
            val gray = (0.299f * Color.red(color) + 0.587f * Color.green(color) + 0.114f * Color.blue(color))
                .roundToInt()
                .coerceIn(0, 255)
            output[index] = Color.rgb(gray, gray, gray)
        }
        return Bitmap.createBitmap(output, width, height, Bitmap.Config.ARGB_8888)
    }

    private fun createBackgroundLuma(bitmap: Bitmap): IntArray {
        val small = Bitmap.createScaledBitmap(
            bitmap,
            max(bitmap.width / 14, 1),
            max(bitmap.height / 14, 1),
            true,
        )
        val blurred = Bitmap.createScaledBitmap(small, bitmap.width, bitmap.height, true)
        val pixels = IntArray(blurred.width * blurred.height)
        blurred.getPixels(pixels, 0, blurred.width, 0, 0, blurred.width, blurred.height)
        return IntArray(pixels.size) { index -> Color.red(pixels[index]) }
    }

    private fun percentile(
        values: IntArray,
        fraction: Float,
    ): Int {
        val histogram = IntArray(256)
        values.forEach { histogram[it.coerceIn(0, 255)]++ }
        val target = (values.size * fraction.coerceIn(0f, 1f)).roundToInt()
        var seen = 0
        histogram.forEachIndexed { index, count ->
            seen += count
            if (seen >= target) return index
        }
        return 255
    }

    private fun loadBitmap(
        imageUri: String,
        maxDimension: Int,
    ): Bitmap? {
        val uri = Uri.parse(imageUri)
        val decodeBounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, decodeBounds)
        }
        val largestSide = max(decodeBounds.outWidth, decodeBounds.outHeight).coerceAtLeast(1)
        val sampleSize = max(1, largestSide / maxDimension)
        val options = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        return openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, options)
        }
    }

    private fun openInputStream(uri: Uri) =
        if (uri.scheme.isNullOrBlank()) {
            File(uri.toString()).inputStream()
        } else {
            context.contentResolver.openInputStream(uri)
        }
}
