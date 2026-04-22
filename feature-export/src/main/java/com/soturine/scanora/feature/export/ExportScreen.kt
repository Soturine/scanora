package com.soturine.scanora.feature.export

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.soturine.scanora.core.common.model.ExportFormat
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
    onShare: (List<com.soturine.scanora.core.common.model.ExportedFile>) -> Unit,
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
            )
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
                        verticalArrangement = Arrangement.spacedBy(10.dp),
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
                if (state.isExporting) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    Text(
                        text = stringResource(id = R.string.export_processing_message),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                SectionHeader(
                    eyebrow = stringResource(id = R.string.export_format_eyebrow),
                    title = stringResource(id = R.string.export_format_section),
                )
                ExportFormat.entries.forEach { format ->
                    OptionCard(
                        title = format.title,
                        subtitle = format.description(),
                        selected = state.selectedFormat == format,
                        onClick = { onSelectFormat(format) },
                    )
                }

                SectionHeader(
                    eyebrow = stringResource(id = R.string.export_quality_eyebrow),
                    title = stringResource(id = R.string.export_quality_section),
                )
                PdfQuality.entries.forEach { quality ->
                    OptionCard(
                        title = quality.title,
                        subtitle = quality.description(),
                        selected = state.selectedQuality == quality,
                        onClick = { onSelectQuality(quality) },
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
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Text(
                                text = stringResource(
                                    id = R.string.export_success_message,
                                    state.exportedFiles.size,
                                ),
                                style = MaterialTheme.typography.titleMedium,
                            )
                            OutlinedButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onShare(state.exportedFiles) },
                            ) {
                                Text(text = stringResource(id = R.string.export_share_action))
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun ExportFormat.description(): String = when (this) {
    ExportFormat.PDF -> "Ideal para compartilhar um lote inteiro como documento."
    ExportFormat.JPG -> "Melhor para páginas individuais com tamanho reduzido."
    ExportFormat.PNG -> "Mantém mais detalhes em páginas individuais."
}

private fun PdfQuality.description(): String = when (this) {
    PdfQuality.COMPACT -> "Arquivos menores para envio rápido."
    PdfQuality.BALANCED -> "Equilíbrio entre qualidade e tamanho."
    PdfQuality.HIGH -> "Mais definição para impressão e arquivo."
}
