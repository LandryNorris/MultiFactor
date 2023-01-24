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
import java.io.File
import java.sql.DriverManager
import java.util.prefs.Preferences

@OptIn(
    ExperimentalSettingsApi::class,
    ExperimentalSettingsImplementation::class,
    ExperimentalCoroutinesApi::class
)
actual val platformModule = module {
    single {
        val home = System.getProperty("user.home")
        val dbFile = File(home, ".multifactor/db/multifactor.db")
        dbFile.parentFile.mkdirs()
        val url = "jdbc:sqlite:${dbFile.absolutePath}"
        val driver = JdbcSqliteDriver(url)
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
