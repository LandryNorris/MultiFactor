package io.github.landrynorris.multifactor.mobileapp.test

import io.github.landrynorris.encryption.Crypto
import io.github.landrynorris.encryption.EncryptResult
import io.github.landrynorris.encryption.Encryption
import kotlin.random.Random

class MockCrypto: Crypto {
    private var key: ByteArray = byteArrayOf()

    override fun generateKey(alias: String) {
        key = createKey()
    }

    override fun decrypt(data: ByteArray, iv: ByteArray, alias: String): ByteArray {
        if(key.isEmpty()) generateKey(alias)
        return Encryption.decrypt(data, iv, key)
    }

    override fun encrypt(data: ByteArray, alias: String): EncryptResult {
        if(key.isEmpty()) generateKey(alias)
        val iv = createSalt()
        val encryptedData = Encryption.encrypt(data, iv, key)

        return EncryptResult(iv, encryptedData)
    }

    private fun createSalt(): ByteArray = Random.nextBytes(256)

    private fun createKey(): ByteArray = Random.nextBytes(128/8)
}