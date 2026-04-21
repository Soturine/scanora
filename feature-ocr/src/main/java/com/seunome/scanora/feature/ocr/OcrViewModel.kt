package com.seunome.scanora.feature.ocr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seunome.scanora.core.common.model.ScanPage
import com.seunome.scanora.core.common.repository.OcrRepository
import com.seunome.scanora.core.common.repository.ScanRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OcrViewModel(
    private val scanId: String,
    private val pageId: String,
    private val scanRepository: ScanRepository,
    private val ocrRepository: OcrRepository,
) : ViewModel() {
    private val recognizedText = MutableStateFlow("")
    private val isLoading = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<OcrUiState> = combine(
        scanRepository.observeScan(scanId),
        recognizedText,
        isLoading,
        errorMessage,
    ) { scan, text, loading, message ->
        val page = scan?.pages?.firstOrNull { it.id == pageId }
        OcrUiState(
            page = page,
            text = if (text.isBlank()) page?.ocrText.orEmpty() else text,
            isLoading = loading,
            errorMessage = message,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = OcrUiState(),
    )

    init {
        viewModelScope.launch {
            val page = scanRepository.observeScan(scanId)
                .map { scan -> scan?.pages?.firstOrNull { it.id == pageId } }
                .filterNotNull()
                .first()
            runRecognition(page)
        }
    }

    fun recognize() {
        val page = uiState.value.page ?: return
        viewModelScope.launch {
            runRecognition(page)
        }
    }

    fun clearMessage() {
        errorMessage.value = null
    }

    private suspend fun runRecognition(page: ScanPage) {
        isLoading.value = true
        errorMessage.value = null
        runCatching {
            val text = ocrRepository.recognizeText(page.displayUri)
            recognizedText.value = text
            scanRepository.updatePageOcr(scanId, pageId, text)
        }.onFailure { throwable ->
            errorMessage.value = throwable.message ?: "Não foi possível reconhecer o texto."
        }
        isLoading.value = false
    }
}
