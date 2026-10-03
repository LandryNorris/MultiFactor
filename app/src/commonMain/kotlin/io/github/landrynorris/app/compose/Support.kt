package io.github.landrynorris.app.compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.tooling.preview.Preview
import io.github.landrynorris.app.components.AboutLogic
import io.github.landrynorris.app.theme.AppTheme

@Composable
fun About(logic: AboutLogic) {
    LazyColumn {
        item {
            val uriHandler = LocalUriHandler.current
            MultiFactorTextButton("Privacy Policy", onClick = { logic.openLegalPage(uriHandler) })
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

@Preview
@Composable
private fun AboutPreview() {
    AppTheme {
        Surface {
            About(
                logic =
                    object : AboutLogic {
                        override val state =
                            io.github.landrynorris.app.components.AboutState(
                                appVersion = "1.0.0",
                                buildId = "12345",
                            )

                        override fun openLegalPage(handler: UriHandler) {}
                    }
            )
        }
    }
}
