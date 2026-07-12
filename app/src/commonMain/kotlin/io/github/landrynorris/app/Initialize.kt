package io.github.landrynorris.app

import io.github.landrynorris.app.platform.platformModule
import io.github.landrynorris.app.repository.OtpRepository
import io.github.landrynorris.app.repository.PasswordRepository
import io.github.landrynorris.encryption.Crypto
import io.github.landrynorris.encryption.SecureCrypto
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(platformModule, commonModule)
    }
}

val commonModule = module {
    single<Crypto> { SecureCrypto }
    single { OtpRepository(get()) }
    single { PasswordRepository(get(), get()) }
}
