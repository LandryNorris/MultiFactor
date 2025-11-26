package io.github.landrynorris.encryption

import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.DelicateCryptographyApi
import dev.whyoleg.cryptography.algorithms.AES

object Encryption {
    val crypto = CryptographyProvider.Default.get(AES.CBC)
    val keyGenerator = crypto.keyDecoder()
    @OptIn(DelicateCryptographyApi::class)
    fun encrypt(data: ByteArray, salt: ByteArray, key: ByteArray): ByteArray {
        val decodedKey = keyGenerator.decodeFromByteArrayBlocking(AES.Key.Format.RAW, key)
        val cipher = decodedKey.cipher(true)

        return cipher.encryptWithIvBlocking(salt, data)
    }

    @OptIn(DelicateCryptographyApi::class)
    fun decrypt(data: ByteArray, salt: ByteArray, key: ByteArray): ByteArray {
        val decodedKey = keyGenerator.decodeFromByteArrayBlocking(AES.Key.Format.RAW, key)
        val cipher = decodedKey.cipher(true)

        return cipher.decryptWithIvBlocking(salt, data)
    }
}
