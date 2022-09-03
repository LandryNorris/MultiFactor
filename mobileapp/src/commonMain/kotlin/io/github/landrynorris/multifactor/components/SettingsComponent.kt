package io.github.landrynorris.multifactor.components

import com.arkivanov.decompose.ComponentContext
import io.github.landrynorris.multifactor.repository.PasswordSettings
import io.github.landrynorris.multifactor.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

interface SettingsLogic {
    val passwordSettings: Flow<PasswordSettings>
    fun setIncludeDigits(enable: Boolean)
    fun setIncludeSpecialChars(enable: Boolean)
    fun setExcludeSimilar(exclude: Boolean)
    fun setPasswordLength(length: Int)
}

class SettingsComponent(val context: ComponentContext,
                        private val settingsRepository: SettingsRepository):
    ComponentContext by context, SettingsLogic {

    override val passwordSettings = settingsRepository.passwordSettingsFlow

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