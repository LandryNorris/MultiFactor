package io.github.landrynorris.encryption

actual object SecureCrypto {
    actual fun generateKey(alias: String) = Unit

    actual fun encrypt(data: ByteArray): EncryptResult {
        TODO("Not yet implemented")
    }

    actual fun decrypt(data: ByteArray, iv: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }
}
