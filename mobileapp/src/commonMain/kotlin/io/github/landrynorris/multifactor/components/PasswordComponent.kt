package io.github.landrynorris.multifactor.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import io.github.landrynorris.multifactor.repository.PasswordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

interface PasswordLogic {
    val state: StateFlow<PasswordState>
    val passwordListLogic: PasswordListLogic
    val createPasswordLogic: CreatePasswordLogic

    fun toggleAddPassword() {}
    fun hideAddPassword() {}
    fun showPassword(index: Int) {}
}

class PasswordComponent(
    private val context: ComponentContext,
    passwordRepository: PasswordRepository): PasswordLogic, ComponentContext by context {
    override val state = MutableStateFlow(PasswordState())
    override val passwordListLogic = PasswordListComponent(childContext("PasswordListLogic"),
        passwordRepository)
    override val createPasswordLogic =
        CreatePasswordComponent(childContext("CreatePasswordLogic"), passwordRepository)

    override fun hideAddPassword() {
        state.update { it.copy(showAddPassword = false) }
    }

    override fun toggleAddPassword() {
        state.update { it.copy(showAddPassword = !it.showAddPassword) }
    }
}

data class PasswordState(val showAddPassword: Boolean = false)
