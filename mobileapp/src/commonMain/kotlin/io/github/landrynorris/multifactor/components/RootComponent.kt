package io.github.landrynorris.multifactor.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import io.github.landrynorris.multifactor.repository.OtpRepository
import io.github.landrynorris.multifactor.repository.PasswordRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface Root {
    val routerState: Value<ChildStack<*, Child>>

    fun navigateToOtp() {}
    fun navigateToPasswordManager() {}

    sealed class Child {
        class Otp(val component: OtpLogic): Child()
        class PasswordManager(val component: PasswordComponent): Child()
    }
}

class RootComponent(context: ComponentContext): ComponentContext by context, Root, KoinComponent {
    private val navigation = StackNavigation<Config>()
    private val otpRepository by inject<OtpRepository>()
    private val passwordRepository by inject<PasswordRepository>()

    override val routerState: Value<ChildStack<*, Root.Child>> =
        childStack(source = navigation, initialStack = { listOf(Config.OtpConfig) },
            childFactory = ::createChild)

    private fun createChild(configuration: Config, context: ComponentContext): Root.Child {
        return when(configuration) {
            is Config.OtpConfig -> Root.Child.Otp(otpComponent(context))
            is Config.PasswordConfig -> Root.Child.PasswordManager(passwordManager(context))
        }
    }

    private fun otpComponent(context: ComponentContext) = OtpComponent(context, otpRepository)
    private fun passwordManager(context: ComponentContext) =
        PasswordComponent(context, passwordRepository)

    override fun navigateToOtp() = navigation.bringToFront(Config.OtpConfig)
    override fun navigateToPasswordManager() = navigation.bringToFront(Config.PasswordConfig)

    sealed class Config: Parcelable {
        @Parcelize
        object OtpConfig: Config()

        @Parcelize
        object PasswordConfig: Config()
    }
}
