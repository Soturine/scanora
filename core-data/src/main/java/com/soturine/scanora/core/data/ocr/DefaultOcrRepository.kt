package com.soturine.scanora.core.data.ocr

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.soturine.scanora.core.common.repository.OcrRepository
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultOcrRepository(
    private val context: Context,
) : OcrRepository {
    override suspend fun recognizeText(imageUri: String): String = withContext(Dispatchers.IO) {
        val bitmap = loadBitmap(imageUri) ?: return@withContext ""
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val result = recognizer.process(inputImage).awaitResult()
        recognizer.close()
        result.text
    }

    private fun loadBitmap(imageUri: String) =
        openInputStream(Uri.parse(imageUri))?.use(BitmapFactory::decodeStream)

    private fun openInputStream(uri: Uri) =
        if (uri.scheme.isNullOrBlank()) {
            File(uri.toString()).inputStream()
        } else {
            context.contentResolver.openInputStream(uri)
        }
}

