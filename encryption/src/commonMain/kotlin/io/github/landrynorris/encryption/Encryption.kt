package io.github.landrynorris.encryption

import com.soywiz.krypto.AES
import com.soywiz.krypto.Padding

object Encryption {
    fun encrypt(data: ByteArray, salt: ByteArray, key: ByteArray) =
        AES.encryptAes128Cbc(data = data, iv = salt, key = key, padding = Padding.ZeroPadding)

    fun decrypt(data: ByteArray, salt: ByteArray, key: ByteArray) =
        AES.decryptAes128Cbc(data = data, iv = salt, key = key, padding = Padding.ZeroPadding)
}
