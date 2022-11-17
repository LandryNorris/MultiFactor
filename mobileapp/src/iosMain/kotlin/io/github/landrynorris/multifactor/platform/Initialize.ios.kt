package io.github.landrynorris.multifactor.platform

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import io.github.landrynorris.database.AppDatabase
import io.github.landrynorris.multifactor.repository.SettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

@OptIn(ExperimentalSettingsApi::class, ExperimentalCoroutinesApi::class)
actual val platformModule = module {
    single {
        val driver = NativeSqliteDriver(AppDatabase.Schema, "otpdatabase")
        AppDatabase(driver)
    }

    single {
        AppleSettings(NSUserDefaults()).toFlowSettings()
    }

    single {
        SettingsRepository(get())
    }
}
