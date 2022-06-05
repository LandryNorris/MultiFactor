package io.github.landrynorris.multifactor.components

import com.arkivanov.decompose.ComponentContext
import io.github.landrynorris.multifactor.models.PasswordModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface CreatePasswordLogic {
    val state: Flow<CreatePasswordState>

    fun nameChanged(name: String) {}
    fun passwordChanged(password: String) {}
    fun confirm() {}
}

class CreatePasswordComponent(context: ComponentContext,
                              private val onPasswordCreated: (PasswordModel) -> Unit):
    CreatePasswordLogic, ComponentContext by context {
    override val state = MutableStateFlow(CreatePasswordState())

    override fun nameChanged(name: String) {
        state.update { it.copy(name = name) }
    }

    override fun passwordChanged(password: String) {
        state.update { it.copy(password = password) }
    }

    override fun confirm() {
        val current = state.value
        onPasswordCreated(PasswordModel(-1L, current.name, byteArrayOf(), byteArrayOf()))
    }
}

data class CreatePasswordState(val name: String = "", val password: String = "")
