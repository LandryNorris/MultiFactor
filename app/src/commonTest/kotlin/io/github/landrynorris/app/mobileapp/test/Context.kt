package io.github.landrynorris.app.mobileapp.test

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

fun createContext(): ComponentContext {
    return DefaultComponentContext(LifecycleRegistry())
}
