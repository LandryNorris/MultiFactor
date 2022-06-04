package io.github.landrynorris.multifactor

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import io.github.landrynorris.multifactor.components.Root

@Composable
fun RootScreen(logic: Root) {
    AppTheme {
        Surface {
            Children(routerState = logic.routerState) {
                when(val child = it.instance) {
                    is Root.Child.Otp -> OtpScreen(child.component)
                }
            }
        }
    }
}
