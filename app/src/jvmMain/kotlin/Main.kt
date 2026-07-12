import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.github.landrynorris.app.components.RootComponent
import io.github.landrynorris.app.compose.RootScreen
import io.github.landrynorris.app.initKoin
import io.github.landrynorris.encryption.SecureCrypto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

fun main() = entryPoint()

fun entryPoint() {
    initKoin()
    val logic =
        runBlocking(Dispatchers.Main) {
            RootComponent(DefaultComponentContext(LifecycleRegistry()), SecureCrypto)
        }
    singleWindowApplication { RootScreen(logic) }
}
