package io.github.landrynorris.multifactor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import io.github.landrynorris.multifactor.components.OtpState
import io.github.landrynorris.otp.Hotp
import io.github.landrynorris.otp.OtpMethod

@Composable
fun HotpItem(pin: String, name: String,
             onIncrementClicked: () -> Unit = {}) {
    Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
        Text(name, fontSize = 18.sp)
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {

            Text(pin, modifier = Modifier.weight(1f),
                fontSize = 20.sp, letterSpacing = 1.sp)
            IconButton(onClick = onIncrementClicked) {
                Icon(Icons.Default.Add,
                    "Increment Counter")
            }
        }
    }
}

@Preview
@Composable
fun HotpPreview() {
    HotpItem("123 456", "My Pin")
}
