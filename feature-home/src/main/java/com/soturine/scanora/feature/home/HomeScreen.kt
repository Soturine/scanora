package com.soturine.scanora.feature.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.soturine.scanora.core.common.model.ScanMode
import com.soturine.scanora.core.common.usecase.FormatScanDateUseCase
import com.soturine.scanora.core.ui.component.EmptyStateCard
import com.soturine.scanora.core.ui.component.ModeCard
import com.soturine.scanora.core.ui.component.PageThumbnailCard
import com.soturine.scanora.core.ui.component.SectionHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    onOpenCamera: (ScanMode) -> Unit,
    onImportImages: (ScanMode, List<String>) -> Unit,
    onModeSelected: (ScanMode) -> Unit,
    onQueryChange: (String) -> Unit,
    onOpenHistory: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenScan: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dateFormatter = remember { FormatScanDateUseCase() }
    val heroChips = listOf(
        stringResource(id = R.string.home_chip_local),
        stringResource(id = R.string.home_chip_ocr),
        stringResource(id = R.string.home_chip_export),
    )
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 12),
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            onImportImages(state.selectedMode, uris.map(Uri::toString))
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.home_app_bar_title)) },
                actions = {
                    IconButton(onClick = onOpenHistory) {
                        Icon(
                            imageVector = Icons.Outlined.History,
                            contentDescription = stringResource(id = R.string.home_history),
                        )
                    }
                    IconButton(onClick = onOpenSettings) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = stringResource(id = R.string.home_settings),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                    ) {
                        SectionHeader(
                            eyebrow = stringResource(id = R.string.home_hero_eyebrow),
                            title = stringResource(id = R.string.home_hero_title),
                            supportingText = stringResource(id = R.string.home_hero_subtitle),
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(heroChips) { chip ->
                                Surface(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = MaterialTheme.shapes.small,
                                ) {
                                    Text(
                                        text = chip,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        }
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onOpenCamera(state.selectedMode) },
                        ) {
                            Text(text = stringResource(id = R.string.home_scan_action))
                        }
                        FilledTonalButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                importLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                                )
                            },
                        ) {
                            Text(text = stringResource(id = R.string.home_import_action))
                        }
                    }
                }
            }

            item {
                SectionHeader(
                    eyebrow = stringResource(id = R.string.home_mode_eyebrow),
                    title = stringResource(id = R.string.home_mode_section),
                    supportingText = stringResource(id = R.string.home_mode_supporting),
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ScanMode.entries.forEach { mode ->
                        ModeCard(
                            mode = mode,
                            selected = state.selectedMode == mode,
                            onClick = { onModeSelected(mode) },
                        )
                    }
                }
            }

            item {
                SectionHeader(
                    eyebrow = stringResource(id = R.string.home_recent_eyebrow),
                    title = stringResource(id = R.string.home_recent_section),
                    supportingText = if (state.recentScans.isEmpty()) {
                        stringResource(id = R.string.home_recent_empty_supporting)
                    } else {
                        stringResource(id = R.string.home_recent_supporting, state.recentScans.size)
                    },
                )
            }

            if (state.recentScans.isEmpty()) {
                item {
                    EmptyStateCard(
                        title = stringResource(id = R.string.home_empty_title),
                        message = stringResource(id = R.string.home_empty_message),
                    )
                }
            } else {
                item {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.query,
                        onValueChange = onQueryChange,
                        label = { Text(text = stringResource(id = R.string.home_search_label)) },
                        placeholder = { Text(text = stringResource(id = R.string.home_search_placeholder)) },
                        singleLine = true,
                    )
                }
                items(state.recentScans, key = { it.id }) { scan ->
                    PageThumbnailCard(
                        title = scan.title,
                        subtitle = stringResource(
                            id = R.string.home_recent_subtitle,
                            scan.pageCount,
                            dateFormatter(scan.updatedAt),
                        ),
                        imageUri = scan.coverPage?.displayUri,
                        overline = scan.mode.title,
                        badge = when {
                            scan.isFavorite -> stringResource(id = R.string.home_badge_favorite)
                            scan.isDraft -> stringResource(id = R.string.home_badge_draft)
                            else -> null
                        },
                        onClick = { onOpenScan(scan.id) },
                    )
                }
            }
        }
    }
}
