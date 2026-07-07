package io.github.landrynorris.multifactor

import android.content.Context
import androidx.startup.Initializer
import io.github.landrynorris.app.initKoin
import org.koin.android.ext.koin.androidContext
import java.util.Collections.emptyList

class Startup: Initializer<Unit> {
    override fun create(context: Context) {
        initKoin {
            androidContext(context)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
