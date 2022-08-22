package io.github.landrynorris.multifactor.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import io.github.landrynorris.multifactor.theme.colors

@Composable
fun HotpItem(index: Int, pin: String, name: String,
             onIncrementClicked: (Int) -> Unit = {}) {
    Column(modifier = Modifier.fillMaxWidth()
        .background(colors.background)) {
        Text(name, fontSize = 18.sp)
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {

            Text(pin, modifier = Modifier.weight(1f),
                fontSize = 20.sp, letterSpacing = 1.sp)
            IconButton(onClick = { onIncrementClicked(index) }) {
                Icon(Icons.Default.Add,
                    "Increment Counter")
            }
        }
    }
}
