package io.github.landrynorris.app.platform

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.coroutines.toSuspendSettings
import io.github.landrynorris.app.Directories
import io.github.landrynorris.app.repository.SettingsRepository
import io.github.landrynorris.database.AppDatabase
import kotlinx.io.files.Path
import java.io.File
import java.util.prefs.Preferences
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
actual val platformModule = module {
    single {
        val driver = initializeDatabaseDriver()
        AppDatabase(driver)
    }

    single { PreferencesSettings(Preferences.userRoot()).toSuspendSettings() }

    single { SettingsRepository(get()) }

    single {
        val userHome = System.getProperty("user.home") ?: "."
        val downloadsDir = File(userHome, "Downloads")
        val tempDir = File(System.getProperty("java.io.tmpdir") ?: ".")
        Directories(
            downloadsDirectory = Path(downloadsDir.absolutePath),
            tempDirectory = Path(tempDir.absolutePath),
        )
    }
}

private fun initializeDatabaseDriver(): SqlDriver {
    val home = System.getProperty("user.home")
    val dbFile = File(home, ".multifactor/db/multifactor.db")
    dbFile.parentFile.mkdirs()
    val url = "jdbc:sqlite:${dbFile.absolutePath}"

    return JdbcSqliteDriver(url)
}
