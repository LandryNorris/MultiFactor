package io.github.landrynorris.multifactor.components

import com.arkivanov.decompose.ComponentContext
import io.github.landrynorris.encryption.Crypto
import io.github.landrynorris.multifactor.models.PasswordModel
import io.github.landrynorris.multifactor.repository.PasswordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface PasswordListLogic {
    val state: StateFlow<PasswordListState>

    fun showHidePressed(field: PasswordField)
}

class PasswordListComponent(context: ComponentContext,
                            private val crypto: Crypto,
                            repository: PasswordRepository): ComponentContext by context,
    PasswordListLogic {
    private val modelsFlow = repository.getPasswordsFlow()
    override val state = MutableStateFlow(PasswordListState())

    init {
        CoroutineScope(Dispatchers.Default).launch {
            modelsFlow.collect { models ->
                state.update {
                    //the existing passwords to keep
                    val filteredPasswords = it.passwords.filter { field ->
                        models.any { model -> model == field.model }
                    }

                    //the new passwords to add
                    val newModels = models.filter { model ->
                        filteredPasswords.none { field -> model == field.model }
                    }

                    val passwords = filteredPasswords +
                            newModels.map { model -> model.toNewField() }

                    it.copy(passwords = passwords)
                }
            }
        }
    }

    override fun showHidePressed(field: PasswordField) {
        state.update {
            it.copy(passwords = it.passwords.map { value ->
                if(value == field) {
                    if(field.isHidden) {
                        val decryptedPassword = decryptPassword(field.model)
                        field.copy(isHidden = false, password = decryptedPassword)
                    } else {
                        field.copy(isHidden = true, password = null)
                    }
                } else value
            })
        }
    }

    private fun decryptPassword(model: PasswordModel): String {
        val decrypted = crypto.decrypt(model.encryptedValue, model.salt)
        return decrypted.decodeToString()
    }

    private fun PasswordModel.toNewField() = PasswordField(this, null, true)
}

data class PasswordField(val model: PasswordModel, val password: String?,
                         val isHidden: Boolean)

data class PasswordListState(val passwords: List<PasswordField> = listOf())
