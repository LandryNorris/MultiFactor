package io.github.landrynorris.multifactor.compose

import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
internal fun MultiFactorCard(contentDescription: String = "card", content: @Composable () -> Unit) {
    Card(modifier = Modifier.testTag(contentDescription)) {
        content()
    }
}
