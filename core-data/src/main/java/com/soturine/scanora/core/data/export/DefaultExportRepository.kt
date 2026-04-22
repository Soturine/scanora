package com.soturine.scanora.core.data.export

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.soturine.scanora.core.common.model.ExportedFile
import com.soturine.scanora.core.common.model.ExportFormat
import com.soturine.scanora.core.common.model.PdfQuality
import com.soturine.scanora.core.common.model.ScanDocument
import com.soturine.scanora.core.common.model.ScanPage
import com.soturine.scanora.core.common.repository.ExportRepository
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultExportRepository(
    private val context: Context,
    private val fileNameBuilder: ExportFileNameBuilder = ExportFileNameBuilder(),
) : ExportRepository {
    override suspend fun exportPdf(
        scan: ScanDocument,
        quality: PdfQuality,
    ): ExportedFile = withContext(Dispatchers.IO) {
        val exportDir = exportDirectory()
        val outputFile = File(
            exportDir,
            fileNameBuilder.buildBaseName(scan.title, ExportFormat.PDF),
        )

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

        FileOutputStream(outputFile).use(document::writeTo)
        document.close()

        exportedFile(outputFile, ExportFormat.PDF)
    }

    override suspend fun exportImages(
        scan: ScanDocument,
        format: ExportFormat,
    ): List<ExportedFile> = withContext(Dispatchers.IO) {
        val exportDir = exportDirectory()
        scan.pages.sortedBy { it.index }.mapNotNull { page ->
            val bitmap = loadBitmap(page) ?: return@mapNotNull null
            val outputFile = File(
                exportDir,
                fileNameBuilder.buildPageName(
                    title = scan.title,
                    pageIndex = page.index,
                    format = format,
                ),
            )
            FileOutputStream(outputFile).use { stream ->
                bitmap.compress(
                    if (format == ExportFormat.PNG) Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.JPEG,
                    92,
                    stream,
                )
            }
            exportedFile(outputFile, format)
        }
    }

    private fun compressForPdf(
        bitmap: Bitmap,
        quality: PdfQuality,
    ): Bitmap {
        val temp = File.createTempFile("scanora-pdf", ".jpg", context.cacheDir)
        FileOutputStream(temp).use { stream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality.jpegQuality, stream)
        }
        val compressed = BitmapFactory.decodeFile(temp.absolutePath)
        temp.delete()
        return compressed ?: bitmap
    }

    private fun loadBitmap(page: ScanPage): Bitmap? {
        val uri = Uri.parse(page.displayUri)
        val stream = when {
            uri.scheme.isNullOrBlank() -> File(page.displayUri).inputStream()
            else -> context.contentResolver.openInputStream(uri)
        }
        return stream?.use(BitmapFactory::decodeStream)
    }

    private fun exportDirectory(): File {
        val baseDir = context.getExternalFilesDir(android.os.Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
        return File(baseDir, "exports").apply { mkdirs() }
    }

    private fun exportedFile(
        file: File,
        format: ExportFormat,
    ): ExportedFile =
        ExportedFile(
            displayName = file.name,
            uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file,
            ).toString(),
            mimeType = format.mimeType,
            sizeBytes = file.length(),
        )
}

