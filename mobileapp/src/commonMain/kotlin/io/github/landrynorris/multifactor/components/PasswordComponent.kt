package io.github.landrynorris.multifactor.components

import com.arkivanov.decompose.ComponentContext
import io.github.landrynorris.multifactor.models.PasswordModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface PasswordLogic {
    val state: Flow<PasswordState>

    fun addPasswordClicked() {}
    fun showPassword(index: Int) {}

}

class PasswordComponent(private val context: ComponentContext): PasswordLogic, ComponentContext by context {
    override val state = MutableStateFlow(PasswordState())

    override fun addPasswordClicked() {
        super.addPasswordClicked()
    }
}

data class Password(val name: String, val password: String?, val shouldShow: Boolean)

data class PasswordState(val passwordModels: List<Password> = listOf(),
                         val showAddPassword: Boolean = false)
