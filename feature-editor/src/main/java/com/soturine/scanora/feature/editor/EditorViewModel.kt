package com.soturine.scanora.feature.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soturine.scanora.core.common.model.DocumentFilterType
import com.soturine.scanora.core.common.model.DocumentQuad
import com.soturine.scanora.core.common.repository.DocumentProcessingRepository
import com.soturine.scanora.core.common.repository.ScanRepository
import com.soturine.scanora.core.common.usecase.ValidateDocumentNameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditorViewModel(
    private val scanId: String,
    initialPageId: String?,
    private val scanRepository: ScanRepository,
    private val processingRepository: DocumentProcessingRepository,
    private val validateDocumentNameUseCase: ValidateDocumentNameUseCase = ValidateDocumentNameUseCase(),
) : ViewModel() {
    private val selectedPageId = MutableStateFlow(initialPageId)
    private val isProcessing = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<EditorUiState> = combine(
        scanRepository.observeScan(scanId),
        selectedPageId,
        isProcessing,
        errorMessage,
    ) { scan, pageId, processing, message ->
        val resolvedPage = scan?.pages
            ?.sortedBy { it.index }
            ?.firstOrNull { it.id == pageId }
            ?: scan?.pages?.sortedBy { it.index }?.firstOrNull()
        EditorUiState(
            scan = scan,
            currentPage = resolvedPage,
            isProcessing = processing,
            errorMessage = message,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EditorUiState(),
    )

    fun selectPage(pageId: String) {
        selectedPageId.value = pageId
    }

    fun updateQuad(quad: DocumentQuad) {
        val page = uiState.value.currentPage ?: return
        viewModelScope.launch {
            scanRepository.updatePage(scanId, page.copy(quad = quad))
        }
    }

    fun applyFilter(filterType: DocumentFilterType) {
        val page = uiState.value.currentPage ?: return
        viewModelScope.launch {
            isProcessing.value = true
            errorMessage.value = null
            runCatching {
                val effectiveQuad = page.quad ?: processingRepository.estimateDocumentQuad(page.sourceUri)
                val processedUri = processingRepository.processPage(
                    sourceUri = page.sourceUri,
                    filterType = filterType,
                    quad = effectiveQuad,
                    rotationDegrees = page.rotationDegrees,
                )
                scanRepository.updatePage(
                    scanId = scanId,
                    page = page.copy(
                        quad = effectiveQuad,
                        processedUri = processedUri,
                        filterType = filterType,
                    ),
                )
            }.onFailure { throwable ->
                errorMessage.value = throwable.message ?: "Não foi possível processar a página."
            }
            isProcessing.value = false
        }
    }

    fun rotateCurrentPage() {
        val page = uiState.value.currentPage ?: return
        viewModelScope.launch {
            isProcessing.value = true
            errorMessage.value = null
            runCatching {
                val newRotation = (page.rotationDegrees + 90) % 360
                val effectiveQuad = page.quad ?: processingRepository.estimateDocumentQuad(page.sourceUri)
                val processedUri = processingRepository.processPage(
                    sourceUri = page.sourceUri,
                    filterType = page.filterType,
                    quad = effectiveQuad,
                    rotationDegrees = newRotation,
                )
                scanRepository.updatePage(
                    scanId = scanId,
                    page = page.copy(
                        quad = effectiveQuad,
                        processedUri = processedUri,
                        rotationDegrees = newRotation,
                    ),
                )
            }.onFailure { throwable ->
                errorMessage.value = throwable.message ?: "Não foi possível girar a página."
            }
            isProcessing.value = false
        }
    }

    fun renameScan(title: String) {
        val result = validateDocumentNameUseCase(title)
        if (!result.isValid) {
            errorMessage.value = result.errorMessage
            return
        }
        if (result.sanitizedValue == uiState.value.scan?.title) return
        viewModelScope.launch {
            scanRepository.renameScan(scanId, result.sanitizedValue)
        }
    }

    fun updateTags(rawValue: String) {
        val tags = rawValue
            .split(",")
            .map(String::trim)
            .filter(String::isNotBlank)
            .distinct()
        if (tags == uiState.value.scan?.tags) return
        viewModelScope.launch {
            scanRepository.updateTags(scanId, tags)
        }
    }

    fun movePage(pageId: String, direction: Int) {
        val scan = uiState.value.scan ?: return
        val orderedIds = scan.pages.sortedBy { it.index }.map { it.id }.toMutableList()
        val currentIndex = orderedIds.indexOf(pageId)
        if (currentIndex == -1) return
        val targetIndex = (currentIndex + direction).coerceIn(0, orderedIds.lastIndex)
        if (currentIndex == targetIndex) return
        orderedIds.removeAt(currentIndex)
        orderedIds.add(targetIndex, pageId)
        viewModelScope.launch {
            scanRepository.updatePageOrder(scanId, orderedIds)
        }
    }

    fun deleteCurrentPage() {
        val page = uiState.value.currentPage ?: return
        viewModelScope.launch {
            scanRepository.deletePage(scanId, page.id)
        }
    }

    fun clearMessage() {
        errorMessage.update { null }
    }
}
