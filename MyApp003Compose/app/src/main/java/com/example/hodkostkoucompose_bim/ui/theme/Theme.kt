package com.example.hodkostkoucompose_bim.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Barvy kostky: běžný hod a zvýrazněná šestka
val DiceNormal = Color(0xFF37474F)
val DiceNormalDark = Color(0xFFCFD8DC)
val DiceSix = Color(0xFFF9A825)

private val LightColors = lightColorScheme()
private val DarkColors = darkColorScheme()

// Jednoduché Material 3 téma aplikace (světlé / tmavé podle systému)
@Composable
fun HodKostkouComposeBimTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
