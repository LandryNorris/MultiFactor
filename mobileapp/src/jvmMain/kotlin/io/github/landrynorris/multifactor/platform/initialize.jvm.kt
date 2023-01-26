package io.github.landrynorris.multifactor.platform

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.JvmPreferencesSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import io.github.landrynorris.database.AppDatabase
import io.github.landrynorris.multifactor.repository.SettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module
import java.io.File
import java.util.prefs.Preferences

@OptIn(
    ExperimentalSettingsApi::class,
    ExperimentalSettingsImplementation::class,
    ExperimentalCoroutinesApi::class
)
actual val platformModule = module {
    single {
        val driver = initializeDatabaseDriver()
        AppDatabase(driver)
    }

    single {
        JvmPreferencesSettings(Preferences.userRoot()).toFlowSettings()
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
    val oldVersion = driver.executeQuery(null,
        "PRAGMA $versionPragma", 0).use {
        if(it.next()) it.getLong(0)?.toInt() else null
    } ?: 0
    val newVersion = AppDatabase.Schema.version

    println("handling $oldVersion -> $newVersion")

    if(oldVersion == 0) {
        AppDatabase.Schema.create(driver)
        driver.execute(null, "PRAGMA $versionPragma=$newVersion", 0)
    } else if(oldVersion < newVersion) {
        AppDatabase.Schema.migrate(driver, oldVersion, newVersion)
        driver.execute(null, "PRAGMA $versionPragma=$newVersion", 0)
    }
}

private const val versionPragma = "user_version"
