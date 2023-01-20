package io.github.landrynorris.multifactor.mobileapp.test

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.MockSettings
import com.russhwolf.settings.coroutines.toFlowSettings
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
    return PasswordRepository(database)
}

@OptIn(ExperimentalSettingsApi::class)
fun createSettingsRepository(): SettingsRepository {
    val settings = MockSettings().toFlowSettings()
    return SettingsRepository(settings)
}
