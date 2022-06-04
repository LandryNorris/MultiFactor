package io.github.landrynorris.multifactor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import io.github.landrynorris.multifactor.components.CreateOtpState
import io.github.landrynorris.otp.OtpMethod

val names = listOf("Hotp", "Totp")
val types = listOf(OtpMethod.HOTP, OtpMethod.TOTP)

@Composable
fun CreateOtpItem(createOtpState: CreateOtpState,
                  onNameChanged: (String) -> Unit = {},
                  onSecretChanged: (String) -> Unit = {},
                  onTypeChanged: (OtpMethod) -> Unit = {},
                  onConfirmClicked: () -> Unit = {}) {
    Row(modifier = Modifier.fillMaxWidth().background(Color.White),
        verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1.0f)) {
            TextField(label = { Text("Name") },
                value = createOtpState.name, onValueChange = onNameChanged)
            TextField(label = { Text("Secret") },
                value = createOtpState.secret, onValueChange = onSecretChanged)
        }

        Column(modifier = Modifier.height(IntrinsicSize.Max),
            verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally) {
            MultiToggleSwitch(createOtpState.type.index(), names, onToggleChanged = { index ->
                onTypeChanged(types[index])
            })

            Button(onClick = onConfirmClicked) {
                Text("Confirm")
            }
        }
    }
}

fun OtpMethod.index(): Int = when(this) {
    is OtpMethod.HOTP -> 0
    is OtpMethod.TOTP -> 1
}

@Preview
@Composable
fun CreateOtpPreview() {
    CreateOtpItem(CreateOtpState(name = "name", secret = "abcdefg"))
}
