package io.github.landrynorris.encryption

import korlibs.crypto.AES
import korlibs.crypto.Padding

object Encryption {
    fun encrypt(data: ByteArray, salt: ByteArray, key: ByteArray) =
        AES.encryptAesCbc(data = data, iv = salt, key = key, padding = Padding.ZeroPadding)

    fun decrypt(data: ByteArray, salt: ByteArray, key: ByteArray) =
        AES.decryptAesCbc(data = data, iv = salt, key = key, padding = Padding.ZeroPadding)
}
