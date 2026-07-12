package io.github.landrynorris.app.platform

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.coroutines.SuspendSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import io.github.landrynorris.app.repository.SettingsRepository
import io.github.landrynorris.database.AppDatabase
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

@OptIn(ExperimentalSettingsApi::class)
actual val platformModule = module {
    single {
        val driver = NativeSqliteDriver(AppDatabase.Schema, "otpdatabase")
        AppDatabase(driver)
    }

    single<SuspendSettings> { NSUserDefaultsSettings(NSUserDefaults()).toFlowSettings() }

    single { SettingsRepository(get()) }
}
