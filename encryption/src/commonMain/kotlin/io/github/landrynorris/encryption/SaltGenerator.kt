package io.github.landrynorris.encryption

import com.soywiz.krypto.SecureRandom

object SaltGenerator {
    private val random = SecureRandom

    fun generateSalt(length: Int): ByteArray {
        return random.nextBytes(length)
    }
}