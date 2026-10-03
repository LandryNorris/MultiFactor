package io.github.landrynorris.app.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.landrynorris.app.theme.AppTheme

@Composable
fun MultiFactorTextButton(text: String, onClick: () -> Unit) {
    TextButton(onClick = onClick) { Text(text, color = MaterialTheme.colorScheme.onBackground) }
}

@Preview
@Composable
private fun MultiFactorTextButtonPreview() {
    AppTheme {
        Surface {
            MultiFactorTextButton(text = "Click Me", onClick = {})
        }
    }
}
