package com.seunome.scanora.feature.settings

import com.seunome.scanora.core.common.model.UserPreferences

data class SettingsUiState(
    val preferences: UserPreferences = UserPreferences(),
)

