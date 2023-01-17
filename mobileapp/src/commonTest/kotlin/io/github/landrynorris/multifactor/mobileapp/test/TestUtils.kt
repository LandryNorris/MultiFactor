package io.github.landrynorris.multifactor.mobileapp.test

import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.test.assertNotNull

suspend fun assertOccursWithin(duration: Long, message: String = "",
                               predicate: suspend () -> Boolean) {
    val result = withTimeoutOrNull(duration) {
        while(!predicate()) delay(1)
    }
    assertNotNull(result, "Predicate${if(message.isNotEmpty()) " " else ""}" +
            "$message did not return true within $duration ms")
}
