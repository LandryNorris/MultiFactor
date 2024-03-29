package io.github.landrynorris.multifactor.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.landrynorris.multifactor.components.CreateOtpState
import io.github.landrynorris.multifactor.theme.colorScheme
import io.github.landrynorris.otp.OtpMethod

val names = listOf("Hotp", "Totp")
val types = listOf(OtpMethod.HOTP, OtpMethod.TOTP)

@Composable
internal fun CreateOtpItem(createOtpState: CreateOtpState,
                  onNameChanged: (String) -> Unit = {},
                  onSecretChanged: (String) -> Unit = {},
                  onTypeChanged: (OtpMethod) -> Unit = {},
                  onConfirmClicked: () -> Unit = {}) {
    Column(modifier = Modifier.fillMaxWidth()
        .background(colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(modifier = Modifier.contentDescription("NameField"),
            label = { Text("Name") },
            value = createOtpState.name, onValueChange = onNameChanged)
        TextField(modifier = Modifier.contentDescription("SecretField"),
            label = { Text("Secret") },
            value = createOtpState.secret, onValueChange = onSecretChanged)
        Spacer(modifier = Modifier.height(8.dp))
        MultiToggleSwitch(createOtpState.type.index(), names, onToggleChanged = { index ->
            onTypeChanged(types[index])
        })

        TextButton(onClick = onConfirmClicked) {
            Text(modifier = Modifier.contentDescription("Confirm"),
                text = "Confirm", color = colorScheme.onBackground)
        }
    }
}

fun OtpMethod.index(): Int = when(this) {
    is OtpMethod.HOTP -> 0
    is OtpMethod.TOTP -> 1
}
