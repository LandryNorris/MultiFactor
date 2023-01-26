
import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.github.landrynorris.encryption.SecureCrypto
import io.github.landrynorris.multifactor.components.RootComponent
import io.github.landrynorris.multifactor.compose.RootScreen
import io.github.landrynorris.multifactor.initKoin

fun main() = entryPoint()

fun entryPoint() {
    initKoin()
    val logic = RootComponent(DefaultComponentContext(LifecycleRegistry()), SecureCrypto)
    singleWindowApplication {
        RootScreen(logic)
    }
}
