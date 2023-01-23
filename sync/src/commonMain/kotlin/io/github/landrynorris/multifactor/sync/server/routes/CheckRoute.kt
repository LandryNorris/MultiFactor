package io.github.landrynorris.multifactor.sync.server.routes

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * GET Route that allows a client to check if a valid server is running on this device.
 * For security, this will only respond if the correct code is given
 *
 * @param expectedCode Code to accept in order to return success
 */
fun Routing.check(expectedCode: String) {
    get<Check> {
        if(it.code == expectedCode) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}

@Resource("/check")
data class Check(val code: String)
