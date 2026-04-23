package com.soturine.scanora.feature.ocr

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.soturine.scanora.core.ui.component.AsyncUriImage
import com.soturine.scanora.core.ui.component.EmptyStateCard
import com.soturine.scanora.core.ui.component.SectionHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OcrScreen(
    state: OcrUiState,
    onRecognizeAgain: () -> Unit,
    onBack: () -> Unit,
    onClearMessage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val paragraphs = remember(state.text) {
        state.text
            .split(Regex("\\n{2,}"))
            .map(String::trim)
            .filter(String::isNotBlank)
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
                title = { Text(text = stringResource(id = R.string.ocr_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.ocr_back),
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        if (state.page == null) {
            EmptyStateCard(
                title = stringResource(id = R.string.ocr_missing_title),
                message = stringResource(id = R.string.ocr_missing_message),
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
                                eyebrow = stringResource(id = R.string.ocr_eyebrow),
                                title = if (state.isLoading) {
                                    stringResource(id = R.string.ocr_processing)
                                } else {
                                    stringResource(id = R.string.ocr_result_title)
                                },
                                supportingText = stringResource(
                                    id = R.string.ocr_supporting,
                                    paragraphs.size,
                                ),
                            )
                            AsyncUriImage(
                                imageUri = state.previewImageUri ?: state.page.displayUri,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(216.dp),
                                maxDimension = 1700,
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                Button(
                                    modifier = Modifier.weight(1f),
                                    onClick = { clipboardManager.setText(AnnotatedString(state.text)) },
                                    enabled = state.text.isNotBlank(),
                                ) {
                                    Text(text = stringResource(id = R.string.ocr_copy))
                                }
                                FilledTonalButton(
                                    modifier = Modifier.weight(1f),
                                    onClick = onRecognizeAgain,
                                ) {
                                    Text(text = stringResource(id = R.string.ocr_retry))
                                }
                            }
                        }
                    }
                }
                if (paragraphs.isEmpty() && !state.isLoading) {
                    item {
                        EmptyStateCard(
                            title = stringResource(id = R.string.ocr_empty_title),
                            message = stringResource(id = R.string.ocr_empty_text),
                        )
                    }
                } else {
                    item {
                        SectionHeader(
                            eyebrow = stringResource(id = R.string.ocr_blocks_eyebrow),
                            title = stringResource(id = R.string.ocr_blocks_title),
                            supportingText = if (state.isLoading && paragraphs.isEmpty()) {
                                stringResource(id = R.string.ocr_processing_detail)
                            } else {
                                stringResource(id = R.string.ocr_blocks_supporting)
                            },
                        )
                    }
                    items(paragraphs.ifEmpty { listOf("") }) { paragraph ->
                        Card {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                if (paragraph.isBlank()) {
                                    Text(
                                        text = stringResource(id = R.string.ocr_processing_detail),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                } else {
                                    Text(
                                        text = paragraph,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
