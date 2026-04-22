package com.soturine.scanora.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soturine.scanora.core.common.repository.ScanRepository
import com.soturine.scanora.core.common.usecase.SearchScansUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(
    scanRepository: ScanRepository,
    searchScansUseCase: SearchScansUseCase = SearchScansUseCase(),
) : ViewModel() {
    private val query = MutableStateFlow("")

    val uiState: StateFlow<HistoryUiState> = combine(
        scanRepository.observeScans(),
        query,
    ) { scans, currentQuery ->
        HistoryUiState(
            query = currentQuery,
            scans = searchScansUseCase(scans, currentQuery),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HistoryUiState(),
    )

    fun onQueryChange(value: String) {
        query.value = value
    }
}

