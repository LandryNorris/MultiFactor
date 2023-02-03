package io.github.landrynorris.multifactor.repository

import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SettingsRepository(private val settings: FlowSettings) {
    object Keys {
        const val INCLUDE_DIGITS = "includeDigits"
        const val INCLUDE_SPECIAL = "includeSpecial"
        const val EXCLUDE_SIMILAR = "excludeSimilar"
        const val PASSWORD_LENGTH = "passwordLength"
    }

    private val includeDigitsFlow = settings.getBooleanFlow(Keys.INCLUDE_DIGITS, true)
    private val includeSpecialFlow = settings.getBooleanFlow(Keys.INCLUDE_SPECIAL, false)
    private val excludeSimilarFlow = settings.getBooleanFlow(Keys.EXCLUDE_SIMILAR, true)
    private val passwordLengthFlow = settings.getIntFlow(Keys.PASSWORD_LENGTH, 10)

    var currentPasswordSettings = runBlocking {
        PasswordSettings(
            includeDigits = includeDigitsFlow.first(),
            includeSpecial = includeSpecialFlow.first(),
            excludeSimilar = excludeSimilarFlow.first(),
            passwordLength = passwordLengthFlow.first()
        )
    }

    val passwordSettingsFlow = combine(includeDigitsFlow, includeSpecialFlow,
        excludeSimilarFlow, passwordLengthFlow) {
            includeDigits, includeSpecial, excludeSimilar, length ->
        val s = PasswordSettings(includeDigits, includeSpecial, excludeSimilar, length)
        currentPasswordSettings = s
        s
    }

    suspend fun setIncludeDigits(enable: Boolean) {
        settings.putBoolean(Keys.INCLUDE_DIGITS, enable)
    }

    suspend fun setIncludeSpecialChars(enable: Boolean) {
        settings.putBoolean(Keys.INCLUDE_SPECIAL, enable)
    }

    suspend fun setExcludeSimilar(exclude: Boolean) {
        settings.putBoolean(Keys.EXCLUDE_SIMILAR, exclude)
    }

    suspend fun setPasswordLength(length: Int) {
        settings.putInt(Keys.PASSWORD_LENGTH, length)
    }
}

data class PasswordSettings(val includeDigits: Boolean = true,
                            val includeSpecial: Boolean = true,
                            val excludeSimilar: Boolean = false,
                            val passwordLength: Int = 25)
