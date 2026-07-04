package io.github.landrynorris.multifactor.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import io.github.landrynorris.multifactor.models.OtpModel
import io.github.landrynorris.multifactor.repository.OtpRepository
import io.github.landrynorris.otp.Hotp
import io.github.landrynorris.otp.OtpMethod
import io.github.landrynorris.otp.Totp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.buildAnnotatedString

interface OtpLogic {
    val state: StateFlow<OtpScreenState>
    val createOtpLogic: CreateOtpLogic

    fun updateTotps()
    fun incrementClicked(index: Int)
    fun deleteItem(index: Int)
    fun copyClicked(clipboardManager: ClipboardManager?, otp: OtpState)
    fun addOtpPressed()
    fun dismissAddOtp()
}

class OtpComponent(private val context: ComponentContext,
                   private val repository: OtpRepository): ComponentContext by context, OtpLogic {
    override val state = MutableStateFlow(OtpScreenState())
    override val createOtpLogic = CreateOtpComponent(childContext("create")) {
        addOtp(it)
    }

    init {
        CoroutineScope(Dispatchers.Default).launch {
            repository.getOtpModelFlow().collect { models ->
                state.update {
                    it.copy(otpList = models.map { model ->
                        model.toState()
                    })
                }
            }
        }
    }

    override fun updateTotps() {
        state.update {
            it.copy(otpList = it.otpList.map { state ->
                if(state.model.otp is Totp) {
                    val totp = state.model.otp
                    state.copy(pin = totp.generatePin(), value = totp.progress)
                } else { //Leave not-time based codes alone.
                    state
                }
            })
        }
    }

    override fun incrementClicked(index: Int) {
        val item = state.value.otpList[index]
        if(item.model.otp !is Hotp) return
        val hotp = item.model.otp
        repository.setHotpCount(item.model.id, hotp.counter + 1)
    }

    override fun deleteItem(index: Int) {
        val item = state.value.otpList[index]
        repository.delete(item.model)
    }

    override fun addOtpPressed() {
        state.update { it.copy(isAdding = !it.isAdding) }
    }

    override fun dismissAddOtp() {
        state.update { it.copy(isAdding = false) }
    }

    private fun addOtp(model: OtpModel) {
        repository.createOtp(model)
    }

    override fun copyClicked(clipboardManager: ClipboardManager?, otp: OtpState) {
        val annotatedString = buildAnnotatedString { append(otp.pin) }
        clipboardManager?.setText(annotatedString)
    }
}

data class OtpScreenState(val otpList: List<OtpState> = listOf(), val isAdding: Boolean = false)

data class OtpState(val model: OtpModel, val type: OtpMethod, val name: String, val pin: String, val value: Float)

fun OtpModel.toState(): OtpState {
    return OtpState(model = this,
        type = if(otp is Hotp) OtpMethod.HOTP else OtpMethod.TOTP,
        name = otp.name,
        pin = otp.generatePin(),
        value = when (otp) {
            is Hotp -> otp.counter.toFloat()
            is Totp -> otp.progress
        }
    )
}
