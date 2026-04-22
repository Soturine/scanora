package com.soturine.scanora.feature.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.soturine.scanora.core.common.model.DocumentFilterType
import com.soturine.scanora.core.common.model.DocumentQuad
import com.soturine.scanora.core.common.model.PointValue
import com.soturine.scanora.core.ui.component.AsyncUriImage
import com.soturine.scanora.core.ui.component.EmptyStateCard
import com.soturine.scanora.core.ui.component.OptionCard
import com.soturine.scanora.core.ui.component.SectionHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropScreen(
    state: EditorUiState,
    onSaveQuad: (DocumentQuad) -> Unit,
    onContinue: () -> Unit,
    onClearMessage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val page = state.currentPage
    val snackbarHostState = remember { SnackbarHostState() }
    var localQuad by remember(page?.id, page?.quad) {
        mutableStateOf(page?.quad ?: defaultQuad())
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            onClearMessage()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.editor_crop_title)) },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        if (page == null) {
            EmptyStateCard(
                title = stringResource(id = R.string.editor_missing_page_title),
                message = stringResource(id = R.string.editor_missing_page_message),
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(24.dp),
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                SectionHeader(
                    eyebrow = stringResource(id = R.string.editor_crop_eyebrow),
                    title = stringResource(id = R.string.editor_crop_heading),
                    supportingText = stringResource(id = R.string.editor_crop_helper),
                )
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    ),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(430.dp),
                    ) {
                        AsyncUriImage(
                            imageUri = page.displayUri,
                            modifier = Modifier.fillMaxSize(),
                        )
                        QuadEditorOverlay(
                            quad = localQuad,
                            onQuadChange = { localQuad = it },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onSaveQuad(localQuad)
                        onContinue()
                    },
                ) {
                    Text(text = stringResource(id = R.string.editor_continue_to_filters))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    state: EditorUiState,
    onApplyFilter: (DocumentFilterType) -> Unit,
    onRotate: () -> Unit,
    onOpenReview: () -> Unit,
    onClearMessage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            onClearMessage()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.editor_filter_title)) },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        val page = state.currentPage
        if (page == null) {
            EmptyStateCard(
                title = stringResource(id = R.string.editor_missing_page_title),
                message = stringResource(id = R.string.editor_missing_page_message),
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(24.dp),
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                SectionHeader(
                    eyebrow = stringResource(id = R.string.editor_filter_eyebrow),
                    title = stringResource(id = R.string.editor_filter_heading),
                    supportingText = stringResource(id = R.string.editor_filter_helper),
                )
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    ),
                ) {
                    AsyncUriImage(
                        imageUri = page.displayUri,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(330.dp),
                    )
                }
                DocumentFilterType.entries.forEach { filter ->
                    OptionCard(
                        title = filter.title,
                        subtitle = filter.description(),
                        selected = page.filterType == filter,
                        onClick = { onApplyFilter(filter) },
                    )
                }
                FilledTonalButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onRotate,
                    enabled = !state.isProcessing,
                ) {
                    Text(text = stringResource(id = R.string.editor_rotate))
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onOpenReview,
                ) {
                    Text(text = stringResource(id = R.string.editor_continue_to_review))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    state: EditorUiState,
    onRename: (String) -> Unit,
    onUpdateTags: (String) -> Unit,
    onClearMessage: () -> Unit,
    onSelectPage: (String) -> Unit,
    onMovePageUp: (String) -> Unit,
    onMovePageDown: (String) -> Unit,
    onDeleteCurrentPage: () -> Unit,
    onOpenCrop: () -> Unit,
    onOpenFilters: () -> Unit,
    onOpenExport: () -> Unit,
    onOpenOcr: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scan = state.scan
    if (scan == null) {
        EmptyStateCard(
            title = stringResource(id = R.string.editor_missing_page_title),
            message = stringResource(id = R.string.editor_missing_page_message),
            modifier = modifier.padding(24.dp),
        )
        return
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val orderedPages = remember(scan.pages) { scan.pages.sortedBy { it.index } }
    val persistedTags = remember(scan.tags) { scan.tags.joinToString(", ") }
    var title by rememberSaveable(scan.id, scan.title) { mutableStateOf(scan.title) }
    var tags by rememberSaveable(scan.id, persistedTags) { mutableStateOf(persistedTags) }
    val normalizedTagDraft = remember(tags) {
        tags
            .split(",")
            .map(String::trim)
            .filter(String::isNotBlank)
            .distinct()
    }
    val hasTitleChanges = title.trim() != scan.title
    val hasTagChanges = normalizedTagDraft != scan.tags

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            onClearMessage()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.editor_review_title)) },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    SectionHeader(
                        eyebrow = stringResource(id = R.string.editor_review_eyebrow),
                        title = title,
                        supportingText = stringResource(
                            id = R.string.editor_review_summary,
                            orderedPages.size,
                        ),
                    )
                    Text(
                        text = stringResource(id = R.string.editor_review_helper),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = title,
                        onValueChange = { title = it },
                        label = { Text(text = stringResource(id = R.string.editor_document_name)) },
                        singleLine = true,
                    )
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onRename(title) },
                        enabled = hasTitleChanges,
                    ) {
                        Text(text = stringResource(id = R.string.editor_save_name))
                    }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = tags,
                        onValueChange = { tags = it },
                        label = { Text(text = stringResource(id = R.string.editor_tags)) },
                        supportingText = { Text(text = stringResource(id = R.string.editor_tags_helper)) },
                    )
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onUpdateTags(tags) },
                        enabled = hasTagChanges,
                    ) {
                        Text(text = stringResource(id = R.string.editor_save_tags))
                    }
                }
            }
            SectionHeader(
                eyebrow = stringResource(id = R.string.editor_pages_eyebrow),
                title = stringResource(id = R.string.editor_pages_section),
                supportingText = stringResource(id = R.string.editor_pages_supporting),
            )
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                itemsIndexed(
                    items = orderedPages,
                    key = { _, page -> page.id },
                ) { index, page ->
                    PageActionsCard(
                        title = stringResource(id = R.string.editor_page_title, page.index + 1),
                        subtitle = page.filterType.title,
                        imageUri = page.displayUri,
                        selected = state.currentPage?.id == page.id,
                        onSelect = { onSelectPage(page.id) },
                        onMoveUp = { onMovePageUp(page.id) },
                        onMoveDown = { onMovePageDown(page.id) },
                        onOpenOcr = { onOpenOcr(page.id) },
                        canMoveUp = index > 0,
                        canMoveDown = index < orderedPages.lastIndex,
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                FilledTonalButton(
                    modifier = Modifier.weight(1f),
                    onClick = onOpenCrop,
                    enabled = state.currentPage != null,
                ) {
                    Text(text = stringResource(id = R.string.editor_open_crop))
                }
                FilledTonalButton(
                    modifier = Modifier.weight(1f),
                    onClick = onOpenFilters,
                    enabled = state.currentPage != null,
                ) {
                    Text(text = stringResource(id = R.string.editor_open_filters))
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onDeleteCurrentPage,
                    enabled = state.currentPage != null,
                ) {
                    Text(text = stringResource(id = R.string.editor_delete_page))
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onOpenExport,
                ) {
                    Text(text = stringResource(id = R.string.editor_open_export))
                }
            }
        }
    }
}

@Composable
private fun PageActionsCard(
    title: String,
    subtitle: String,
    imageUri: String,
    selected: Boolean,
    onSelect: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onOpenOcr: () -> Unit,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.surfaceContainerHigh
            } else {
                MaterialTheme.colorScheme.surface
            },
        ),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            AsyncUriImage(
                imageUri = imageUri,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(164.dp),
            )
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = if (selected) {
                    stringResource(id = R.string.editor_page_subtitle_selected, subtitle)
                } else {
                    subtitle
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onSelect,
                    enabled = !selected,
                ) {
                    Text(
                        text = if (selected) {
                            stringResource(id = R.string.editor_page_selected)
                        } else {
                            stringResource(id = R.string.editor_select_page)
                        },
                    )
                }
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onOpenOcr,
                ) {
                    Text(text = stringResource(id = R.string.editor_open_ocr))
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onMoveUp,
                    enabled = canMoveUp,
                ) {
                    Text(text = stringResource(id = R.string.editor_move_up))
                }
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onMoveDown,
                    enabled = canMoveDown,
                ) {
                    Text(text = stringResource(id = R.string.editor_move_down))
                }
            }
        }
    }
}

private fun defaultQuad(): DocumentQuad =
    DocumentQuad(
        topLeft = PointValue(0.08f, 0.08f),
        topRight = PointValue(0.92f, 0.08f),
        bottomRight = PointValue(0.92f, 0.92f),
        bottomLeft = PointValue(0.08f, 0.92f),
    )

private fun DocumentFilterType.description(): String = when (this) {
    DocumentFilterType.ORIGINAL_CORRECTED -> "Corrige perspectiva e mantém cores naturais do documento."
    DocumentFilterType.DOCUMENT_BLACK_WHITE -> "Prioriza contraste alto para impressão e texto."
    DocumentFilterType.DOCUMENT_GRAY -> "Reduz ruído visual mantendo leitura confortável."
    DocumentFilterType.COLOR_ENHANCED -> "Realça cores sem deixar a página artificial."
    DocumentFilterType.RECEIPT_HIGH_CONTRAST -> "Melhora recibos térmicos e documentos com pouco contraste."
}
