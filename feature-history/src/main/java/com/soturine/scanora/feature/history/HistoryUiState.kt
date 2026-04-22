package com.soturine.scanora.feature.history

import com.soturine.scanora.core.common.model.ScanDocument

data class HistoryUiState(
    val query: String = "",
    val scans: List<ScanDocument> = emptyList(),
)

