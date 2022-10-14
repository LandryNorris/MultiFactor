package io.github.landrynorris.multifactor

import androidx.compose.ui.window.Application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.github.landrynorris.multifactor.components.RootComponent
import io.github.landrynorris.multifactor.compose.RootScreen
import platform.UIKit.UIViewController

object EntryPoint {
    fun createEntryPoint(): UIViewController {
        val logic = RootComponent(DefaultComponentContext(LifecycleRegistry()))
        return Application("MultiFactor") {
            RootScreen(logic)
        }
    }
}
