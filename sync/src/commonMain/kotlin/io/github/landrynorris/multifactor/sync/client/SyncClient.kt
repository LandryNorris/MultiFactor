package io.github.landrynorris.multifactor.sync.client

import io.github.landrynorris.multifactor.sync.shared.PORT
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*

class SyncClient {
    private val client = HttpClient(CIO) {
        expectSuccess = false
    }

    suspend fun checkServer(ipPortPair: String, code: String): Boolean {
        println("Checking $ipPortPair")
        val checkResponse = try {
            client.get("http://$ipPortPair/check") {
                url {
                    parameters.append("code", code)
                }
            }.status
        } catch(e: Exception) {
            HttpStatusCode.NotFound
        }

        return checkResponse == HttpStatusCode.OK
    }
}