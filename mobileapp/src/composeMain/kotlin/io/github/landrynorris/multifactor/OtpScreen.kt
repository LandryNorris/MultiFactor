package io.github.landrynorris.multifactor

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import io.github.landrynorris.multifactor.components.OtpLogic
import io.github.landrynorris.multifactor.components.OtpState
import io.github.landrynorris.otp.OtpMethod
import androidx.compose.runtime.getValue
import io.github.landrynorris.multifactor.components.CreateOtpState

@Composable
fun OtpScreen(logic: OtpLogic) {
    val state by logic.state.collectAsState(logic.state.value)
    val createOtpLogic = logic.createOtpLogic
    val createState by createOtpLogic.state.collectAsState(createOtpLogic.state.value)
    Scaffold(
        floatingActionButton = {
            IconButton(onClick = logic::addOtpPressed) {
                Icon(Icons.Default.Add, "")
            }
        }) {
        OtpList(state.otpList,
            createOtpState = if(state.showCreate) createState else null,
            onIncrementClicked = logic::incrementClicked,
            onNameChanged = createOtpLogic::nameChanged,
            onSecretChanged = createOtpLogic::secretChanged,
            onTypeChanged = createOtpLogic::methodChanged,
            onConfirmClicked = createOtpLogic::confirm)
    }
}

@Composable
fun OtpList(otpStates: List<OtpState>,
            createOtpState: CreateOtpState? = null,
            onIncrementClicked: (Int) -> Unit = {},
            onNameChanged: (String) -> Unit = {},
            onSecretChanged: (String) -> Unit = {},
            onTypeChanged: (OtpMethod) -> Unit = {},
            onConfirmClicked: () -> Unit = {}) {
    LazyColumn {
        itemsIndexed(otpStates) { index, otp ->
            if(otp.type is OtpMethod.HOTP) HotpItem(index,
                otp.pin, otp.name,
                onIncrementClicked = onIncrementClicked)
            else TotpItem(otp.pin, otp.name, otp.value)
        }

        item {
            if(createOtpState != null) {
                CreateOtpItem(createOtpState, onNameChanged,
                    onSecretChanged, onTypeChanged,
                    onConfirmClicked)
            }
        }
    }
}

@Preview
@Composable
fun OtpListPreview() {
    OtpList(
        listOf(
            OtpState(null, OtpMethod.HOTP, "my hotp pin", "123 456", 0f),
            OtpState(null, OtpMethod.TOTP, "my totp pin", "628 234", 0.7f),
            OtpState(null, OtpMethod.TOTP, "my totp pin 2", "712 938", 0.5f),
            OtpState(null, OtpMethod.TOTP, "Wonka Bar app", "192 844", 0.2f),
            OtpState(null, OtpMethod.TOTP, "Stark Industries Login", "019 233", 0.0f),
            OtpState(null, OtpMethod.TOTP, "Weyland Corp login", "102 834", 1.0f),
        )
    )
}
