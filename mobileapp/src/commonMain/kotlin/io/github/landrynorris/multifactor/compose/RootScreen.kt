package io.github.landrynorris.multifactor.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import io.github.landrynorris.multifactor.components.Root

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootScreen(logic: Root) {
    AppTheme {
        Surface {
            val stack by logic.routerState.subscribeAsState()
            Column {
                Children(stack = stack, modifier = Modifier.weight(1f)) {
                    when(val child = it.instance) {
                        is Root.Child.Otp -> OtpScreen(child.component)
                        is Root.Child.PasswordManager -> PasswordScreen(child.component)
                    }
                }
                BottomNavigation(modifier = Modifier.fillMaxWidth()) {
                    BottomNavigationItem(false, onClick = logic::navigateToOtp,
                        icon = { Icon(Icons.Default.Password, "password") },
                        label = { Text("otp") })
                    BottomNavigationItem(false, onClick = logic::navigateToPasswordManager,
                        icon = { Icon(Icons.Default.Password, "password") },
                        label = { Text("password manager") })
                }
            }
        }
    }
}
