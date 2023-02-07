package io.github.landrynorris.multifactor.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

@Composable
internal fun CopyButton(isEnabled: Boolean = true, onClick: () -> Unit) {
    TextButton(onClick) { Text(modifier = Modifier.semantics { contentDescription = "Copy" },
        text = "copy",
        color = if(isEnabled) MaterialTheme.colors.onBackground else Color.DarkGray ) }
}
