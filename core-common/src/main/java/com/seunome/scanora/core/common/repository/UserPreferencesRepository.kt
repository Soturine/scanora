package com.seunome.scanora.core.common.repository

import com.seunome.scanora.core.common.model.AppThemePreference
import com.seunome.scanora.core.common.model.PdfQuality
import com.seunome.scanora.core.common.model.ScanMode
import com.seunome.scanora.core.common.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val preferences: Flow<UserPreferences>

    suspend fun setOnboardingCompleted(completed: Boolean)

    suspend fun setThemePreference(preference: AppThemePreference)

    suspend fun setDefaultScanMode(mode: ScanMode)

    suspend fun setDefaultPdfQuality(quality: PdfQuality)
}

