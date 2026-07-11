package io.github.landrynorris.app.mobileapp.test

import androidx.test.core.app.ActivityScenario
import io.github.landrynorris.app.components.OtpLogic
import io.github.landrynorris.app.components.PasswordLogic
import io.github.landrynorris.app.components.RootComponent
import io.github.landrynorris.app.components.SettingsLogic
import io.github.landrynorris.multifactor.MainActivity

fun ActivityScenario<MainActivity>.withRootComponent(block: RootComponent.() -> Unit) {
    onActivity {
        it.logic.block()
    }
}

fun ActivityScenario<MainActivity>.withCurrentLogic(block: Any.() -> Unit) {
    withRootComponent {
        val activeChild = routerState.value.active
        val component = activeChild.instance.component
        component.block()
    }
}

fun ActivityScenario<MainActivity>.withOtpLogic(block: OtpLogic.() -> Unit) {
    withCurrentLogic {
        if(this !is OtpLogic) error("Otp Logic is not active. Make sure to navigate")
        block()
    }
}

fun ActivityScenario<MainActivity>.withPasswordLogic(block: PasswordLogic.() -> Unit) {
    withCurrentLogic {
        if(this !is PasswordLogic) error("Password Logic is not active. Make sure to navigate")
        block()
    }
}

fun ActivityScenario<MainActivity>.withSettingsLogic(block: SettingsLogic.() -> Unit) {
    withCurrentLogic {
        if(this !is SettingsLogic) error("Settings Logic is not active. Make sure to navigate")
        block()
    }
}
