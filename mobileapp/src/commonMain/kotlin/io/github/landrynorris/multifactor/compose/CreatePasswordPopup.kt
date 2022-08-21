package io.github.landrynorris.multifactor.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.landrynorris.multifactor.components.CreatePasswordLogic

@Composable
fun CreatePasswordPopup(logic: CreatePasswordLogic) {
    val state by logic.state.collectAsState()
    Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(state.name, onValueChange = logic::nameChanged,
            label = { Text("Name") })
        TextField(state.password, onValueChange = logic::passwordChanged,
            label = { Text("Password") })
        TextButton(onClick = logic::confirm) { Text("Confirm") }
    }
}
