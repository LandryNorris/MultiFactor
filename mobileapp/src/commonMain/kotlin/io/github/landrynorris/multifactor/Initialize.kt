package io.github.landrynorris.multifactor

import io.github.landrynorris.multifactor.repository.OtpRepository
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(commonModule)
    }
}

val commonModule = module {
    single { OtpRepository() }
}
