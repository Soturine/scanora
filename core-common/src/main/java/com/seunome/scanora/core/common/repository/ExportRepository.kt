package com.seunome.scanora.core.common.repository

import com.seunome.scanora.core.common.model.ExportedFile
import com.seunome.scanora.core.common.model.ExportFormat
import com.seunome.scanora.core.common.model.PdfQuality
import com.seunome.scanora.core.common.model.ScanDocument

interface ExportRepository {
    suspend fun exportPdf(
        scan: ScanDocument,
        quality: PdfQuality,
    ): ExportedFile

    suspend fun exportImages(
        scan: ScanDocument,
        format: ExportFormat,
    ): List<ExportedFile>
}

