package io.github.landrynorris.multifactor.repository

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking

class SettingsRepository(private val settings: Settings) {
    object Keys {
        const val INCLUDE_DIGITS = "includeDigits"
        const val INCLUDE_SPECIAL = "includeSpecial"
        const val EXCLUDE_SIMILAR = "excludeSimilar"
        const val PASSWORD_LENGTH = "passwordLength"
    }

    val currentPasswordSettings get() = passwordSettingsFlow.value

    val passwordSettingsFlow = runBlocking {
        MutableStateFlow(PasswordSettings(
            includeDigits = settings.getBoolean(Keys.INCLUDE_DIGITS, true),
            includeSpecial = settings.getBoolean(Keys.INCLUDE_SPECIAL, false),
            excludeSimilar = settings.getBoolean(Keys.EXCLUDE_SIMILAR, true),
            passwordLength = settings.getInt(Keys.PASSWORD_LENGTH, 10)
        ))
    }

    fun setIncludeDigits(enable: Boolean) {
        settings.putBoolean(Keys.INCLUDE_DIGITS, enable)
        passwordSettingsFlow.update {
            it.copy(includeDigits = enable)
        }
    }

    fun setIncludeSpecialChars(enable: Boolean) {
        settings.putBoolean(Keys.INCLUDE_SPECIAL, enable)
        passwordSettingsFlow.update {
            it.copy(includeSpecial = enable)
        }
    }

    fun setExcludeSimilar(exclude: Boolean) {
        settings.putBoolean(Keys.EXCLUDE_SIMILAR, exclude)
        passwordSettingsFlow.update {
            it.copy(excludeSimilar = exclude)
        }
    }

    fun setPasswordLength(length: Int) {
        settings.putInt(Keys.PASSWORD_LENGTH, length)
        passwordSettingsFlow.update {
            it.copy(passwordLength = length)
        }
    }
}

data class PasswordSettings(val includeDigits: Boolean = true,
                            val includeSpecial: Boolean = true,
                            val excludeSimilar: Boolean = false,
                            val passwordLength: Int = 25)
