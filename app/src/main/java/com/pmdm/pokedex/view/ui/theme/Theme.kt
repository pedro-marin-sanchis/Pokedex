package com.pmdm.pokedex.view.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    primaryContainer = Purple80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    primaryContainer = Purple40,
    background = BackgroundColor
)

@Composable
fun getColorScheme(): ColorScheme {
    return if(isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
}

@Composable
fun setStatusBarColor(color: Color = getColorScheme().primaryContainer) {
    val view = LocalView.current
    SideEffect {
        val window = (view.context as Activity).window
        WindowCompat.getInsetsController(window, view)
        window.statusBarColor = color.toArgb()
    }
}

@Composable
fun PokedexTheme(
    content: @Composable () -> Unit
) {
    setStatusBarColor()
    MaterialTheme(
        colorScheme = getColorScheme(),
        typography = Typography,
        content = content
    )
}