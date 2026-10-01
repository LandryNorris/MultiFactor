package io.github.landrynorris.app.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import io.github.landrynorris.app.components.PasswordExportData
import io.github.landrynorris.app.components.PasswordExportLogic
import io.github.landrynorris.app.theme.colorScheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun PasswordExportPopup(component: PasswordExportLogic) {
    val state by component.state.collectAsState()

    if (state.isShowing) {
        PasswordExportPopupContent(component, state)
    }
}

@Composable
private fun PasswordExportPopupContent(
    component: PasswordExportLogic,
    state: PasswordExportData
) {
    Dialog(
        onDismissRequest = component::dismiss,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().background(colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(
                modifier = Modifier.contentDescription("PasswordField"),
                label = { Text("Export Password") },
                value = state.encryptionPassword,
                onValueChange = component::setExportPassword,
            )

            TextButton(onClick = {
                CoroutineScope(Dispatchers.Default).launch {
                    component.export()
                }
            }) {
                Text("Export")
            }
        }
    }
}
