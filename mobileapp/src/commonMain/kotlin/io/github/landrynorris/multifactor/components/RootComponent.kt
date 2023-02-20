package io.github.landrynorris.multifactor.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import io.github.landrynorris.encryption.Crypto
import io.github.landrynorris.multifactor.repository.OtpRepository
import io.github.landrynorris.multifactor.repository.PasswordRepository
import io.github.landrynorris.multifactor.repository.SettingsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface Root {
    val routerState: Value<ChildStack<*, Child>>

    fun navigateToOtp()
    fun navigateToPasswordManager()
    fun navigateToSettings()
    fun navigateToAbout()
    fun navigateToLegal()

    sealed class Child {
        abstract val component: Any
        class Otp(override val component: OtpLogic): Child()
        class PasswordManager(override val component: PasswordLogic): Child()
        class Settings(override val component: SettingsLogic): Child()
        class About(override val component: AboutComponent): Child()
    }
}

class RootComponent(context: ComponentContext,
                    private val crypto: Crypto): ComponentContext by context, Root, KoinComponent {
    private val navigation = StackNavigation<Config>()
    private val otpRepository by inject<OtpRepository>()
    private val passwordRepository by inject<PasswordRepository>()
    private val settingsRepository by inject<SettingsRepository>()

    override val routerState: Value<ChildStack<*, Root.Child>> =
        childStack(source = navigation, initialStack = { listOf(Config.OtpConfig) },
            childFactory = ::createChild)

    private fun createChild(configuration: Config, context: ComponentContext): Root.Child {
        return when(configuration) {
            is Config.OtpConfig -> Root.Child.Otp(otpComponent(context))
            is Config.PasswordConfig -> Root.Child.PasswordManager(passwordManager(context))
            is Config.Settings -> Root.Child.Settings(settings(context))
            is Config.About -> Root.Child.About(about(context))
        }
    }

    private fun otpComponent(context: ComponentContext) = OtpComponent(context, otpRepository)
    private fun passwordManager(context: ComponentContext) =
        PasswordComponent(context, crypto, passwordRepository, settingsRepository)
    private fun settings(context: ComponentContext) = SettingsComponent(context,
        settingsRepository, openAbout = ::navigateToAbout)
    private fun about(context: ComponentContext) =
        AboutComponent(context, openLegal = ::navigateToLegal)

    override fun navigateToOtp() = navigation.bringToFront(Config.OtpConfig)
    override fun navigateToPasswordManager() = navigation.bringToFront(Config.PasswordConfig)
    override fun navigateToSettings() = navigation.bringToFront(Config.Settings)
    override fun navigateToAbout() = navigation.bringToFront(Config.About)
    override fun navigateToLegal() {
        println("Opening privacy policy")
    }

    sealed class Config: Parcelable {
        @Parcelize
        object OtpConfig: Config()

        @Parcelize
        object PasswordConfig: Config()

        @Parcelize
        object Settings: Config()

        @Parcelize
        object About: Config()
    }
}
