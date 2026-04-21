package com.seunome.scanora.feature.history

import com.seunome.scanora.core.common.model.ScanDocument

data class HistoryUiState(
    val query: String = "",
    val scans: List<ScanDocument> = emptyList(),
)

