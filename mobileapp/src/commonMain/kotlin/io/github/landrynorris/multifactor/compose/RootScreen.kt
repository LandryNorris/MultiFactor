package io.github.landrynorris.multifactor.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import io.github.landrynorris.multifactor.components.Root
import io.github.landrynorris.multifactor.theme.AppTheme

@Composable
internal fun RootScreen(logic: Root) {
    println("Setting up theme")
    AppTheme {
        println("Setting up surface")
        Surface {
            val stack by logic.routerState.subscribeAsState()
            println("Stack instance is ${stack.active.instance}")
            Column {
                TopBar(getName(stack.active.instance))
                println("Name is ${getName(stack.active.instance)}")
                Children(stack = stack, modifier = Modifier.weight(1f)) {
                    when(val child = it.instance) {
                        is Root.Child.Otp -> OtpScreen(child.component)
                        is Root.Child.PasswordManager -> PasswordScreen(child.component)
                        is Root.Child.Settings -> Settings(child.component)
                    }
                }
                BottomNav(logic::navigateToOtp, logic::navigateToPasswordManager,
                    logic::navigateToSettings)
            }
        }
    }
}

fun getName(child: Root.Child) = when(child) {
    is Root.Child.Otp -> "Otp"
    is Root.Child.PasswordManager -> "Password Manager"
    is Root.Child.Settings -> "Settings"
}

@Composable
internal fun TopBar(title: String) {
    TopAppBar {
        Text(title)
    }
}

@Composable
internal fun BottomNav(navigateToOtp: () -> Unit, navigateToPasswordManager: () -> Unit,
              navigateToSettings: () -> Unit) {
    BottomNavigation(modifier = Modifier.fillMaxWidth()) {
        BottomNavigationItem(false, onClick = navigateToOtp,
            icon = { Icon(Icons.Default.Pin, "otp") },
            label = { Text("otp") })
        BottomNavigationItem(false, onClick = navigateToPasswordManager,
            icon = { Icon(Icons.Default.Password, "password") },
            label = { Text("passwords") })
        BottomNavigationItem(false, onClick = navigateToSettings,
            icon = { Icon(Icons.Default.Settings, "settings") },
            label = { Text("settings") })
    }
}
