package com.seunome.scanora.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Teal500,
    onPrimary = Sand100,
    secondary = Ink500,
    onSecondary = Sand100,
    tertiary = AccentAmber,
    onTertiary = Ink900,
    background = Sand100,
    onBackground = Ink900,
    surface = ColorWhite,
    onSurface = Ink900,
    surfaceVariant = Stone200,
    onSurfaceVariant = Ink700,
    outline = Stone400,
    error = ErrorRed,
)

private val DarkColors = darkColorScheme(
    primary = Teal300,
    onPrimary = Ink900,
    secondary = Sand100,
    onSecondary = Ink900,
    tertiary = AccentAmber,
    onTertiary = Ink900,
    background = Ink900,
    onBackground = Sand100,
    surface = Ink700,
    onSurface = Sand100,
    surfaceVariant = Ink500,
    onSurfaceVariant = Sand100,
    outline = Teal300,
    error = Color(0xFFFFB4AB),
)

private val ColorWhite = Color(0xFFFFFFFF)

@Composable
fun ScanoraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = ScanoraTypography,
        content = content,
    )
}
