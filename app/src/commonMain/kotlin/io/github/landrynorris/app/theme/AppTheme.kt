package io.github.landrynorris.app.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
internal fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = colorScheme) { content() }
}
