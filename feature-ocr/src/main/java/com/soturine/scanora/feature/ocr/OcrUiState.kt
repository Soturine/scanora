package com.soturine.scanora.feature.ocr

import com.soturine.scanora.core.common.model.OcrTextBlock
import com.soturine.scanora.core.common.model.ScanPage

data class OcrUiState(
    val page: ScanPage? = null,
    val previewImageUri: String? = null,
    val text: String = "",
    val blocks: List<OcrTextBlock> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

