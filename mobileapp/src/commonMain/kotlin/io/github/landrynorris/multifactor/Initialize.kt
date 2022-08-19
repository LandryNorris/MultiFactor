package io.github.landrynorris.multifactor

import io.github.landrynorris.multifactor.platform.platformModule
import io.github.landrynorris.multifactor.repository.OtpRepository
import io.github.landrynorris.multifactor.repository.PasswordRepository
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
    single { OtpRepository(get()) }
    single { PasswordRepository(get()) }
}
