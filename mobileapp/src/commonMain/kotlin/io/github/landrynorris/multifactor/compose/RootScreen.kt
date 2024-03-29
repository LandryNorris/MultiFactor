package io.github.landrynorris.multifactor.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
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
                        is Root.Child.About -> About(child.component)
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
    is Root.Child.About -> "About"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopBar(title: String) {
    TopAppBar(title = {
        Text(title)
    })
}

@Composable
internal fun BottomNav(navigateToOtp: () -> Unit, navigateToPasswordManager: () -> Unit,
              navigateToSettings: () -> Unit) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        NavigationBarItem(false, onClick = navigateToOtp,
            icon = { Icon(Icons.Default.Pin, "otp") },
            label = { Text("otp") })
        NavigationBarItem(false, onClick = navigateToPasswordManager,
            icon = { Icon(Icons.Default.Password, "password") },
            label = { Text("passwords") })
        NavigationBarItem(false, onClick = navigateToSettings,
            icon = { Icon(Icons.Default.Settings, "settings") },
            label = { Text("settings") })
    }
}
