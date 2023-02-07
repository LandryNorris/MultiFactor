package io.github.landrynorris.multifactor.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import io.github.landrynorris.multifactor.components.CreatePasswordLogic
import io.github.landrynorris.multifactor.theme.colors

@Composable
internal fun CreatePasswordPopup(logic: CreatePasswordLogic) {
    val state by logic.state.collectAsState()
    val clipboard = LocalClipboardManager.current
    Column(modifier = Modifier.fillMaxWidth().background(colors.background),
        horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(modifier = Modifier.fillMaxWidth().contentDescription("NameField"),
            value = state.name,
            onValueChange = logic::nameChanged, label = { Text("Name") })
        TextField(modifier = Modifier.fillMaxWidth().contentDescription("DomainField"),
            value = state.domain,
            onValueChange = logic::domainChanged, label = { Text("Domain") })

        Row {
            TextField(modifier = Modifier.contentDescription("PasswordField"),
                value = state.password, onValueChange = logic::passwordChanged,
                label = { Text("Password") })
            IconButton(onClick = { logic.generateNewPassword(clipboard) }) {
                Icon(Icons.Default.Add, "generate new password")
            }
        }
        TextButton(modifier = Modifier.contentDescription("Confirm"),
            onClick = logic::confirm,
            enabled = state.isConfirmEnabled) { Text("Confirm", color = colors.onBackground) }
    }
}
