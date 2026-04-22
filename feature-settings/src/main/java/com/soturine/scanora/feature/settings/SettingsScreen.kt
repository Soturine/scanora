package com.soturine.scanora.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.soturine.scanora.core.common.model.AppThemePreference
import com.soturine.scanora.core.common.model.PdfQuality
import com.soturine.scanora.core.common.model.ScanMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onThemeSelected: (AppThemePreference) -> Unit,
    onDefaultModeSelected: (ScanMode) -> Unit,
    onPdfQualitySelected: (PdfQuality) -> Unit,
    onResetOnboarding: () -> Unit,
    onOpenAbout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.settings_title)) },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(id = R.string.settings_theme_section),
                style = MaterialTheme.typography.titleLarge,
            )
            AppThemePreference.entries.forEach { preference ->
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onThemeSelected(preference) },
                ) {
                    Text(
                        text = if (state.preferences.themePreference == preference) {
                            "${preference.displayName()} | selecionado"
                        } else {
                            preference.displayName()
                        },
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.settings_default_mode_section),
                style = MaterialTheme.typography.titleLarge,
            )
            ScanMode.entries.forEach { mode ->
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onDefaultModeSelected(mode) },
                ) {
                    Text(
                        text = if (state.preferences.defaultScanMode == mode) {
                            "${mode.title} | selecionado"
                        } else {
                            mode.title
                        },
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.settings_pdf_quality_section),
                style = MaterialTheme.typography.titleLarge,
            )
            PdfQuality.entries.forEach { quality ->
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onPdfQualitySelected(quality) },
                ) {
                    Text(
                        text = if (state.preferences.defaultPdfQuality == quality) {
                            "${quality.title} | selecionado"
                        } else {
                            quality.title
                        },
                    )
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onResetOnboarding,
            ) {
                Text(text = stringResource(id = R.string.settings_reset_onboarding))
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onOpenAbout,
            ) {
                Text(text = stringResource(id = R.string.settings_open_about))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onOpenPrivacyPolicy: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.settings_about_title)) },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(id = R.string.settings_about_body),
                style = MaterialTheme.typography.bodyLarge,
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onOpenPrivacyPolicy,
            ) {
                Text(text = stringResource(id = R.string.settings_open_privacy))
            }
        }
    }
}

private fun AppThemePreference.displayName(): String = when (this) {
    AppThemePreference.SYSTEM -> "Sistema"
    AppThemePreference.LIGHT -> "Claro"
    AppThemePreference.DARK -> "Escuro"
}
