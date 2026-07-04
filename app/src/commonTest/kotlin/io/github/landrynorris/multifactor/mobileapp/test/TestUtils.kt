package io.github.landrynorris.multifactor.mobileapp.test

import io.github.landrynorris.multifactor.repository.OtpRepository
import io.github.landrynorris.multifactor.repository.PasswordRepository
import io.github.landrynorris.multifactor.repository.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import org.koin.core.context.startKoin
import org.koin.dsl.module
import kotlin.test.assertNotNull

suspend fun assertOccursWithin(duration: Long, message: String = "",
                               predicate: suspend () -> Boolean) {
    val result = withTimeoutOrNull(duration) {
        while(!predicate()) delay(1)
    }
    assertNotNull(result, "Predicate${if(message.isNotEmpty()) " " else ""}" +
            "$message did not return true within $duration ms")
}

fun initKoin(otpRepository: OtpRepository = createOtpRepository(),
             passwordRepository: PasswordRepository = createPasswordRepository(),
             settingsRepository: SettingsRepository = createSettingsRepository()
) {
    startKoin {
        modules(repositoryModule(otpRepository, passwordRepository, settingsRepository))
    }
}

fun repositoryModule(otpRepository: OtpRepository = createOtpRepository(),
                     passwordRepository: PasswordRepository = createPasswordRepository(),
                     settingsRepository: SettingsRepository = createSettingsRepository()) = module {
    single { otpRepository }
    single { passwordRepository }
    single { settingsRepository }
}
