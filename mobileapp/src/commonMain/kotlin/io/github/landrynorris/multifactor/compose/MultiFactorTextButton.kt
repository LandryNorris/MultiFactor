package io.github.landrynorris.multifactor.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun MultiFactorTextButton(text: String, onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(text, color = MaterialTheme.colors.onBackground)
    }
}
