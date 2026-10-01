package io.github.landrynorris.app.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import io.github.landrynorris.app.export.PasswordExporter
import io.github.landrynorris.app.repository.PasswordRepository
import io.github.landrynorris.app.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

interface SettingsLogic {
    val passwordSettings: Flow<List<Setting<*>>>

    val passwordExportLogic: PasswordExportLogic

    fun setIncludeDigits(enable: Boolean)

    fun setIncludeSpecialChars(enable: Boolean)

    fun setExcludeSimilar(exclude: Boolean)

    fun setPasswordLength(length: Int)

    fun navigateToAbout()
    fun onPasswordExportPressed()
}

class SettingsComponent(
    val context: ComponentContext,
    passwordExporter: PasswordExporter,
    private val settingsRepository: SettingsRepository,
    private val openAbout: () -> Unit,
) : ComponentContext by context, SettingsLogic {
    private val includeDigitsSetting =
        Setting(
            "Include Digits",
            "Whether to include digits 0-9 in auto-generated password",
            settingsRepository.currentPasswordSettings.includeDigits,
            ::setIncludeDigits,
        )
    private val includeSpecialSetting =
        Setting(
            "Include Special",
            "Whether to include special characters",
            settingsRepository.currentPasswordSettings.includeSpecial,
            ::setIncludeSpecialChars,
        )
    private val excludeSimilarSetting =
        Setting(
            "Exclude Similar",
            "Whether to exclude characters that look alike, such as G6, Il1, etc.",
            settingsRepository.currentPasswordSettings.excludeSimilar,
            ::setExcludeSimilar,
        )
    private val passwordLengthSetting =
        Setting(
            "Length",
            "Length of the password to generate",
            settingsRepository.currentPasswordSettings.passwordLength,
            ::setPasswordLength,
        )

    override val passwordSettings =
        settingsRepository.passwordSettingsFlow.map {
            listOf(
                includeDigitsSetting.copy(value = it.includeDigits),
                includeSpecialSetting.copy(value = it.includeSpecial),
                excludeSimilarSetting.copy(value = it.excludeSimilar),
                passwordLengthSetting.copy(value = it.passwordLength),
            )
        }

    override val passwordExportLogic =
        PasswordExportComponent(childContext("PasswordExport"), passwordExporter)

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

    override fun navigateToAbout() = openAbout()

    override fun onPasswordExportPressed() {
        passwordExportLogic.show()
    }
}

data class Setting<T>(
    val name: String,
    val description: String,
    val value: T,
    val onValueChanged: (T) -> Unit,
)
