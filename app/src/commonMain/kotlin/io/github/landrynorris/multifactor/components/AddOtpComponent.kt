package io.github.landrynorris.multifactor.components

import com.arkivanov.decompose.ComponentContext
import io.github.landrynorris.multifactor.models.OtpModel
import io.github.landrynorris.otp.Hotp
import io.github.landrynorris.otp.OtpMethod
import io.github.landrynorris.otp.Totp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

interface CreateOtpLogic {
    val state: StateFlow<CreateOtpState>
    fun nameChanged(name: String)
    fun secretChanged(secret: String)
    fun methodChanged(method: OtpMethod)
    fun confirm()
}

class CreateOtpComponent(context: ComponentContext,
                      private val otpCreated: (OtpModel) -> Unit):
    ComponentContext by context, CreateOtpLogic {
    override val state = MutableStateFlow(CreateOtpState())

    override fun nameChanged(name: String) {
        state.update { it.copy(name = name) }
    }

    override fun secretChanged(secret: String) {
        state.update { it.copy(secret = secret) }
    }

    override fun methodChanged(method: OtpMethod) {
        state.update { it.copy(type = method) }
    }

    override fun confirm() {
        val currentState = state.value
        otpCreated(OtpModel(-1L, otp = if(currentState.type == OtpMethod.HOTP) {
            Hotp(secret = currentState.secret, name = currentState.name, counter = 0L)
        } else {
            Totp(secret = currentState.secret, name = currentState.name)
        }))
        state.update { it.copy(name = "", secret = "") }
    }
}

data class CreateOtpState(val name: String = "",
                          val secret: String = "",
                          val type: OtpMethod = OtpMethod.HOTP)
