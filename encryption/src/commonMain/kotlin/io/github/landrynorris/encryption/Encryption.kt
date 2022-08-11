package io.github.landrynorris.encryption

import com.soywiz.krypto.AES

object Encryption {
    fun encrypt(data: ByteArray, salt: ByteArray, key: ByteArray) =
        AES.encryptAes128Cbc(data = data, iv = salt, key = key)

    fun decrypt(data: ByteArray, salt: ByteArray, key: ByteArray) =
        AES.decryptAes128Cbc(data = data, iv = salt, key = key)
}
