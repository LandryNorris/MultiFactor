package io.github.landrynorris.multifactor.compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import io.github.landrynorris.multifactor.components.AboutLogic

@Composable
fun About(logic: AboutLogic) {
    LazyColumn {
        item {
            val uriHandler = LocalUriHandler.current
            TextButton(onClick = { logic.openLegalPage(uriHandler) }) {
                Text("Privacy Policy")
            }
        }
    }
}
