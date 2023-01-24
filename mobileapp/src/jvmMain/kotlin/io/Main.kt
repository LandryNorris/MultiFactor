
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.github.landrynorris.encryption.SecureCrypto
import io.github.landrynorris.multifactor.components.RootComponent
import io.github.landrynorris.multifactor.compose.RootScreen

fun main() = entryPoint()

fun entryPoint() {
    val logic = RootComponent(DefaultComponentContext(LifecycleRegistry()), SecureCrypto)
    application {
        RootScreen(logic)
    }
}
