package io.github.landrynorris.multifactor.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

@Composable
internal fun CopyButton(isEnabled: Boolean = true, onClick: () -> Unit) {
    val color = MaterialTheme.colorScheme.onBackground
    TextButton(onClick, enabled = isEnabled) { Text(modifier = Modifier.semantics { contentDescription = "Copy" },
        text = "copy",
        color = if(isEnabled) color else color.copy(alpha = 0.38f) ) }
}
