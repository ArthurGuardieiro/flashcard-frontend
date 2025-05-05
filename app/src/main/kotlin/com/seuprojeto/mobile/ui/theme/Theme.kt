package com.seuprojeto.mobile.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Esquema de cores para o tema claro
private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryVariant,
    onPrimaryContainer = OnPrimary,
    secondary = SecondaryColor,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryVariant,
    onSecondaryContainer = OnSecondary,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    error = Error,
    onError = OnPrimary
)

// Esquema de cores para o tema escuro
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryVariant,
    onPrimaryContainer = OnPrimary,
    secondary = SecondaryColor,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryVariant,
    onSecondaryContainer = OnSecondary,
    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE0E0E0),
    error = Error,
    onError = OnPrimary
)

/**
 * Tema principal do aplicativo de Flashcards.
 * 
 * @param darkTheme Se verdadeiro, aplica o tema escuro
 * @param content Conteúdo a ser exibido com o tema aplicado
 */
@Composable
fun FlashcardsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    // Aplicar a cor de status bar baseada no tema
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
} 