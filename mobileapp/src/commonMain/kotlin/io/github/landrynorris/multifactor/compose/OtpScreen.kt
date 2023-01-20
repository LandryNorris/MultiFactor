package io.github.landrynorris.multifactor.compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import io.github.landrynorris.multifactor.components.OtpLogic
import io.github.landrynorris.multifactor.components.OtpState
import io.github.landrynorris.otp.OtpMethod
import androidx.compose.runtime.getValue
import io.github.landrynorris.multifactor.components.CreateOtpState

@Composable
internal fun OtpScreen(logic: OtpLogic) {
    val state by logic.state.collectAsState(logic.state.value)
    val createOtpLogic = logic.createOtpLogic
    val createState by createOtpLogic.state.collectAsState(createOtpLogic.state.value)
    Scaffold(
        floatingActionButton = {
            AddButton(state.isAdding, logic::addOtpPressed)
        }) {
        OtpList(state.otpList,
            createOtpState = if(state.isAdding) createState else null,
            onIncrementClicked = logic::incrementClicked,
            onNameChanged = createOtpLogic::nameChanged,
            onSecretChanged = createOtpLogic::secretChanged,
            onTypeChanged = createOtpLogic::methodChanged,
            onConfirmClicked = createOtpLogic::confirm)
    }
}

@Composable
internal fun OtpList(otpStates: List<OtpState>,
            createOtpState: CreateOtpState? = null,
            onIncrementClicked: (Int) -> Unit = {},
            onNameChanged: (String) -> Unit = {},
            onSecretChanged: (String) -> Unit = {},
            onTypeChanged: (OtpMethod) -> Unit = {},
            onConfirmClicked: () -> Unit = {}) {
    LazyColumn {
        itemsIndexed(otpStates) { index, otp ->
            MultiFactorCard {
                if(otp.type is OtpMethod.HOTP)
                    HotpItem(index, otp.pin, otp.name, onIncrementClicked = onIncrementClicked)
                else
                    TotpItem(otp.pin, otp.name, otp.value)
            }
        }

        item {
            if(createOtpState != null) {
                MultiFactorCard {
                    CreateOtpItem(createOtpState, onNameChanged,
                        onSecretChanged, onTypeChanged,
                        onConfirmClicked)
                }
            }
        }
    }
}
