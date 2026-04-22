package com.soturine.scanora.core.common.repository

import com.soturine.scanora.core.common.model.ExportedFile
import com.soturine.scanora.core.common.model.ExportFormat
import com.soturine.scanora.core.common.model.PdfQuality
import com.soturine.scanora.core.common.model.ScanDocument

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

