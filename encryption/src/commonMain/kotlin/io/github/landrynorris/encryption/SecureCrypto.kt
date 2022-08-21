package io.github.landrynorris.encryption

expect object SecureCrypto {
    fun generateKey(alias: String)
    fun encrypt(data: ByteArray): EncryptResult
    fun decrypt(data: ByteArray, iv: ByteArray): ByteArray
}

class EncryptResult(val iv: ByteArray, val data: ByteArray)
