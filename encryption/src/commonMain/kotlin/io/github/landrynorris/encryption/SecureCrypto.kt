package io.github.landrynorris.encryption

expect object SecureCrypto: Crypto {
    override fun generateKey(alias: String)
    override fun encrypt(data: ByteArray): EncryptResult
    override fun decrypt(data: ByteArray, iv: ByteArray): ByteArray
}

class EncryptResult(val iv: ByteArray, val data: ByteArray)
