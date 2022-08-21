package io.github.landrynorris.multifactor.compose

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable

@Composable
fun AddButton(isAdding: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(if(isAdding) Icons.Default.Close else Icons.Default.Add,
            if(isAdding) "close" else "add")
    }
}
