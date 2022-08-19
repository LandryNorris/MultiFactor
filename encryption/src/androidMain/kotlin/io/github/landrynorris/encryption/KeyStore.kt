package io.github.landrynorris.encryption

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore.SecretKeyEntry
import java.security.KeyStore as AndroidKeyStore
import javax.crypto.SecretKey
import javax.crypto.KeyGenerator as AndroidKeyGenerator

actual object KeyStore {
    private const val ANDROID_KEY_STORE = "AndroidKeyStore"
    private const val alias = ""

    actual fun getKey(): ByteArray {
        val keyStore = AndroidKeyStore.getInstance(ANDROID_KEY_STORE)
        keyStore.load(null)

        if(!keyStore.isKeyEntry(alias)) {
            generateKey()
        }

        val secretEntry: SecretKeyEntry = keyStore.getEntry(alias, null) as SecretKeyEntry
        return secretEntry.secretKey.encoded
    }

    private fun generateKey(): ByteArray {
        val generator = AndroidKeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEY_STORE)

        val keySpec = KeyGenParameterSpec.Builder(alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setKeySize(128)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        generator.init(keySpec)

        return generator.generateKey().encoded
    }
}
