package io.github.landrynorris.multifactor.platform

import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.github.landrynorris.multifactor.AppDatabase
import org.koin.dsl.module

actual val platformModule = module {
    single {
        val driver = AndroidSqliteDriver(AppDatabase.Schema, get(), "otpdatabase")
        AppDatabase(driver)
    }
}
