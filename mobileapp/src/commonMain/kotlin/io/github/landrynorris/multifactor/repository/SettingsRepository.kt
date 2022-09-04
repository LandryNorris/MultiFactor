package io.github.landrynorris.multifactor.repository

import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.flow.combine

class SettingsRepository(private val settings: FlowSettings) {
    private val includeDigitsFlow = settings.getBooleanFlow("includeDigits")
    private val includeSpecialFlow = settings.getBooleanFlow("includeSpecial")
    private val excludeSimilarFlow = settings.getBooleanFlow("excludeSimilar")
    private val passwordLengthFlow = settings.getIntFlow("passwordLength")

    var currentPasswordSettings = PasswordSettings()

    val passwordSettingsFlow = combine(includeDigitsFlow, includeSpecialFlow,
        excludeSimilarFlow, passwordLengthFlow) {
            includeDigits, includeSpecial, excludeSimilar, length ->
        val s = PasswordSettings(includeDigits, includeSpecial, excludeSimilar, length)
        currentPasswordSettings = s
        s
    }

    suspend fun setIncludeDigits(enable: Boolean) {
        settings.putBoolean("includeDigits", enable)
    }

    suspend fun setIncludeSpecialChars(enable: Boolean) {
        settings.putBoolean("includeSpecial", enable)
    }

    suspend fun setExcludeSimilar(exclude: Boolean) {
        settings.putBoolean("excludeSimilar", exclude)
    }

    suspend fun setPasswordLength(length: Int) {
        settings.putInt("passwordLength", length)
    }
}

data class PasswordSettings(val includeDigits: Boolean = true,
                            val includeSpecial: Boolean = true,
                            val excludeSimilar: Boolean = false,
                            val passwordLength: Int = 25)
