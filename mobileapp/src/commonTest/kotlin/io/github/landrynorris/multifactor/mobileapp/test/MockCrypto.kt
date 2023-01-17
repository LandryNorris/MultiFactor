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

    override fun decrypt(data: ByteArray, iv: ByteArray): ByteArray {
        if(key.isEmpty()) generateKey("")
        return Encryption.encrypt(data, iv, key)
    }

    override fun encrypt(data: ByteArray): EncryptResult {
        if(key.isEmpty()) generateKey("")
        val iv = createSalt()
        val encryptedData = Encryption.encrypt(data, iv, key)

        return EncryptResult(iv, encryptedData)
    }

    private fun createSalt(): ByteArray = Random.nextBytes(256)

    private fun createKey(): ByteArray = Random.nextBytes(128)
}