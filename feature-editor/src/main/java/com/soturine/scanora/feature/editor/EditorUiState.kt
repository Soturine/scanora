package com.soturine.scanora.feature.editor

import com.soturine.scanora.core.common.model.ScanDocument
import com.soturine.scanora.core.common.model.ScanPage

data class EditorUiState(
    val scan: ScanDocument? = null,
    val currentPage: ScanPage? = null,
    val isProcessing: Boolean = false,
    val errorMessage: String? = null,
)

