package io.github.landrynorris.app.compose

import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import io.github.landrynorris.app.theme.AppTheme

@Composable
internal fun MultiFactorCard(contentDescription: String = "card", content: @Composable () -> Unit) {
    Card(modifier = Modifier.testTag(contentDescription)) { content() }
}

@Preview
@Composable
private fun MultiFactorCardPreview() {
    AppTheme { Surface { MultiFactorCard { Text("Card Content") } } }
}
