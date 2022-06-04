package io.github.landrynorris.multifactor

import android.graphics.fonts.FontFamily
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = Theme.colors,
        content = content
    )
}

object Theme {
    val colors: Colors
        @Composable
        get() = if(isSystemInDarkTheme()) darkColors()
        else lightColors()
}