package io.github.landrynorris.encryption

interface Crypto {
    fun generateKey(alias: String)
    fun encrypt(data: ByteArray): EncryptResult
    fun decrypt(data: ByteArray, iv: ByteArray): ByteArray
}