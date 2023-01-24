package io.github.landrynorris.encryption

import java.security.KeyStore
import java.security.KeyStore.SecretKeyEntry
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

actual object SecureCrypto: Crypto {
    private const val ALIAS = "MultiFactorKeyStore"
    private const val CIPHER = "AES/GCM/NoPadding"
    private const val KEY_SIZE = 256
    private const val TAG_LENGTH = 128

    private val keystore: KeyStore by lazy {
        val result = KeyStore.getInstance(KeyStore.getDefaultType())
        result.load(null)
        result
    }

    actual override fun generateKey(alias: String) {
        val generator = KeyGenerator.getInstance(
            "AES"
        )

        generator.init(KEY_SIZE)
        generator.generateKey()
    }

    private fun getKey(): SecretKey {
        if(!keystore.isKeyEntry(ALIAS)) generateKey(ALIAS)
        val entry = keystore.getEntry(ALIAS, null) as SecretKeyEntry
        return entry.secretKey
    }

    actual override fun encrypt(data: ByteArray): EncryptResult {
        val key = getKey()
        val cipher = Cipher.getInstance(CIPHER)
        cipher.init(Cipher.ENCRYPT_MODE, key)

        return EncryptResult(cipher.iv, cipher.doFinal(data))
    }

    actual override fun decrypt(data: ByteArray, iv: ByteArray): ByteArray {
        val key = getKey()
        val cipher = Cipher.getInstance(CIPHER)
        val params = GCMParameterSpec(TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, key, params)

        return cipher.doFinal(data)
    }
}
