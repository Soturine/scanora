package com.soturine.scanora.feature.settings

import com.soturine.scanora.core.common.model.UserPreferences

data class SettingsUiState(
    val preferences: UserPreferences = UserPreferences(),
)

