package com.seunome.scanora.feature.ocr

import com.seunome.scanora.core.common.model.ScanPage

data class OcrUiState(
    val page: ScanPage? = null,
    val text: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

