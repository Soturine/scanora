package com.soturine.scanora.feature.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.soturine.scanora.core.common.model.ScanDocument
import com.soturine.scanora.core.common.usecase.FormatScanDateUseCase
import com.soturine.scanora.core.ui.component.EmptyStateCard
import com.soturine.scanora.core.ui.component.PageThumbnailCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    state: HistoryUiState,
    onQueryChange: (String) -> Unit,
    onOpenScan: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dateFormatter = remember { FormatScanDateUseCase() }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.history_title)) },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    value = state.query,
                    onValueChange = onQueryChange,
                    label = { Text(text = stringResource(id = R.string.history_search_label)) },
                    placeholder = { Text(text = stringResource(id = R.string.history_search_placeholder)) },
                )
            }

            if (state.scans.isEmpty()) {
                item {
                    EmptyStateCard(
                        title = stringResource(id = R.string.history_empty_title),
                        message = stringResource(id = R.string.history_empty_message),
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            } else {
                items(state.scans, key = { it.id }) { scan ->
                    PageThumbnailCard(
                        title = scan.title,
                        subtitle = stringResource(
                            id = R.string.history_subtitle,
                            scan.pageCount,
                            dateFormatter(scan.updatedAt),
                        ),
                        imageUri = scan.coverPage?.displayUri,
                        onClick = { onOpenScan(scan.id) },
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanDetailScreen(
    scan: ScanDocument?,
    onToggleFavorite: () -> Unit,
    onDeleteScan: () -> Unit,
    onOpenReview: () -> Unit,
    onOpenExport: () -> Unit,
    onOpenOcr: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (scan == null) {
        EmptyStateCard(
            title = stringResource(id = R.string.history_detail_missing_title),
            message = stringResource(id = R.string.history_detail_missing_message),
            modifier = modifier.padding(24.dp),
        )
        return
    }

    val dateFormatter = remember { FormatScanDateUseCase() }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = scan.title) },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    text = stringResource(
                        id = R.string.history_detail_meta,
                        scan.pageCount,
                        dateFormatter(scan.updatedAt),
                    ),
                )
            }
            item {
                androidx.compose.material3.Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    onClick = onToggleFavorite,
                ) {
                    Text(
                        text = if (scan.isFavorite) {
                            stringResource(id = R.string.history_remove_favorite)
                        } else {
                            stringResource(id = R.string.history_add_favorite)
                        },
                    )
                }
            }
            item {
                androidx.compose.material3.Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    onClick = onOpenReview,
                ) {
                    Text(text = stringResource(id = R.string.history_open_review))
                }
            }
            item {
                androidx.compose.material3.Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    onClick = onOpenExport,
                ) {
                    Text(text = stringResource(id = R.string.history_open_export))
                }
            }
            items(scan.pages.sortedBy { it.index }, key = { it.id }) { page ->
                PageThumbnailCard(
                    title = stringResource(id = R.string.history_page_title, page.index + 1),
                    subtitle = page.filterType.title,
                    imageUri = page.displayUri,
                    onClick = { onOpenOcr(page.id) },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
            item {
                androidx.compose.material3.Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    onClick = onDeleteScan,
                ) {
                    Text(text = stringResource(id = R.string.history_delete_scan))
                }
            }
        }
    }
}
