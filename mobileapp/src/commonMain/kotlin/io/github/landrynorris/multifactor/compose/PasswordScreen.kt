package io.github.landrynorris.multifactor.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.github.landrynorris.multifactor.components.PasswordLogic

@Composable
fun PasswordScreen(logic: PasswordLogic) {
    val state by logic.state.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {

    }
}
