package io.github.landrynorris.multifactor.platform

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.JvmPreferencesSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import io.github.landrynorris.database.AppDatabase
import io.github.landrynorris.multifactor.repository.SettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module
import java.util.prefs.Preferences

@OptIn(
    ExperimentalSettingsApi::class,
    ExperimentalSettingsImplementation::class,
    ExperimentalCoroutinesApi::class
)
actual val platformModule = module {
    single {
        val driver = JdbcSqliteDriver("otpdatabase")
        AppDatabase.Schema.create(driver)
        AppDatabase(driver)
    }

    single {
        JvmPreferencesSettings(Preferences.userRoot()).toFlowSettings()
    }

    single {
        SettingsRepository(get())
    }
}
