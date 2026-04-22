package com.soturine.scanora.core.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.soturine.scanora.core.common.model.ScanMode

@Composable
fun ModeCard(
    mode: ScanMode,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OptionCard(
        modifier = modifier,
        title = mode.title,
        subtitle = mode.subtitle,
        selected = selected,
        onClick = onClick,
        badge = if (selected) "Em uso" else null,
    )
}

