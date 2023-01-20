package io.github.landrynorris.multifactor.mobileapp.test

import io.github.landrynorris.multifactor.components.Root
import io.github.landrynorris.multifactor.components.RootComponent
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import kotlin.test.Test
import kotlin.test.assertIs

class RootComponentTest: KoinTest {

    @Test
    fun testNavigation() {
        val component = createComponent()
        assertIs<Root.Child.Otp>(component.navigationState)

        component.navigateToPasswordManager()
        assertIs<Root.Child.PasswordManager>(component.navigationState)

        component.navigateToSettings()
        assertIs<Root.Child.Settings>(component.navigationState)

        component.navigateToOtp()
        assertIs<Root.Child.Otp>(component.navigationState)

        stopKoin()
    }

    private val Root.navigationState: Root.Child get() {
        return routerState.value.active.instance
    }

    private fun createComponent(): Root {
        initKoin()
        return RootComponent(createContext(), MockCrypto())
    }
}