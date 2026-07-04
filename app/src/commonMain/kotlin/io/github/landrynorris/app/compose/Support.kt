package io.github.landrynorris.app.compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import io.github.landrynorris.app.components.AboutLogic

@Composable
fun About(logic: AboutLogic) {
    LazyColumn {
        item {
            val uriHandler = LocalUriHandler.current
            MultiFactorTextButton("Privacy Policy",
                onClick = { logic.openLegalPage(uriHandler) })
        }

        item {
            val version = logic.state.appVersion
            Text("App Version: $version")
        }

        item {
            val buildId = logic.state.buildId
            Text("Build Id: $buildId")
        }
    }
}
