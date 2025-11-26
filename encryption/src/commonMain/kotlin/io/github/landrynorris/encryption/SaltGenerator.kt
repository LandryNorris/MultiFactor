package io.github.landrynorris.encryption

import dev.whyoleg.cryptography.random.CryptographyRandom

object SaltGenerator {
    private val random = CryptographyRandom

    fun generateSalt(length: Int): ByteArray {
        return random.nextBytes(length)
    }
}