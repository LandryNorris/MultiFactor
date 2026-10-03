package io.github.landrynorris.app.platform

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.coroutines.SuspendSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import io.github.landrynorris.app.Directories
import io.github.landrynorris.app.repository.SettingsRepository
import io.github.landrynorris.database.AppDatabase
import kotlinx.io.files.Path
import org.koin.dsl.module
import platform.Foundation.NSDownloadsDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUserDefaults
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalSettingsApi::class)
actual val platformModule = module {
    single {
        val driver = NativeSqliteDriver(AppDatabase.Schema, "otpdatabase")
        AppDatabase(driver)
    }

    single<SuspendSettings> { NSUserDefaultsSettings(NSUserDefaults()).toFlowSettings() }

    single { SettingsRepository(get()) }

    single {
        val downloadsPath = (NSFileManager.defaultManager.URLsForDirectory(
            NSDownloadsDirectory,
            NSUserDomainMask
        ).firstOrNull() as? NSURL)?.path ?: NSTemporaryDirectory()

        Directories(
            downloadsDirectory = Path(downloadsPath),
            tempDirectory = Path(NSTemporaryDirectory()),
        )
    }
}
