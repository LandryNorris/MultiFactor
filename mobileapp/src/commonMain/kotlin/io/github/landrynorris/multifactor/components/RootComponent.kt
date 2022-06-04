package io.github.landrynorris.multifactor.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

interface Root {
    val routerState: Value<RouterState<*, Child>>

    sealed class Child {
        class Otp(val component: OtpLogic): Child()
    }
}

class RootComponent(context: ComponentContext): ComponentContext by context, Root {
    private val router =
        router<Config, Root.Child>(
            initialConfiguration = Config.OtpConfig,
            handleBackButton = true, // Pop the back stack on back button press
            childFactory = ::createChild
        )

    override val routerState: Value<RouterState<*, Root.Child>> = router.state

    private fun createChild(configuration: Config, context: ComponentContext): Root.Child {
        return when(configuration) {
            is Config.OtpConfig -> Root.Child.Otp(otpComponent(context))
        }
    }

    private fun otpComponent(context: ComponentContext): OtpComponent = OtpComponent(context)

    sealed class Config: Parcelable {
        @Parcelize
        object OtpConfig: Config()
    }
}
