package io.github.landrynorris.encryption

interface Crypto {
    fun generateKey(alias: String)
    fun encrypt(data: ByteArray, alias: String): EncryptResult
    fun decrypt(data: ByteArray, iv: ByteArray, alias: String): ByteArray
}