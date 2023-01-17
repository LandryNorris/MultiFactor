package io.github.landrynorris.multifactor.mobileapp.test

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.MockSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import io.github.landrynorris.database.AppDatabase
import io.github.landrynorris.multifactor.repository.OtpRepository
import io.github.landrynorris.multifactor.repository.PasswordRepository
import io.github.landrynorris.multifactor.repository.SettingsRepository

fun createOtpRepository(): OtpRepository {
    val driver = createInMemoryTestDriver()
    val database = AppDatabase(driver)
    return OtpRepository(database)
}

fun createPasswordRepository(): PasswordRepository {
    val driver = createInMemoryTestDriver()
    val database = AppDatabase(driver)
    return PasswordRepository(database)
}

@OptIn(ExperimentalSettingsApi::class)
fun createSettingsRepository(): SettingsRepository {
    val settings = MockSettings().toFlowSettings()
    return SettingsRepository(settings)
}
