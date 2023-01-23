package io.github.landrynorris.multifactor.sync.client

import io.github.landrynorris.multifactor.sync.shared.PORT

class CheckClient {
    suspend fun findServerIpPortPair(check: suspend (String) -> Boolean): String? {
        val subBlock = "192.168.0"

        for(i in 1 until 256) {
            val ip = "$subBlock.$i"

            val success = check("$ip:$PORT")
            if(success) return "$ip:$PORT"
        }

        return null
    }
}