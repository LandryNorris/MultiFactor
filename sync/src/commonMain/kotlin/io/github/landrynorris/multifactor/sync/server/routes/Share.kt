package io.github.landrynorris.multifactor.sync.server.routes

import io.github.landrynorris.multifactor.sync.shared.Share
import io.ktor.server.routing.*

fun Routing.share(onPasswordCreated: (String, String) -> Unit) {
    put<Share> {
        println("Received password in put ${it.password}")
        onPasswordCreated(it.name, it.password)
    }
}

