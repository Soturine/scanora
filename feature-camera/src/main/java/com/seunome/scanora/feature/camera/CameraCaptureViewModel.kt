package com.seunome.scanora.feature.camera

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.seunome.scanora.core.common.model.ScanMode

class CameraCaptureViewModel(
    mode: ScanMode,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CameraCaptureUiState(mode = mode))
    val uiState: StateFlow<CameraCaptureUiState> = _uiState.asStateFlow()

    fun onPermissionResult(granted: Boolean) {
        _uiState.update { it.copy(permissionGranted = granted) }
    }

    fun onCaptureStarted() {
        _uiState.update { it.copy(isCapturing = true, errorMessage = null) }
    }

    fun onCaptureFinished() {
        _uiState.update { it.copy(isCapturing = false) }
    }

    fun onError(message: String) {
        _uiState.update { it.copy(isCapturing = false, errorMessage = message) }
    }
}

