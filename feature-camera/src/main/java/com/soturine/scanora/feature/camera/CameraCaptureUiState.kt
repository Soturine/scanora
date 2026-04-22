package com.soturine.scanora.feature.camera

import com.soturine.scanora.core.common.model.ScanMode

data class CameraCaptureUiState(
    val mode: ScanMode,
    val permissionGranted: Boolean = false,
    val isCapturing: Boolean = false,
    val errorMessage: String? = null,
)

