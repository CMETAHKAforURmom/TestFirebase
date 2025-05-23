package ru.test.andernam.view.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    background = Color(0xFF3E3E3E),
    onBackground = Color.White,
    primary = Color(0xFFB0B0B0),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF9E9E9E),
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFF757575),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF6E6E6E),
    onSecondaryContainer = Color.Black,
    tertiary = Color(0xFF9E9E9E),
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFFAEAEAE),
    onTertiaryContainer = Color.Black,
    surface = Color(0xFF4E4E4E),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF5E5E5E),
    onSurfaceVariant = Color.White,
    inverseSurface = Color(0xFFE0E0E0),
    inverseOnSurface = Color.Black,
    outline = Color(0xFF757575),
    outlineVariant = Color(0xFF5E5E5E),
    scrim = Color(0x99000000),
    error = Color(0xFFCF6679),
    onError = Color.Black,
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PurpleGrey80,
    secondary = LightDark,
    tertiary = Pink80

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun TestFirebaseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}