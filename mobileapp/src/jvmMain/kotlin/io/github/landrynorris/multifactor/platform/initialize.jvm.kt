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

    return JdbcSqliteDriver(url).also {
        migrateIfNeeded(it)
    }
}

fun migrateIfNeeded(driver: SqlDriver) {
    val result = driver.execute(null,
        "PRAGMA $versionPragma", 0)
    val oldVersion = result.value
    val newVersion = AppDatabase.Schema.version

    println("handling $oldVersion -> $newVersion")

    if(oldVersion == 0L) {
        AppDatabase.Schema.create(driver)
        driver.execute(null, "PRAGMA $versionPragma=$newVersion", 0)
    } else if(oldVersion < newVersion) {
        AppDatabase.Schema.migrate(driver, oldVersion, newVersion)
        driver.execute(null, "PRAGMA $versionPragma=$newVersion", 0)
    }
}

private const val versionPragma = "user_version"
