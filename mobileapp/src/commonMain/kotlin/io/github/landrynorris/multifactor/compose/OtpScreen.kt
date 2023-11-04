package io.github.landrynorris.multifactor.compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import io.github.landrynorris.multifactor.components.OtpLogic
import io.github.landrynorris.multifactor.components.OtpState
import io.github.landrynorris.otp.OtpMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
internal fun OtpScreen(logic: OtpLogic) {
    val state by logic.state.collectAsState(logic.state.value)
    val createOtpLogic = logic.createOtpLogic
    Scaffold(
        floatingActionButton = {
            AddButton(state.isAdding, logic::addOtpPressed)
        }) {
        if(state.isAdding) {
            CreateOtpDialog(createOtpLogic, onDismiss = logic::dismissAddOtp)
        }
        LaunchedEffect(Unit) {
            withContext(Dispatchers.Default) {
                while(true) {
                    withFrameMillis {
                        logic.updateTotps()
                    }
                }
            }
        }
        OtpList(state.otpList,
            onIncrementClicked = logic::incrementClicked,
            onDelete = logic::deleteItem,
            onCopyClicked = logic::copyClicked)
    }
}

@Composable
internal fun OtpList(otpStates: List<OtpState>,
                     onIncrementClicked: (Int) -> Unit = {},
                     onDelete: (Int) -> Unit,
                     onCopyClicked: (ClipboardManager, OtpState) -> Unit) {
    LazyColumn {
        itemsIndexed(otpStates, key = { _, item -> item.model.id }) { index, otp ->
            SwipeToDelete(onDelete = { onDelete(index) }) {
                MultiFactorCard(contentDescription = otp.name) {
                    val clipboardManager = LocalClipboardManager.current
                    if(otp.type is OtpMethod.HOTP)
                        HotpItem(index, otp.pin, otp.name, onIncrementClicked = onIncrementClicked,
                            onCopyClicked = { onCopyClicked(clipboardManager, otp) })
                    else
                        TotpItem(otp.pin, otp.name, otp.value,
                            onCopyClicked = { onCopyClicked(clipboardManager, otp) })
                }
            }
        }
    }
}
