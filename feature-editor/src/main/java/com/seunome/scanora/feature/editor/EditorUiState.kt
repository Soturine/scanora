package com.seunome.scanora.feature.editor

import com.seunome.scanora.core.common.model.ScanDocument
import com.seunome.scanora.core.common.model.ScanPage

data class EditorUiState(
    val scan: ScanDocument? = null,
    val currentPage: ScanPage? = null,
    val isProcessing: Boolean = false,
    val errorMessage: String? = null,
)

