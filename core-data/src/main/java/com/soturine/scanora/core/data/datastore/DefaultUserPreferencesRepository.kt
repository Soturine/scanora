package com.soturine.scanora.core.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.soturine.scanora.core.common.model.AppThemePreference
import com.soturine.scanora.core.common.model.PdfQuality
import com.soturine.scanora.core.common.model.ScanMode
import com.soturine.scanora.core.common.model.UserPreferences
import com.soturine.scanora.core.common.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.scanoraPreferencesDataStore by preferencesDataStore(name = "scanora_preferences")

class DefaultUserPreferencesRepository(
    private val context: Context,
) : UserPreferencesRepository {
    override val preferences: Flow<UserPreferences> =
        context.scanoraPreferencesDataStore.data.map { prefs ->
            UserPreferences(
                onboardingCompleted = prefs[Keys.OnboardingCompleted] ?: false,
                themePreference = AppThemePreference.fromStorageKey(
                    prefs[Keys.ThemePreference] ?: AppThemePreference.SYSTEM.storageKey,
                ),
                defaultScanMode = ScanMode.fromStorageKey(
                    prefs[Keys.DefaultScanMode] ?: ScanMode.DOCUMENT.storageKey,
                ),
                defaultPdfQuality = PdfQuality.fromStorageKey(
                    prefs[Keys.DefaultPdfQuality] ?: PdfQuality.BALANCED.storageKey,
                ),
            )
        }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        context.scanoraPreferencesDataStore.edit { prefs ->
            prefs[Keys.OnboardingCompleted] = completed
        }
    }

    override suspend fun setThemePreference(preference: AppThemePreference) {
        context.scanoraPreferencesDataStore.edit { prefs ->
            prefs[Keys.ThemePreference] = preference.storageKey
        }
    }

    override suspend fun setDefaultScanMode(mode: ScanMode) {
        context.scanoraPreferencesDataStore.edit { prefs ->
            prefs[Keys.DefaultScanMode] = mode.storageKey
        }
    }

    override suspend fun setDefaultPdfQuality(quality: PdfQuality) {
        context.scanoraPreferencesDataStore.edit { prefs ->
            prefs[Keys.DefaultPdfQuality] = quality.storageKey
        }
    }

    private object Keys {
        val OnboardingCompleted = booleanPreferencesKey("onboarding_completed")
        val ThemePreference = stringPreferencesKey("theme_preference")
        val DefaultScanMode = stringPreferencesKey("default_scan_mode")
        val DefaultPdfQuality = stringPreferencesKey("default_pdf_quality")
    }
}

