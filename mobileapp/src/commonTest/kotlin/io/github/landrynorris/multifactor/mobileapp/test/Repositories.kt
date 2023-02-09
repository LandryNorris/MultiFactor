package io.github.landrynorris.multifactor.mobileapp.test

import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.coroutines.toSuspendSettings
import com.squareup.sqldelight.db.SqlDriver
import io.github.landrynorris.database.AppDatabase
import io.github.landrynorris.multifactor.repository.OtpRepository
import io.github.landrynorris.multifactor.repository.PasswordRepository
import io.github.landrynorris.multifactor.repository.SettingsRepository

var latestDriver: SqlDriver? = null

fun createOtpRepository(): OtpRepository {
    val driver = createInMemoryTestDriver()
    latestDriver = driver
    val database = AppDatabase(driver)
    database.otpQueries.clear()
    return OtpRepository(database)
}

fun createPasswordRepository(): PasswordRepository {
    val driver = createInMemoryTestDriver()
    latestDriver = driver
    val database = AppDatabase(driver)
    database.passwordQueries.clear()
    return PasswordRepository(database, MockCrypto())
}

fun createSettingsRepository(): SettingsRepository {
    val settings = MapSettings().toSuspendSettings()
    return SettingsRepository(settings)
}
