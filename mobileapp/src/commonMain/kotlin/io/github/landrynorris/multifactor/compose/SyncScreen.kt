package io.github.landrynorris.multifactor.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.github.landrynorris.multifactor.components.SyncLogic

@Composable
internal fun SyncScreen(logic: SyncLogic) {
    val state by logic.state.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = logic::startServer) { Text("Start Server") }
        if(state.serverRunning) {
            Text("Server running. Enter code ${state.serverCode} on a client")
        } else {
            Text("No Server Running")
        }

        Button(onClick = { logic.connect() }) { Text("connect") }

        Text("Enter code to connect to server:")
        TextField(value = state.enteredCode, onValueChange = logic::enterCode, label = { Text("Code") })
    }
}
