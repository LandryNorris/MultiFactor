package io.github.landrynorris.multifactor

import androidx.compose.ui.window.Application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.github.landrynorris.encryption.SecureCrypto
import io.github.landrynorris.multifactor.components.RootComponent
import io.github.landrynorris.multifactor.compose.RootScreen
import kotlinx.cinterop.*
import platform.Foundation.NSStringFromClass
import platform.UIKit.*

@Suppress("unused") //called from Swift
object EntryPoint {
    fun createEntryPoint(): UIViewController {
        val logic = RootComponent(DefaultComponentContext(LifecycleRegistry()), SecureCrypto)
        println("Starting application")
        return Application("MultiFactor") {
            RootScreen(logic)
        }
    }

    fun main() {
        val args = emptyArray<String>()
        memScoped {
            val argc = args.size + 1
            val argv = (arrayOf("skikoApp") + args).map { it.cstr.ptr }.toCValues()
            autoreleasepool {
                UIApplicationMain(argc, argv, null, NSStringFromClass(SkikoAppDelegate))
            }
        }
    }

    class SkikoAppDelegate : UIResponder, UIApplicationDelegateProtocol {
        companion object : UIResponderMeta(), UIApplicationDelegateProtocolMeta

        @ObjCObjectBase.OverrideInit
        constructor() : super()

        private var _window: UIWindow? = null
        override fun window() = _window
        override fun setWindow(window: UIWindow?) {
            _window = window
        }

        override fun application(application: UIApplication, didFinishLaunchingWithOptions: Map<Any?, *>?): Boolean {
            initKoin()
            window = UIWindow(frame = UIScreen.mainScreen.bounds)
            println("Bounds: ${UIScreen.mainScreen.bounds.useContents { size.width }}x${UIScreen.mainScreen.bounds.useContents { size.width }}")
            window!!.rootViewController = createEntryPoint()
            window!!.makeKeyAndVisible()
            println("Set up window")
            return true
        }
    }
}
