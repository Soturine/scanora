package com.seunome.scanora.feature.home

import com.seunome.scanora.core.common.model.ScanDocument
import com.seunome.scanora.core.common.model.ScanMode

data class HomeUiState(
    val isLoading: Boolean = true,
    val query: String = "",
    val selectedMode: ScanMode = ScanMode.DOCUMENT,
    val recentScans: List<ScanDocument> = emptyList(),
)

