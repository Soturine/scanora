package com.soturine.scanora.core.common.repository

import com.soturine.scanora.core.common.model.AppThemePreference
import com.soturine.scanora.core.common.model.PdfQuality
import com.soturine.scanora.core.common.model.ScanMode
import com.soturine.scanora.core.common.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val preferences: Flow<UserPreferences>

    suspend fun setOnboardingCompleted(completed: Boolean)

    suspend fun setThemePreference(preference: AppThemePreference)

    suspend fun setDefaultScanMode(mode: ScanMode)

    suspend fun setDefaultPdfQuality(quality: PdfQuality)
}

