package com.soturine.scanora.core.data.export

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.soturine.scanora.core.common.model.ExportedFile
import com.soturine.scanora.core.common.model.ExportFormat
import com.soturine.scanora.core.common.model.PdfQuality
import com.soturine.scanora.core.common.model.ScanDocument
import com.soturine.scanora.core.common.model.ScanPage
import com.soturine.scanora.core.common.model.requiresDerivedImage
import com.soturine.scanora.core.common.repository.DocumentProcessingRepository
import com.soturine.scanora.core.common.repository.ExportRepository
import java.io.ByteArrayOutputStream
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultExportRepository(
    private val context: Context,
    private val processingRepository: DocumentProcessingRepository,
    private val fileNameBuilder: ExportFileNameBuilder = ExportFileNameBuilder(),
) : ExportRepository {
    override suspend fun exportPdf(
        scan: ScanDocument,
        quality: PdfQuality,
    ): ExportedFile = withContext(Dispatchers.IO) {
        val document = PdfDocument()
        scan.pages.sortedBy { it.index }.forEachIndexed { pageNumber, page ->
            val bitmap = loadBitmap(page) ?: return@forEachIndexed
            val compressed = compressForPdf(bitmap, quality)
            val pageInfo = PdfDocument.PageInfo.Builder(
                compressed.width,
                compressed.height,
                pageNumber + 1,
            ).create()
            val pdfPage = document.startPage(pageInfo)
            pdfPage.canvas.drawBitmap(compressed, 0f, 0f, null)
            document.finishPage(pdfPage)
        }

        val displayName = fileNameBuilder.buildBaseName(scan.title, ExportFormat.PDF)
        val bytes = ByteArrayOutputStream().use { output ->
            document.writeTo(output)
            output.toByteArray()
        }
        document.close()
        writeBytes(
            displayName = displayName,
            mimeType = ExportFormat.PDF.mimeType,
            bytes = bytes,
        )
    }

    override suspend fun exportImages(
        scan: ScanDocument,
        format: ExportFormat,
    ): List<ExportedFile> = withContext(Dispatchers.IO) {
        scan.pages.sortedBy { it.index }.mapNotNull { page ->
            val bitmap = loadBitmap(page) ?: return@mapNotNull null
            val displayName = fileNameBuilder.buildPageName(
                title = scan.title,
                pageIndex = page.index,
                format = format,
            )
            writeBytes(
                displayName = displayName,
                mimeType = format.mimeType,
                bytes = bitmapToBytes(bitmap, format),
            )
        }
    }

    private fun compressForPdf(
        bitmap: Bitmap,
        quality: PdfQuality,
    ): Bitmap {
        val bytes = ByteArrayOutputStream().use { output ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality.jpegQuality, output)
            output.toByteArray()
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: bitmap
    }

    private fun bitmapToBytes(
        bitmap: Bitmap,
        format: ExportFormat,
    ): ByteArray =
        ByteArrayOutputStream().use { output ->
            bitmap.compress(
                if (format == ExportFormat.PNG) Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.JPEG,
                92,
                output,
            )
            output.toByteArray()
        }

    private suspend fun loadBitmap(page: ScanPage): Bitmap? {
        val finalImageUri = if (page.requiresDerivedImage()) {
            processingRepository.processPage(
                sourceUri = page.sourceUri,
                filterType = page.filterType,
                quad = page.quad,
                rotationDegrees = page.rotationDegrees,
            )
        } else {
            page.canonicalUri
        }
        val uri = Uri.parse(finalImageUri)
        val stream = when {
            uri.scheme.isNullOrBlank() -> File(finalImageUri).inputStream()
            else -> context.contentResolver.openInputStream(uri)
        }
        return stream?.use(BitmapFactory::decodeStream)
    }

    private fun writeBytes(
        displayName: String,
        mimeType: String,
        bytes: ByteArray,
    ): ExportedFile {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeToDownloads(displayName, mimeType, bytes)
        } else {
            writeToAppStorage(displayName, mimeType, bytes)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun writeToDownloads(
        displayName: String,
        mimeType: String,
        bytes: ByteArray,
    ): ExportedFile {
        val resolver = context.contentResolver
        val relativePath = "${Environment.DIRECTORY_DOWNLOADS}/Scanora"
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
            ?: error("Não foi possível preparar o arquivo para exportação.")

        try {
            resolver.openOutputStream(uri)?.use { stream ->
                stream.write(bytes)
                stream.flush()
            } ?: error("Não foi possível gravar o arquivo exportado.")

            val publishedValues = ContentValues().apply {
                put(MediaStore.MediaColumns.IS_PENDING, 0)
            }
            resolver.update(uri, publishedValues, null, null)
        } catch (throwable: Throwable) {
            resolver.delete(uri, null, null)
            throw throwable
        }

        return ExportedFile(
            displayName = displayName,
            uri = uri.toString(),
            mimeType = mimeType,
            sizeBytes = bytes.size.toLong(),
            locationLabel = "Downloads > Scanora",
            pathHint = "Downloads/Scanora/$displayName",
        )
    }

    private fun writeToAppStorage(
        displayName: String,
        mimeType: String,
        bytes: ByteArray,
    ): ExportedFile {
        val baseDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: context.filesDir
        val exportDir = File(baseDir, "scanora-exports").apply { mkdirs() }
        val file = File(exportDir, displayName)
        file.writeBytes(bytes)
        return ExportedFile(
            displayName = displayName,
            uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file,
            ).toString(),
            mimeType = mimeType,
            sizeBytes = bytes.size.toLong(),
            locationLabel = "Armazenamento do app > scanora-exports",
            pathHint = file.absolutePath,
        )
    }
}
