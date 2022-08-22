package io.github.landrynorris.multifactor.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog as AndroidDialog

@Composable
actual fun Dialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
    AndroidDialog(onDismissRequest = onDismissRequest, content = content)
}
