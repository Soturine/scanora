package com.soturine.scanora.feature.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.home_app_bar_title))
                },
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
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Card {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.home_hero_title),
                            style = MaterialTheme.typography.headlineMedium,
                        )
                        Text(
                            text = stringResource(id = R.string.home_hero_subtitle),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onOpenCamera(state.selectedMode) },
                        ) {
                            Text(text = stringResource(id = R.string.home_scan_action))
                        }
                        Button(
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
                Text(
                    text = stringResource(id = R.string.home_mode_section),
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.height(12.dp))
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
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.query,
                    onValueChange = onQueryChange,
                    label = { Text(text = stringResource(id = R.string.home_search_label)) },
                    placeholder = { Text(text = stringResource(id = R.string.home_search_placeholder)) },
                )
            }

            item {
                Text(
                    text = stringResource(id = R.string.home_recent_section),
                    style = MaterialTheme.typography.titleLarge,
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
                items(state.recentScans, key = { it.id }) { scan ->
                    PageThumbnailCard(
                        title = scan.title,
                        subtitle = stringResource(
                            id = R.string.home_recent_subtitle,
                            scan.pageCount,
                            dateFormatter(scan.updatedAt),
                        ),
                        imageUri = scan.coverPage?.displayUri,
                        onClick = { onOpenScan(scan.id) },
                    )
                }
            }
        }
    }
}
