package io.github.landrynorris.encryption

expect object SecureCrypto: Crypto {
    override fun generateKey(alias: String)
    override fun encrypt(data: ByteArray, alias: String): EncryptResult
    override fun decrypt(data: ByteArray, iv: ByteArray, alias: String): ByteArray
}

class EncryptResult(val iv: ByteArray, val data: ByteArray)
