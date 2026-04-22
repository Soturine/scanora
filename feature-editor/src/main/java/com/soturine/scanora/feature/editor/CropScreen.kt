package com.soturine.scanora.feature.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.soturine.scanora.core.common.model.DocumentFilterType
import com.soturine.scanora.core.common.model.DocumentQuad
import com.soturine.scanora.core.common.model.PointValue
import com.soturine.scanora.core.ui.component.AsyncUriImage
import com.soturine.scanora.core.ui.component.EmptyStateCard
import com.soturine.scanora.core.ui.component.OptionCard
import com.soturine.scanora.core.ui.component.PageThumbnailCard
import com.soturine.scanora.core.ui.component.SectionHeader
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropScreen(
    state: EditorUiState,
    onSaveQuad: (DocumentQuad) -> Unit,
    onContinue: () -> Unit,
    onEnsureQuad: () -> Unit,
    onBack: () -> Unit,
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

    LaunchedEffect(page?.id, page?.quad) {
        if (page != null && page.quad == null) {
            onEnsureQuad()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.editor_crop_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.editor_back),
                        )
                    }
                },
            )
        },
        bottomBar = {
            if (page != null) {
                Surface(shadowElevation = 8.dp) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        if (state.isProcessing) {
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        }
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                onSaveQuad(localQuad)
                                onContinue()
                            },
                            enabled = !state.isProcessing,
                        ) {
                            Text(text = stringResource(id = R.string.editor_save_crop_continue))
                        }
                    }
                }
            }
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
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                SectionHeader(
                    eyebrow = stringResource(id = R.string.editor_crop_eyebrow),
                    title = stringResource(id = R.string.editor_crop_heading),
                    supportingText = stringResource(id = R.string.editor_crop_helper),
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    ),
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        DocumentCropPreview(
                            imageUri = page.sourceUri,
                            quad = localQuad,
                            onQuadChange = { localQuad = it },
                            modifier = Modifier.fillMaxSize(),
                        )
                        if (state.isProcessing) {
                            PreviewProgressOverlay(
                                text = stringResource(id = R.string.editor_crop_detecting),
                            )
                        }
                    }
                }
                Text(
                    text = stringResource(id = R.string.editor_crop_caption),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    state: EditorUiState,
    onApplyFilter: (DocumentFilterType) -> Unit,
    onRequestPreview: (DocumentFilterType, Int) -> Unit,
    onRotate: () -> Unit,
    onOpenReview: () -> Unit,
    onBack: () -> Unit,
    onClearMessage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val page = state.currentPage
    var selectedFilter by remember(page?.id, page?.filterType) {
        mutableStateOf(page?.filterType ?: DocumentFilterType.ORIGINAL_CORRECTED)
    }
    var previewLongSide by remember(page?.id) { mutableIntStateOf(1500) }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            onClearMessage()
        }
    }

    LaunchedEffect(page?.id, selectedFilter, previewLongSide) {
        if (page != null) {
            onRequestPreview(selectedFilter, previewLongSide)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.editor_filter_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.editor_back),
                        )
                    }
                },
            )
        },
        bottomBar = {
            if (page != null) {
                val canContinue = selectedFilter == page.filterType && page.processedUri != null
                Surface(shadowElevation = 8.dp) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        if (state.isProcessing) {
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            FilledTonalButton(
                                modifier = Modifier.weight(1f),
                                onClick = onRotate,
                                enabled = !state.isProcessing,
                            ) {
                                Text(text = stringResource(id = R.string.editor_rotate))
                            }
                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    if (canContinue) {
                                        onOpenReview()
                                    } else {
                                        onApplyFilter(selectedFilter)
                                    }
                                },
                                enabled = !state.isProcessing,
                            ) {
                                Text(
                                    text = if (canContinue) {
                                        stringResource(id = R.string.editor_save_continue)
                                    } else {
                                        stringResource(id = R.string.editor_apply_visual)
                                    },
                                )
                            }
                        }
                    }
                }
            }
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                item {
                    SectionHeader(
                        eyebrow = stringResource(id = R.string.editor_filter_eyebrow),
                        title = stringResource(id = R.string.editor_filter_heading),
                        supportingText = stringResource(id = R.string.editor_filter_helper),
                    )
                }
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        ),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(372.dp)
                                    .onSizeChanged { size ->
                                        previewLongSide = max(size.width, size.height).coerceIn(1400, 1800)
                                    },
                            ) {
                                AsyncUriImage(
                                    imageUri = state.previewImageUri ?: page.displayUri,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit,
                                    maxDimension = previewLongSide,
                                )
                                if (state.isPreviewLoading || state.isProcessing) {
                                    PreviewProgressOverlay(
                                        text = if (state.isProcessing) {
                                            stringResource(id = R.string.editor_filter_applying)
                                        } else {
                                            stringResource(id = R.string.editor_filter_preview_loading)
                                        },
                                    )
                                }
                            }
                            if (state.isPreviewRefining && !state.isProcessing) {
                                Text(
                                    text = stringResource(id = R.string.editor_filter_preview_refining),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
                itemsIndexed(DocumentFilterType.entries, key = { _, filter -> filter.storageKey }) { _, filter ->
                    OptionCard(
                        title = filter.title,
                        subtitle = filter.description(),
                        selected = selectedFilter == filter,
                        badge = if (page.filterType == filter) {
                            stringResource(id = R.string.editor_filter_current)
                        } else {
                            null
                        },
                        enabled = !state.isProcessing,
                        onClick = { selectedFilter = filter },
                    )
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
    onBack: () -> Unit,
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
    val hasMetadataChanges = title.trim() != scan.title || normalizedTagDraft != scan.tags

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
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.editor_back),
                        )
                    }
                },
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
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
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
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
            }
            item {
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
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = tags,
                            onValueChange = { tags = it },
                            label = { Text(text = stringResource(id = R.string.editor_tags)) },
                            supportingText = {
                                Text(text = stringResource(id = R.string.editor_tags_helper))
                            },
                        )
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                onRename(title)
                                onUpdateTags(tags)
                            },
                            enabled = hasMetadataChanges,
                        ) {
                            Text(text = stringResource(id = R.string.editor_save_metadata))
                        }
                    }
                }
            }
            item {
                SectionHeader(
                    eyebrow = stringResource(id = R.string.editor_pages_eyebrow),
                    title = stringResource(id = R.string.editor_pages_section),
                    supportingText = stringResource(id = R.string.editor_pages_supporting),
                )
            }
            itemsIndexed(
                items = orderedPages,
                key = { _, page -> page.id },
            ) { index, page ->
                val selected = state.currentPage?.id == page.id
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    PageThumbnailCard(
                        title = stringResource(id = R.string.editor_page_title, page.index + 1),
                        subtitle = if (selected) {
                            stringResource(id = R.string.editor_page_subtitle_selected, page.filterType.title)
                        } else {
                            page.filterType.title
                        },
                        imageUri = page.displayUri,
                        overline = stringResource(id = R.string.editor_page_overline, page.index + 1),
                        badge = if (selected) {
                            stringResource(id = R.string.editor_page_selected_badge)
                        } else {
                            null
                        },
                        selected = selected,
                        onClick = { onSelectPage(page.id) },
                    )
                    if (selected) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            FilledTonalButton(
                                modifier = Modifier.weight(1f),
                                onClick = { onOpenOcr(page.id) },
                            ) {
                                Text(text = stringResource(id = R.string.editor_open_ocr))
                            }
                            OutlinedButton(
                                modifier = Modifier.weight(1f),
                                onClick = { onMovePageUp(page.id) },
                                enabled = index > 0,
                            ) {
                                Text(text = stringResource(id = R.string.editor_move_up))
                            }
                        }
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onMovePageDown(page.id) },
                            enabled = index < orderedPages.lastIndex,
                        ) {
                            Text(text = stringResource(id = R.string.editor_move_down))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DocumentCropPreview(
    imageUri: String,
    quad: DocumentQuad,
    onQuadChange: (DocumentQuad) -> Unit,
    modifier: Modifier = Modifier,
) {
    var imageSize by remember(imageUri) { mutableStateOf(IntSize.Zero) }

    BoxWithConstraints(modifier = modifier) {
        val imageBounds = remember(
            constraints.maxWidth,
            constraints.maxHeight,
            imageSize,
        ) {
            computeFittedBounds(
                containerWidth = constraints.maxWidth.toFloat(),
                containerHeight = constraints.maxHeight.toFloat(),
                imageSize = imageSize,
            )
        }

        AsyncUriImage(
            imageUri = imageUri,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
            maxDimension = 1600,
            onBitmapLoaded = { imageSize = it },
        )

        imageBounds?.let { bounds ->
            QuadEditorOverlay(
                quad = quad,
                imageBounds = bounds,
                onQuadChange = onQuadChange,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun PreviewProgressOverlay(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
            shape = MaterialTheme.shapes.large,
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.5.dp)
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

private fun computeFittedBounds(
    containerWidth: Float,
    containerHeight: Float,
    imageSize: IntSize,
): Rect? {
    if (containerWidth <= 0f || containerHeight <= 0f) return null
    if (imageSize == IntSize.Zero || imageSize.width == 0 || imageSize.height == 0) return null

    val imageAspect = imageSize.width.toFloat() / imageSize.height.toFloat()
    val containerAspect = containerWidth / containerHeight

    return if (imageAspect > containerAspect) {
        val fittedHeight = containerWidth / imageAspect
        val top = (containerHeight - fittedHeight) / 2f
        Rect(
            left = 0f,
            top = top,
            right = containerWidth,
            bottom = top + fittedHeight,
        )
    } else {
        val fittedWidth = containerHeight * imageAspect
        val left = (containerWidth - fittedWidth) / 2f
        Rect(
            left = left,
            top = 0f,
            right = left + fittedWidth,
            bottom = containerHeight,
        )
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
    DocumentFilterType.ORIGINAL_CORRECTED -> "Mantém as cores do papel com correção de perspectiva e limpeza suave."
    DocumentFilterType.DOCUMENT_BLACK_WHITE -> "Texto mais firme para impressão, estudo e arquivos simples."
    DocumentFilterType.DOCUMENT_GRAY -> "Mantém leitura confortável sem lavar o miolo da página."
    DocumentFilterType.COLOR_ENHANCED -> "Recupera contraste e cor sem exagerar no documento."
    DocumentFilterType.RECEIPT_HIGH_CONTRAST -> "Focado em recibos, notas térmicas e papéis mais apagados."
}
