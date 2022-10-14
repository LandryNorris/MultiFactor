package io.github.landrynorris.multifactor.platform

import androidx.compose.runtime.Composable

@Composable
internal actual fun Dialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
    content()
}
