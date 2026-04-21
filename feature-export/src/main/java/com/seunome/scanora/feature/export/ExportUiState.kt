package com.seunome.scanora.feature.export

import com.seunome.scanora.core.common.model.ExportedFile
import com.seunome.scanora.core.common.model.ExportFormat
import com.seunome.scanora.core.common.model.PdfQuality
import com.seunome.scanora.core.common.model.ScanDocument

data class ExportUiState(
    val scan: ScanDocument? = null,
    val selectedFormat: ExportFormat = ExportFormat.PDF,
    val selectedQuality: PdfQuality = PdfQuality.BALANCED,
    val isExporting: Boolean = false,
    val exportedFiles: List<ExportedFile> = emptyList(),
    val errorMessage: String? = null,
)

