package io.github.landrynorris.multifactor.mobileapp.test

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

fun createContext(): ComponentContext {
    return DefaultComponentContext(LifecycleRegistry())
}
