package com.soturine.scanora.feature.home

import com.soturine.scanora.core.common.model.ScanDocument
import com.soturine.scanora.core.common.model.ScanMode

data class HomeUiState(
    val isLoading: Boolean = true,
    val manualMode: ScanMode = ScanMode.DOCUMENT,
    val recentScans: List<ScanDocument> = emptyList(),
)

