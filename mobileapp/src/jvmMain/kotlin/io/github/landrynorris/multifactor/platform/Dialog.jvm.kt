package io.github.landrynorris.multifactor.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Popup

@Composable
internal actual fun Dialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
    androidx.compose.ui.window.Dialog(onCloseRequest = onDismissRequest) {
        content()
    }
}
