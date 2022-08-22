package io.github.landrynorris.multifactor.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate

@Composable
fun AddButton(isAdding: Boolean, onClick: () -> Unit) {
    val degrees by animateFloatAsState(if(isAdding) 45f else 0f)
    IconButton(onClick = onClick, modifier = Modifier.rotate(degrees)) {
        Icon(Icons.Default.Add, if(isAdding) "Close" else "Add")
    }
}
