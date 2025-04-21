package com.example.syncednotepad.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

val CustomPrimary = Color(0xFF76ABAE)
val CustomOnPrimary = Color(0xFFEEEEEE)
val CustomBackground = Color(0xFF222831)
val CustomSurface = Color(0xFF444A52)
val CustomOnSurface = Color(0xFFEEEEEE)


private val CustomDarkColorScheme = darkColorScheme(
    primary = CustomPrimary,
    onPrimary = CustomOnPrimary,
    background = CustomBackground,
    onBackground = CustomOnSurface,
    surface = CustomSurface,
    onSurface = CustomOnSurface
)

private val CustomLightColorScheme = lightColorScheme(
    primary = CustomPrimary,
    onPrimary = CustomOnPrimary,
    background = CustomBackground,
    onBackground = CustomOnSurface,
    surface = CustomSurface,
    onSurface = CustomOnSurface
)


@Composable
fun SyncedNotePadTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> CustomDarkColorScheme
        else -> CustomLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}