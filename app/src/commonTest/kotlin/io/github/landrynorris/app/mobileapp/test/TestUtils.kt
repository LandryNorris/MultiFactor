package io.github.landrynorris.app.mobileapp.test

import io.github.landrynorris.app.repository.OtpRepository
import io.github.landrynorris.app.repository.PasswordRepository
import io.github.landrynorris.app.repository.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import org.koin.core.context.startKoin
import org.koin.dsl.module
import kotlin.test.assertNotNull
import kotlin.time.Duration.Companion.milliseconds

suspend fun assertOccursWithin(duration: Long, message: String = "",
                               predicate: suspend () -> Boolean) {
    val result = withTimeoutOrNull(duration.milliseconds) {
        while(!predicate()) delay(1.milliseconds)
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
