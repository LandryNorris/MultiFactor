package io.github.landrynorris.multifactor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import io.github.landrynorris.encryption.SecureCrypto
import io.github.landrynorris.app.components.RootComponent
import io.github.landrynorris.app.compose.RootScreen

class MainActivity: ComponentActivity() {
    lateinit var logic: RootComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logic = RootComponent(defaultComponentContext(), SecureCrypto)
        setContent {
            RootScreen(logic)
        }
    }
}
