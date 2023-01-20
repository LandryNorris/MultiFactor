package io.github.landrynorris.multifactor.compose

import androidx.compose.material.Card
import androidx.compose.runtime.Composable

@Composable
fun MultiFactorCard(content: @Composable () -> Unit) {
    Card {
        content()
    }
}
