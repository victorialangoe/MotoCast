package com.example.motocast.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightThemeColors = lightColorScheme(
    primary = LightPrimary,
    secondary = LightSecondary,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = LightOnPrimary,
    onSecondary = LightOnSecondary,
    onBackground = LightOnBackground,
    onSurface = LightOnSurface,
    error = LightError,
    onError = LightOnError,
    tertiary = Blue200,
    onTertiary = Blue800,
)

private val DarkThemeColors = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = DarkOnPrimary,
    onSecondary = DarkOnSecondary,
    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface,
    error = DarkError,
    onError = DarkOnError,
    tertiary = Blue800,
    onTertiary = Blue200,
)

/**
 * The [AppTheme] composable is used to set the theme of the app.
 * It takes a [darkTheme] parameter, which is used to set the theme to dark or light.
 * Everything inside the [AppTheme] composable will be themed accordingly, with the
 * use of Material3 components.
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) {
            DarkThemeColors
        } else {
            LightThemeColors
        },
        typography = Typography,
    ) {
        content()
    }
}
