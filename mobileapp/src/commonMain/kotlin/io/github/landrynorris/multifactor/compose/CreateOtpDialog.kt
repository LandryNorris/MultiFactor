package io.github.landrynorris.multifactor.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Dialog
import io.github.landrynorris.multifactor.components.CreateOtpLogic

@Composable
internal fun CreateOtpDialog(logic: CreateOtpLogic, onDismiss: () -> Unit) {
    val state by logic.state.collectAsState()
    Dialog(onDismissRequest = onDismiss) {
        CreateOtpItem(state, onNameChanged = logic::nameChanged,
            onSecretChanged = logic::secretChanged,
            onTypeChanged = logic::methodChanged,
            onConfirmClicked = logic::confirm)
    }
}
