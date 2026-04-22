package com.soturine.scanora.feature.ocr

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
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
    onClearMessage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current
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
                title = { Text(text = stringResource(id = R.string.ocr_title)) },
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
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
                        state.page.filterType.title,
                    ),
                )
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    ),
                ) {
                    AsyncUriImage(
                        imageUri = state.page.displayUri,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(196.dp),
                    )
                }
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                ) {
                    SelectionContainer {
                        Text(
                            text = state.text.ifBlank { stringResource(id = R.string.ocr_empty_text) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { clipboardManager.setText(AnnotatedString(state.text)) },
                    enabled = state.text.isNotBlank(),
                ) {
                    Text(text = stringResource(id = R.string.ocr_copy))
                }
                FilledTonalButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onRecognizeAgain,
                ) {
                    Text(text = stringResource(id = R.string.ocr_retry))
                }
            }
        }
    }
}
