package com.soturine.scanora.feature.export

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.soturine.scanora.core.common.model.ExportFormat
import com.soturine.scanora.core.common.model.ExportedFile
import com.soturine.scanora.core.common.model.PdfQuality
import com.soturine.scanora.core.ui.component.EmptyStateCard
import com.soturine.scanora.core.ui.component.OptionCard
import com.soturine.scanora.core.ui.component.SectionHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportScreen(
    state: ExportUiState,
    onSelectFormat: (ExportFormat) -> Unit,
    onSelectQuality: (PdfQuality) -> Unit,
    onExport: () -> Unit,
    onShare: (List<ExportedFile>) -> Unit,
    onOpenFile: (ExportedFile) -> Unit,
    onBack: () -> Unit,
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
                title = { Text(text = stringResource(id = R.string.export_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.export_back),
                        )
                    }
                },
            )
        },
        bottomBar = {
            if (state.scan != null) {
                Surface(shadowElevation = 8.dp) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        if (state.isExporting) {
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                            Text(
                                text = stringResource(id = R.string.export_processing_message),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onExport,
                            enabled = !state.isExporting,
                        ) {
                            Text(text = stringResource(id = R.string.export_action))
                        }
                        if (state.exportedFiles.isNotEmpty()) {
                            FilledTonalButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onShare(state.exportedFiles) },
                            ) {
                                Text(text = stringResource(id = R.string.export_share_action))
                            }
                        }
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        val scan = state.scan
        if (scan == null) {
            EmptyStateCard(
                title = stringResource(id = R.string.export_missing_title),
                message = stringResource(id = R.string.export_missing_message),
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
                                eyebrow = stringResource(id = R.string.export_eyebrow),
                                title = scan.title,
                                supportingText = stringResource(id = R.string.export_summary, scan.pageCount),
                            )
                            Text(
                                text = stringResource(id = R.string.export_local_notice),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
                item {
                    SectionHeader(
                        eyebrow = stringResource(id = R.string.export_format_eyebrow),
                        title = stringResource(id = R.string.export_format_section),
                        supportingText = stringResource(id = R.string.export_format_supporting),
                    )
                }
                items(ExportFormat.entries, key = { it.storageKey }) { format ->
                    OptionCard(
                        title = format.title,
                        subtitle = format.description(),
                        selected = state.selectedFormat == format,
                        onClick = { onSelectFormat(format) },
                    )
                }
                if (state.selectedFormat == ExportFormat.PDF) {
                    item {
                        SectionHeader(
                            eyebrow = stringResource(id = R.string.export_quality_eyebrow),
                            title = stringResource(id = R.string.export_quality_section),
                            supportingText = stringResource(id = R.string.export_quality_supporting),
                        )
                    }
                    items(PdfQuality.entries, key = { it.name }) { quality ->
                        OptionCard(
                            title = quality.title,
                            subtitle = quality.description(),
                            selected = state.selectedQuality == quality,
                            onClick = { onSelectQuality(quality) },
                        )
                    }
                }
                if (state.exportedFiles.isNotEmpty()) {
                    item {
                        Card {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp),
                            ) {
                                SectionHeader(
                                    eyebrow = stringResource(id = R.string.export_ready_eyebrow),
                                    title = stringResource(id = R.string.export_ready_title),
                                    supportingText = stringResource(
                                        id = R.string.export_success_message,
                                        state.exportedFiles.size,
                                    ),
                                )
                                state.exportedFiles.forEach { file ->
                                    ExportedFileCard(
                                        file = file,
                                        onOpen = { onOpenFile(file) },
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

@Composable
private fun ExportedFileCard(
    file: ExportedFile,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = file.displayName,
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Text(
                        text = file.mimeType.typeLabel(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = file.sizeLabel(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = stringResource(id = R.string.export_location_label, file.locationLabel),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            file.pathHint?.takeIf { it.isNotBlank() }?.let { pathHint ->
                Text(
                    text = pathHint,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onOpen,
            ) {
                Text(text = stringResource(id = R.string.export_open_file_action))
            }
        }
    }
}

private fun ExportFormat.description(): String = when (this) {
    ExportFormat.PDF -> "Melhor para enviar ou arquivar o lote inteiro em um único documento."
    ExportFormat.JPG -> "Salva cada página como imagem leve e fácil de compartilhar."
    ExportFormat.PNG -> "Mantém mais detalhe por página quando a saída precisa ficar em imagem."
}

private fun PdfQuality.description(): String = when (this) {
    PdfQuality.COMPACT -> "Arquivo menor para envio rápido."
    PdfQuality.BALANCED -> "Equilíbrio entre nitidez e tamanho."
    PdfQuality.HIGH -> "Mais definição para leitura fina e impressão."
}

private fun String.typeLabel(): String = when (this) {
    ExportFormat.PDF.mimeType -> "Documento PDF"
    ExportFormat.JPG.mimeType -> "Imagem JPG"
    ExportFormat.PNG.mimeType -> "Imagem PNG"
    else -> this
}

private fun ExportedFile.sizeLabel(): String {
    val sizeInKb = sizeBytes / 1024f
    return if (sizeInKb >= 1024f) {
        String.format("%.1f MB", sizeInKb / 1024f)
    } else {
        String.format("%.0f KB", sizeInKb.coerceAtLeast(1f))
    }
}
