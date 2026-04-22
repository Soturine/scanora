package com.soturine.scanora.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soturine.scanora.core.common.repository.ScanRepository
import com.soturine.scanora.core.common.repository.UserPreferencesRepository
import com.soturine.scanora.core.common.usecase.SearchScansUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    scanRepository: ScanRepository,
    preferencesRepository: UserPreferencesRepository,
    searchScansUseCase: SearchScansUseCase = SearchScansUseCase(),
) : ViewModel() {
    private val query = MutableStateFlow("")
    private val selectedMode = MutableStateFlow<com.soturine.scanora.core.common.model.ScanMode?>(null)

    val uiState: StateFlow<HomeUiState> = combine(
        scanRepository.observeScans(),
        preferencesRepository.preferences,
        query,
        selectedMode,
    ) { scans, preferences, currentQuery, manualMode ->
        HomeUiState(
            isLoading = false,
            query = currentQuery,
            selectedMode = manualMode ?: preferences.defaultScanMode,
            recentScans = searchScansUseCase(scans, currentQuery).take(20),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(),
    )

    fun onQueryChange(value: String) {
        query.value = value
    }

    fun onModeSelected(mode: com.soturine.scanora.core.common.model.ScanMode) {
        selectedMode.value = mode
    }
}

