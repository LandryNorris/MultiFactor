package io.github.landrynorris.multifactor.sync.client

import io.github.landrynorris.multifactor.sync.shared.Check
import io.github.landrynorris.multifactor.sync.shared.Share
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*

class SyncClient {
    private val client = HttpClient(CIO) {
        expectSuccess = false

        install(Resources)
    }

    suspend fun checkServer(ipPortPair: String, code: String): Boolean {
        println("Checking $ipPortPair")
        val checkResponse = try {
            client.get(Check(code)) {
                host = "http://$ipPortPair"
            }.status
        } catch(e: Exception) {
            HttpStatusCode.NotFound
        }

        return checkResponse == HttpStatusCode.OK
    }

    suspend fun sharePassword(ipPortPair: String, name: String, password: String) {
        println("Making share request to $ipPortPair")
        val result = client.put(Share(name, password)) {
            host = "http://$ipPortPair"
        }
        println("Share password result is ${result.status}")
    }
}
