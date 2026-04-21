package com.seunome.scanora.core.data.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import com.seunome.scanora.core.common.model.DocumentFilterType
import com.seunome.scanora.core.common.model.DocumentQuad
import com.seunome.scanora.core.common.model.PointValue
import com.seunome.scanora.core.common.repository.DocumentProcessingRepository
import java.io.File
import java.io.FileOutputStream
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultDocumentProcessingRepository(
    private val context: Context,
) : DocumentProcessingRepository {
    override suspend fun estimateDocumentQuad(imageUri: String): DocumentQuad = withContext(Dispatchers.IO) {
        val bitmap = loadBitmap(imageUri, maxDimension = 1600) ?: fallbackQuad(1600, 2200)
        val width = bitmap.width
        val height = bitmap.height
        val grayscale = toLumaArray(bitmap)
        val edgeMagnitude = sobelEdges(grayscale, width, height)
        val averageEdge = edgeMagnitude.average().toFloat()
        val threshold = max(28f, averageEdge * 1.35f)
        val bounds = detectBounds(edgeMagnitude, width, height, threshold)
        bounds?.let { rect ->
            DocumentQuad(
                topLeft = PointValue(rect.left / width.toFloat(), rect.top / height.toFloat()),
                topRight = PointValue(rect.right / width.toFloat(), rect.top / height.toFloat()),
                bottomRight = PointValue(rect.right / width.toFloat(), rect.bottom / height.toFloat()),
                bottomLeft = PointValue(rect.left / width.toFloat(), rect.bottom / height.toFloat()),
            )
        } ?: fallbackQuad(width, height)
    }

    override suspend fun processPage(
        sourceUri: String,
        filterType: DocumentFilterType,
        quad: DocumentQuad?,
        rotationDegrees: Int,
    ): String = withContext(Dispatchers.IO) {
        var bitmap = loadBitmap(sourceUri, maxDimension = 2200) ?: error("Não foi possível abrir a imagem.")
        if (rotationDegrees % 360 != 0) {
            bitmap = rotateBitmap(bitmap, rotationDegrees.toFloat())
        }

        val effectiveQuad = scaleQuadToBitmap(
            quad = quad ?: estimateDocumentQuad(sourceUri),
            width = bitmap.width,
            height = bitmap.height,
        )
        var processed = warpPerspective(bitmap, effectiveQuad)
        processed = removeBlackBorders(processed)
        processed = when (filterType) {
            DocumentFilterType.ORIGINAL_CORRECTED -> enhanceOriginal(processed)
            DocumentFilterType.DOCUMENT_BLACK_WHITE -> documentBlackWhite(processed)
            DocumentFilterType.DOCUMENT_GRAY -> documentGray(processed)
            DocumentFilterType.COLOR_ENHANCED -> colorEnhanced(processed)
            DocumentFilterType.RECEIPT_HIGH_CONTRAST -> receiptHighContrast(processed)
        }

        saveBitmap(processed, prefix = filterType.storageKey)
    }

    private fun enhanceOriginal(bitmap: Bitmap): Bitmap {
        val normalized = normalizeLighting(bitmap)
        return sharpen(normalized)
    }

    private fun documentBlackWhite(bitmap: Bitmap): Bitmap {
        val grayscale = normalizeLighting(toGrayscale(bitmap))
        val luma = toLumaArray(grayscale)
        val threshold = otsuThreshold(luma)
        val pixels = IntArray(luma.size) { index ->
            if (luma[index] >= threshold) Color.WHITE else Color.BLACK
        }
        return Bitmap.createBitmap(pixels, grayscale.width, grayscale.height, Bitmap.Config.ARGB_8888)
    }

    private fun documentGray(bitmap: Bitmap): Bitmap =
        sharpen(normalizeLighting(toGrayscale(bitmap)))

    private fun colorEnhanced(bitmap: Bitmap): Bitmap {
        val normalized = normalizeLighting(bitmap)
        val matrix = ColorMatrix(
            floatArrayOf(
                1.08f, 0f, 0f, 0f, 8f,
                0f, 1.08f, 0f, 0f, 8f,
                0f, 0f, 1.08f, 0f, 8f,
                0f, 0f, 0f, 1f, 0f,
            ),
        )
        return applyColorMatrix(normalized, matrix)
    }

    private fun receiptHighContrast(bitmap: Bitmap): Bitmap {
        val grayscale = normalizeLighting(toGrayscale(bitmap))
        val luma = toLumaArray(grayscale)
        val threshold = max(otsuThreshold(luma) - 10, 90)
        val pixels = IntArray(luma.size) { index ->
            val value = if (luma[index] >= threshold) 255 else 0
            Color.rgb(value, value, value)
        }
        return sharpen(Bitmap.createBitmap(pixels, grayscale.width, grayscale.height, Bitmap.Config.ARGB_8888))
    }

    private fun normalizeLighting(bitmap: Bitmap): Bitmap {
        val grayscale = toGrayscale(bitmap)
        val small = Bitmap.createScaledBitmap(
            grayscale,
            max(grayscale.width / 12, 1),
            max(grayscale.height / 12, 1),
            true,
        )
        val blurred = Bitmap.createScaledBitmap(small, grayscale.width, grayscale.height, true)

        val source = IntArray(bitmap.width * bitmap.height)
        val background = IntArray(blurred.width * blurred.height)
        bitmap.getPixels(source, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        blurred.getPixels(background, 0, blurred.width, 0, 0, blurred.width, blurred.height)

        val normalized = IntArray(source.size)
        for (index in source.indices) {
            val backgroundGray = max(Color.red(background[index]), 1)
            val sourceColor = source[index]
            val correctedRed = ((Color.red(sourceColor) * 255f) / backgroundGray).roundToInt().coerceIn(0, 255)
            val correctedGreen = ((Color.green(sourceColor) * 255f) / backgroundGray).roundToInt().coerceIn(0, 255)
            val correctedBlue = ((Color.blue(sourceColor) * 255f) / backgroundGray).roundToInt().coerceIn(0, 255)
            normalized[index] = Color.rgb(correctedRed, correctedGreen, correctedBlue)
        }
        return Bitmap.createBitmap(normalized, bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
    }

    private fun toGrayscale(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val matrix = ColorMatrix().apply { setSaturation(0f) }
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            colorFilter = ColorMatrixColorFilter(matrix)
        }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return output
    }

    private fun applyColorMatrix(bitmap: Bitmap, colorMatrix: ColorMatrix): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            colorFilter = ColorMatrixColorFilter(colorMatrix)
        }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return output
    }

    private fun sharpen(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val source = IntArray(width * height)
        bitmap.getPixels(source, 0, width, 0, 0, width, height)
        val output = IntArray(source.size)
        val kernel = intArrayOf(
            0, -1, 0,
            -1, 5, -1,
            0, -1, 0,
        )
        for (y in 1 until height - 1) {
            for (x in 1 until width - 1) {
                var red = 0
                var green = 0
                var blue = 0
                var kernelIndex = 0
                for (ky in -1..1) {
                    for (kx in -1..1) {
                        val color = source[(y + ky) * width + (x + kx)]
                        val weight = kernel[kernelIndex++]
                        red += Color.red(color) * weight
                        green += Color.green(color) * weight
                        blue += Color.blue(color) * weight
                    }
                }
                output[y * width + x] = Color.rgb(
                    red.coerceIn(0, 255),
                    green.coerceIn(0, 255),
                    blue.coerceIn(0, 255),
                )
            }
        }
        return Bitmap.createBitmap(output, width, height, Bitmap.Config.ARGB_8888)
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun warpPerspective(bitmap: Bitmap, quad: DocumentQuad): Bitmap {
        val targetWidth = max(
            distance(quad.topLeft, quad.topRight),
            distance(quad.bottomLeft, quad.bottomRight),
        ).roundToInt().coerceAtLeast(1)
        val targetHeight = max(
            distance(quad.topLeft, quad.bottomLeft),
            distance(quad.topRight, quad.bottomRight),
        ).roundToInt().coerceAtLeast(1)

        val source = floatArrayOf(
            quad.topLeft.x, quad.topLeft.y,
            quad.topRight.x, quad.topRight.y,
            quad.bottomRight.x, quad.bottomRight.y,
            quad.bottomLeft.x, quad.bottomLeft.y,
        )
        val destination = floatArrayOf(
            0f, 0f,
            targetWidth.toFloat(), 0f,
            targetWidth.toFloat(), targetHeight.toFloat(),
            0f, targetHeight.toFloat(),
        )

        val matrix = Matrix().apply {
            setPolyToPoly(source, 0, destination, 0, 4)
        }
        val output = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        canvas.drawColor(Color.WHITE)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        canvas.drawBitmap(bitmap, matrix, paint)
        return output
    }

    private fun detectBounds(
        edgeMagnitude: FloatArray,
        width: Int,
        height: Int,
        threshold: Float,
    ): Rect? {
        var left = width
        var right = 0
        var top = height
        var bottom = 0
        for (y in 0 until height) {
            for (x in 0 until width) {
                val edge = edgeMagnitude[y * width + x]
                if (edge < threshold) continue
                left = min(left, x)
                right = max(right, x)
                top = min(top, y)
                bottom = max(bottom, y)
            }
        }
        if (left >= right || top >= bottom) return null

        val minWidth = (width * 0.35f).roundToInt()
        val minHeight = (height * 0.35f).roundToInt()
        if ((right - left) < minWidth || (bottom - top) < minHeight) return null

        val insetX = ((right - left) * 0.03f).roundToInt()
        val insetY = ((bottom - top) * 0.03f).roundToInt()
        return Rect(
            max(left - insetX, 0),
            max(top - insetY, 0),
            min(right + insetX, width - 1),
            min(bottom + insetY, height - 1),
        )
    }

    private fun sobelEdges(
        luma: IntArray,
        width: Int,
        height: Int,
    ): FloatArray {
        val edges = FloatArray(width * height)
        for (y in 1 until height - 1) {
            for (x in 1 until width - 1) {
                val gx =
                    -luma[(y - 1) * width + (x - 1)] - 2 * luma[y * width + (x - 1)] - luma[(y + 1) * width + (x - 1)] +
                        luma[(y - 1) * width + (x + 1)] + 2 * luma[y * width + (x + 1)] + luma[(y + 1) * width + (x + 1)]
                val gy =
                    -luma[(y - 1) * width + (x - 1)] - 2 * luma[(y - 1) * width + x] - luma[(y - 1) * width + (x + 1)] +
                        luma[(y + 1) * width + (x - 1)] + 2 * luma[(y + 1) * width + x] + luma[(y + 1) * width + (x + 1)]
                edges[y * width + x] = hypot(gx.toFloat(), gy.toFloat())
            }
        }
        return edges
    }

    private fun toLumaArray(bitmap: Bitmap): IntArray {
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        return IntArray(pixels.size) { index ->
            val color = pixels[index]
            (0.299f * Color.red(color) + 0.587f * Color.green(color) + 0.114f * Color.blue(color)).roundToInt()
        }
    }

    private fun otsuThreshold(values: IntArray): Int {
        val histogram = IntArray(256)
        values.forEach { histogram[it.coerceIn(0, 255)]++ }
        val total = values.size
        var sum = 0.0
        for (index in histogram.indices) {
            sum += index * histogram[index]
        }

        var backgroundWeight = 0
        var backgroundSum = 0.0
        var maxVariance = 0.0
        var threshold = 127
        for (index in histogram.indices) {
            backgroundWeight += histogram[index]
            if (backgroundWeight == 0) continue
            val foregroundWeight = total - backgroundWeight
            if (foregroundWeight == 0) break
            backgroundSum += index * histogram[index]
            val backgroundMean = backgroundSum / backgroundWeight
            val foregroundMean = (sum - backgroundSum) / foregroundWeight
            val variance = backgroundWeight * foregroundWeight * (backgroundMean - foregroundMean) * (backgroundMean - foregroundMean)
            if (variance > maxVariance) {
                maxVariance = variance
                threshold = index
            }
        }
        return threshold
    }

    private fun removeBlackBorders(bitmap: Bitmap): Bitmap {
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        fun rowAverage(y: Int): Int {
            var total = 0
            for (x in 0 until bitmap.width) {
                total += Color.red(pixels[y * bitmap.width + x])
            }
            return total / bitmap.width
        }

        fun columnAverage(x: Int): Int {
            var total = 0
            for (y in 0 until bitmap.height) {
                total += Color.red(pixels[y * bitmap.width + x])
            }
            return total / bitmap.height
        }

        var top = 0
        var bottom = bitmap.height - 1
        var left = 0
        var right = bitmap.width - 1
        while (top < bottom && rowAverage(top) < 18) top++
        while (bottom > top && rowAverage(bottom) < 18) bottom--
        while (left < right && columnAverage(left) < 18) left++
        while (right > left && columnAverage(right) < 18) right--

        val cropWidth = (right - left).coerceAtLeast(1)
        val cropHeight = (bottom - top).coerceAtLeast(1)
        return Bitmap.createBitmap(bitmap, left, top, cropWidth, cropHeight)
    }

    private fun saveBitmap(
        bitmap: Bitmap,
        prefix: String,
    ): String {
        val dir = File(context.cacheDir, "processed").apply { mkdirs() }
        val file = File(dir, "${prefix}-${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { stream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 92, stream)
        }
        return Uri.fromFile(file).toString()
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

    private fun fallbackQuad(
        width: Int,
        height: Int,
    ): DocumentQuad =
        DocumentQuad(
            topLeft = PointValue(0.06f, 0.06f),
            topRight = PointValue(0.94f, 0.06f),
            bottomRight = PointValue(0.94f, 0.94f),
            bottomLeft = PointValue(0.06f, 0.94f),
        )

    private fun scaleQuadToBitmap(
        quad: DocumentQuad,
        width: Int,
        height: Int,
    ): DocumentQuad =
        DocumentQuad(
            topLeft = PointValue(quad.topLeft.x * width, quad.topLeft.y * height),
            topRight = PointValue(quad.topRight.x * width, quad.topRight.y * height),
            bottomRight = PointValue(quad.bottomRight.x * width, quad.bottomRight.y * height),
            bottomLeft = PointValue(quad.bottomLeft.x * width, quad.bottomLeft.y * height),
        )

    private fun distance(
        start: PointValue,
        end: PointValue,
    ): Float = hypot(abs(end.x - start.x), abs(end.y - start.y))
}
