package com.soturine.scanora.feature.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soturine.scanora.core.common.model.DocumentFilterType
import com.soturine.scanora.core.common.model.DocumentQuad
import com.soturine.scanora.core.common.model.ScanPage
import com.soturine.scanora.core.common.repository.DocumentProcessingRepository
import com.soturine.scanora.core.common.repository.ScanRepository
import com.soturine.scanora.core.common.usecase.ValidateDocumentNameUseCase
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.Job
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
    private val isPreviewLoading = MutableStateFlow(false)
    private val previewImageUri = MutableStateFlow<String?>(null)
    private val errorMessage = MutableStateFlow<String?>(null)

    private val previewCache = mutableMapOf<String, String>()
    private var previewJob: Job? = null
    private var quadJob: Job? = null

    private data class EditorBaseState(
        val scan: com.soturine.scanora.core.common.model.ScanDocument?,
        val pageId: String?,
        val isProcessing: Boolean,
        val isPreviewLoading: Boolean,
    )

    private val baseState = combine(
        scanRepository.observeScan(scanId),
        selectedPageId,
        isProcessing,
        isPreviewLoading,
    ) { scan, pageId, processing, previewLoading ->
        EditorBaseState(
            scan = scan,
            pageId = pageId,
            isProcessing = processing,
            isPreviewLoading = previewLoading,
        )
    }

    val uiState: StateFlow<EditorUiState> = combine(
        baseState,
        previewImageUri,
        errorMessage,
    ) { base, previewUri, message ->
        val resolvedPage = base.scan?.pages
            ?.sortedBy { it.index }
            ?.firstOrNull { it.id == base.pageId }
            ?: base.scan?.pages?.sortedBy { it.index }?.firstOrNull()
        EditorUiState(
            scan = base.scan,
            currentPage = resolvedPage,
            isProcessing = base.isProcessing,
            isPreviewLoading = base.isPreviewLoading,
            previewImageUri = previewUri ?: resolvedPage?.displayUri,
            errorMessage = message,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EditorUiState(),
    )

    fun selectPage(pageId: String) {
        selectedPageId.value = pageId
        previewImageUri.value = null
        previewCache.clear()
    }

    fun ensureQuadForCurrentPage(force: Boolean = false) {
        val page = uiState.value.currentPage ?: return
        if (!force && page.quad != null) return
        if (quadJob?.isActive == true) return

        quadJob = viewModelScope.launch {
            isProcessing.value = true
            errorMessage.value = null
            runCatching {
                val suggestedQuad = processingRepository.estimateDocumentQuad(page.sourceUri)
                scanRepository.updatePage(
                    scanId = scanId,
                    page = page.copy(quad = suggestedQuad),
                )
            }.onFailure { throwable ->
                if (throwable !is CancellationException) {
                    errorMessage.value = throwable.message ?: "Não foi possível sugerir os cantos do documento."
                }
            }
            isProcessing.value = false
        }
    }

    fun updateQuad(quad: DocumentQuad) {
        val page = uiState.value.currentPage ?: return
        previewImageUri.value = null
        previewCache.clear()
        viewModelScope.launch {
            scanRepository.updatePage(scanId, page.copy(quad = quad))
        }
    }

    fun prepareFilterPreview(filterType: DocumentFilterType) {
        val page = uiState.value.currentPage ?: return
        previewJob?.cancel()

        val currentDisplayUri = page.displayUri
        if (filterType == page.filterType && page.processedUri != null) {
            previewImageUri.value = currentDisplayUri
            isPreviewLoading.value = false
            return
        }

        val cacheKey = buildPreviewCacheKey(page, filterType)
        previewCache[cacheKey]?.let { cachedPreview ->
            previewImageUri.value = cachedPreview
            isPreviewLoading.value = false
            return
        }

        previewJob = viewModelScope.launch {
            isPreviewLoading.value = true
            errorMessage.value = null
            runCatching {
                val effectiveQuad = page.quad ?: processingRepository.estimateDocumentQuad(page.sourceUri)
                if (page.quad == null) {
                    scanRepository.updatePage(
                        scanId = scanId,
                        page = page.copy(quad = effectiveQuad),
                    )
                }
                processingRepository.renderPreview(
                    sourceUri = page.sourceUri,
                    filterType = filterType,
                    quad = effectiveQuad,
                    rotationDegrees = page.rotationDegrees,
                )
            }.onSuccess { previewUri ->
                previewCache[cacheKey] = previewUri
                previewImageUri.value = previewUri
            }.onFailure { throwable ->
                if (throwable !is CancellationException) {
                    previewImageUri.value = currentDisplayUri
                    errorMessage.value = throwable.message ?: "Não foi possível atualizar a prévia do filtro."
                }
            }
            isPreviewLoading.value = false
        }
    }

    fun applyFilter(filterType: DocumentFilterType) {
        val page = uiState.value.currentPage ?: return
        previewJob?.cancel()
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
                processedUri
            }.onSuccess { processedUri ->
                previewCache.clear()
                previewImageUri.value = processedUri
            }.onFailure { throwable ->
                if (throwable !is CancellationException) {
                    errorMessage.value = throwable.message ?: "Não foi possível aplicar o visual da página."
                }
            }
            isProcessing.value = false
        }
    }

    fun rotateCurrentPage() {
        val page = uiState.value.currentPage ?: return
        previewJob?.cancel()
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
                processedUri
            }.onSuccess { processedUri ->
                previewCache.clear()
                previewImageUri.value = processedUri
            }.onFailure { throwable ->
                if (throwable !is CancellationException) {
                    errorMessage.value = throwable.message ?: "Não foi possível girar a página."
                }
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

    private fun buildPreviewCacheKey(
        page: ScanPage,
        filterType: DocumentFilterType,
    ): String = buildString {
        append(page.id)
        append('|')
        append(filterType.storageKey)
        append('|')
        append(page.rotationDegrees)
        append('|')
        append(page.quad?.hashCode() ?: "no-quad")
    }
}
