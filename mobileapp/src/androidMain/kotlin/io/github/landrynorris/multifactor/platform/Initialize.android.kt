package io.github.landrynorris.multifactor.platform

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.russhwolf.settings.coroutines.SuspendSettings
import com.russhwolf.settings.datastore.DataStoreSettings
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.github.landrynorris.database.AppDatabase
import io.github.landrynorris.multifactor.repository.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single {
        val driver = AndroidSqliteDriver(AppDatabase.Schema, get(), "multifactor-database")
        //AppDatabase.Schema.migrate(driver, 0, AppDatabase.Schema.version)
        AppDatabase(driver)
    }

    single<SuspendSettings> {
        val context = androidContext()
        val datastore = context.datastore
        DataStoreSettings(datastore)
    }

    single {
        SettingsRepository(get())
    }
}

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "settings")

