package io.github.landrynorris.multifactor.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
internal fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(colors = colors) {
        content()
    }
}
