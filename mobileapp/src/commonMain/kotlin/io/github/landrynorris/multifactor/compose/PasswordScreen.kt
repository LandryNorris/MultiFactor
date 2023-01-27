package io.github.landrynorris.multifactor.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EnhancedEncryption
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import io.github.landrynorris.multifactor.components.PasswordLogic
import io.github.landrynorris.multifactor.platform.Dialog

@Composable
internal fun PasswordScreen(logic: PasswordLogic) {
    val state by logic.state.collectAsState()
    val passwordListState by logic.passwordListLogic.state.collectAsState()

    Scaffold(floatingActionButton = {
        AddButton(state.showAddPassword) {
            logic.toggleAddPassword()
        }
    }) {
        if(state.showAddPassword) {
            Dialog(onDismissRequest = logic::hideAddPassword) {
                CreatePasswordPopup(logic.createPasswordLogic)
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(passwordListState.passwords, key = { it.model.id }) { field ->
                SwipeToDelete(onDelete = { logic.deletePassword(field.model) }) {
                    MultiFactorCard {
                        val clipboardManager = LocalClipboardManager.current
                        PasswordCard(field.model.name, field.password,
                            onCopyClicked = { logic.copyPasswordClicked(clipboardManager,
                                field.password ?: "") }) {
                            logic.passwordListLogic.showHidePressed(field)
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun PasswordCard(name: String, password: String?,
                          onCopyClicked: () -> Unit,
                          onToggleVisibleClick: () -> Unit = {}) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(name)

            if(password == null) HiddenPasswordText()
            else Text(password)
        }
        Spacer(modifier = Modifier.weight(1f))
        CopyButton(isEnabled = password != null, onClick = onCopyClicked)
        IconButton(onClick = onToggleVisibleClick) {
            Icon(
                if(password == null) Icons.Default.EnhancedEncryption else Icons.Default.LockOpen,
                if(password == null) "show" else "hide")
        }
    }
}

@Composable
internal fun HiddenPasswordText() {
    Text("************")
}
