package io.github.landrynorris.multifactor.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import io.github.landrynorris.multifactor.components.Root
import io.github.landrynorris.multifactor.theme.AppTheme

@Composable
internal fun RootScreen(logic: Root) {
    AppTheme {
        Surface {
            val stack by logic.routerState.subscribeAsState()
            Column {
                TopBar(getName(stack.active.instance))
                Children(stack = stack, modifier = Modifier.weight(1f)) {
                    when(val child = it.instance) {
                        is Root.Child.Otp -> OtpScreen(child.component)
                        is Root.Child.PasswordManager -> PasswordScreen(child.component)
                        is Root.Child.Settings -> Settings(child.component)
                        is Root.Child.Sync -> SyncScreen(child.component)
                    }
                }
                BottomNav(logic::navigateToOtp, logic::navigateToPasswordManager,
                    logic::navigateToSettings, logic::navigateToSync)
            }
        }
    }
}

fun getName(child: Root.Child) = when(child) {
    is Root.Child.Otp -> "Otp"
    is Root.Child.PasswordManager -> "Password Manager"
    is Root.Child.Settings -> "Settings"
    is Root.Child.Sync -> "Sync"
}

@Composable
internal fun TopBar(title: String) {
    TopAppBar {
        Text(title)
    }
}

@Composable
internal fun BottomNav(navigateToOtp: () -> Unit, navigateToPasswordManager: () -> Unit,
              navigateToSettings: () -> Unit, navigateToSync: () -> Unit) {
    BottomNavigation(modifier = Modifier.fillMaxWidth()) {
        BottomNavigationItem(false, onClick = navigateToOtp,
            icon = { Icon(Icons.Default.Pin, "otp") },
            label = { Text("otp") })
        BottomNavigationItem(false, onClick = navigateToPasswordManager,
            icon = { Icon(Icons.Default.Password, "password") },
            label = { Text("passwords") })
        BottomNavigationItem(false, onClick = navigateToSync,
            icon = { Icon(Icons.Default.Sync, "sync") },
            label = { Text("sync") })
        BottomNavigationItem(false, onClick = navigateToSettings,
            icon = { Icon(Icons.Default.Settings, "settings") },
            label = { Text("settings") })
    }
}
