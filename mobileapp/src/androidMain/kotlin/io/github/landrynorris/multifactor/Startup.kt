package io.github.landrynorris.multifactor

import android.content.Context
import androidx.startup.Initializer
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.github.landrynorris.multifactor.repository.OtpRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class Startup: Initializer<Unit> {
    override fun create(context: Context) {
        val driver = AndroidSqliteDriver(OtpDatabase.Schema, context, "otpdatabase")
        initKoin {
            androidContext(context)
            modules(
                module {
                    single { OtpDatabase(driver) }
                }
            )
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}