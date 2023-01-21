package io.github.landrynorris.multifactor.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
internal fun CopyButton(isEnabled: Boolean = true, onClick: () -> Unit) {
    TextButton(onClick) { Text("copy",
        color = if(isEnabled) MaterialTheme.colors.onBackground else Color.DarkGray ) }
}
