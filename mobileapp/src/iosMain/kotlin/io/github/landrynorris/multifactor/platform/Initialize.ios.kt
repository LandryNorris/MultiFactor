package io.github.landrynorris.multifactor.platform

import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import io.github.landrynorris.multifactor.AppDatabase
import org.koin.dsl.module

actual val platformModule = module {
    single {
        val driver = NativeSqliteDriver(AppDatabase.Schema, "otpdatabase")
        AppDatabase(driver)
    }
}
