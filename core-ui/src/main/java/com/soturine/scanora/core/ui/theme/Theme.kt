package com.soturine.scanora.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColorWhite = Color(0xFFFFFFFF)

private val LightColors = lightColorScheme(
    primary = Teal600,
    onPrimary = ColorWhite,
    primaryContainer = Teal200,
    onPrimaryContainer = Ink950,
    secondary = Ink600,
    onSecondary = ColorWhite,
    secondaryContainer = Sand200,
    onSecondaryContainer = Ink900,
    tertiary = AccentAmber,
    onTertiary = Ink950,
    tertiaryContainer = AccentAmberSoft,
    onTertiaryContainer = Ink950,
    background = Sand050,
    onBackground = Ink900,
    surface = ColorWhite,
    onSurface = Ink900,
    surfaceVariant = Sand200,
    onSurfaceVariant = Ink600,
    surfaceContainer = Color(0xFFF5F0E7),
    surfaceContainerHigh = Color(0xFFEEE7DB),
    surfaceContainerHighest = Color(0xFFE6DECF),
    surfaceBright = ColorWhite,
    surfaceDim = Sand100,
    outline = Stone400,
    outlineVariant = Stone200,
    error = ErrorRed,
    inverseSurface = Ink900,
    inverseOnSurface = Sand050,
)

private val DarkColors = darkColorScheme(
    primary = Teal300,
    onPrimary = Ink900,
    primaryContainer = Ink700,
    onPrimaryContainer = Teal200,
    secondary = Sand100,
    onSecondary = Ink950,
    secondaryContainer = Ink700,
    onSecondaryContainer = Sand100,
    tertiary = AccentAmber,
    onTertiary = Ink900,
    tertiaryContainer = Color(0xFF593A1A),
    onTertiaryContainer = Color(0xFFFFDFC0),
    background = Ink950,
    onBackground = Sand100,
    surface = Ink900,
    onSurface = Sand100,
    surfaceVariant = Ink800,
    onSurfaceVariant = Color(0xFFD0D8DA),
    surfaceContainer = Color(0xFF132126),
    surfaceContainerHigh = Color(0xFF193038),
    surfaceContainerHighest = Color(0xFF21404A),
    surfaceBright = Ink700,
    surfaceDim = Ink950,
    outline = Color(0xFF567780),
    outlineVariant = Ink700,
    error = Color(0xFFFFB4AB),
    inverseSurface = Sand050,
    inverseOnSurface = Ink950,
)

@Composable
fun ScanoraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = ScanoraTypography,
        shapes = ScanoraShapes,
        content = content,
    )
}
