package io.github.landrynorris.encryption

import korlibs.crypto.SecureRandom

object SaltGenerator {
    private val random = SecureRandom

    fun generateSalt(length: Int): ByteArray {
        return random.nextBytes(length)
    }
}