package io.github.landrynorris.app.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import io.github.landrynorris.app.theme.AppTheme

@Composable
internal fun CopyButton(isEnabled: Boolean = true, onClick: () -> Unit) {
    val color = MaterialTheme.colorScheme.onBackground
    TextButton(onClick, enabled = isEnabled) {
        Text(
            modifier = Modifier.semantics { contentDescription = "Copy" },
            text = "copy",
            color = if (isEnabled) color else color.copy(alpha = 0.38f),
        )
    }
}

@Preview
@Composable
private fun CopyButtonPreview() {
    AppTheme { Surface { CopyButton(isEnabled = true, onClick = {}) } }
}

@Preview
@Composable
private fun CopyButtonDisabledPreview() {
    AppTheme { Surface { CopyButton(isEnabled = false, onClick = {}) } }
}
