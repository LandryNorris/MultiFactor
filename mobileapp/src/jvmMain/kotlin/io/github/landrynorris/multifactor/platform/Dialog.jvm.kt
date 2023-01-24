package io.github.landrynorris.multifactor.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Popup

@Composable
internal actual fun Dialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
    Popup {
        content()
    }
}
