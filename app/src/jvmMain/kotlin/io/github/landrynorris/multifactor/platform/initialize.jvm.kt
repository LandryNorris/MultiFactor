package io.github.landrynorris.multifactor.platform

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.coroutines.toSuspendSettings
import io.github.landrynorris.database.AppDatabase
import io.github.landrynorris.multifactor.repository.SettingsRepository
import org.koin.dsl.module
import java.io.File
import java.util.prefs.Preferences

@OptIn(
    ExperimentalSettingsApi::class,
)
actual val platformModule = module {
    single {
        val driver = initializeDatabaseDriver()
        AppDatabase(driver)
    }

    single {
        PreferencesSettings(Preferences.userRoot()).toSuspendSettings()
    }

    single {
        SettingsRepository(get())
    }
}

private fun initializeDatabaseDriver(): SqlDriver {
    val home = System.getProperty("user.home")
    val dbFile = File(home, ".multifactor/db/multifactor.db")
    dbFile.parentFile.mkdirs()
    val url = "jdbc:sqlite:${dbFile.absolutePath}"

    return JdbcSqliteDriver(url)
}
