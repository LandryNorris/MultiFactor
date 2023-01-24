package io.github.landrynorris.multifactor.sync.server

import io.github.landrynorris.multifactor.sync.server.routes.check
import io.github.landrynorris.multifactor.sync.server.routes.share
import io.github.landrynorris.multifactor.sync.shared.PORT
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*

fun createSyncServer(expectedCode: String,
                     onPasswordCreated: (String, String) -> Unit) =
    embeddedServer(CIO, port = PORT) {
    install(Resources)
    install(CORS)

    routing {
        check(expectedCode)
        share(onPasswordCreated)
    }
}.also {
    it.start()
}
