package io.github.landrynorris.multifactor.platform

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.coroutines.SuspendSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import io.github.landrynorris.database.AppDatabase
import io.github.landrynorris.multifactor.repository.SettingsRepository
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

@OptIn(ExperimentalSettingsApi::class)
actual val platformModule = module {
    single {
        val driver = NativeSqliteDriver(AppDatabase.Schema, "otpdatabase")
        AppDatabase(driver)
    }

    single<SuspendSettings> {
        NSUserDefaultsSettings(NSUserDefaults()).toFlowSettings()
    }

    single {
        SettingsRepository(get())
    }
}
