package io.github.landrynorris.multifactor.components

import com.arkivanov.decompose.ComponentContext
import io.github.landrynorris.multifactor.repository.PasswordSettings
import io.github.landrynorris.multifactor.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KClass

interface SettingsLogic {
    val passwordSettings: Flow<List<Setting<*>>>
    fun setIncludeDigits(enable: Boolean)
    fun setIncludeSpecialChars(enable: Boolean)
    fun setExcludeSimilar(exclude: Boolean)
    fun setPasswordLength(length: Int)
}

class SettingsComponent(val context: ComponentContext,
                        private val settingsRepository: SettingsRepository):
    ComponentContext by context, SettingsLogic {
    private val includeDigitsSetting = Setting("Include Digits",
        "Whether to include digits 0-9 in auto-generated password",
        true, ::setIncludeDigits)
    private val includeSpecialSetting = Setting("Include Special",
        "Whether to include special characters", true,
        ::setIncludeSpecialChars)
    private val excludeSimilarSetting = Setting("Exclude Similar",
        "Whether to exclude characters that look alike, such as G6, Il1, etc.",
        true, ::setExcludeSimilar)
    private val passwordLengthSetting = Setting("Length",
        "Length of the password to generate", -1, ::setPasswordLength)

    override val passwordSettings = settingsRepository.passwordSettingsFlow.map {
        listOf(includeDigitsSetting.copy(value = it.includeDigits),
            includeSpecialSetting.copy(value = it.includeSpecial),
            excludeSimilarSetting.copy(value = it.excludeSimilar),
            passwordLengthSetting.copy(value = it.passwordLength))
    }



    override fun setIncludeDigits(enable: Boolean) = runBlocking {
        settingsRepository.setIncludeDigits(enable)
    }

    override fun setIncludeSpecialChars(enable: Boolean) = runBlocking {
        settingsRepository.setIncludeSpecialChars(enable)
    }

    override fun setExcludeSimilar(exclude: Boolean) = runBlocking {
        settingsRepository.setExcludeSimilar(exclude)
    }

    override fun setPasswordLength(length: Int) = runBlocking {
        settingsRepository.setPasswordLength(length)
    }
}

data class Setting<T>(val name: String, val description: String,
                      val value: T, val onValueChanged: (T) -> Unit)
