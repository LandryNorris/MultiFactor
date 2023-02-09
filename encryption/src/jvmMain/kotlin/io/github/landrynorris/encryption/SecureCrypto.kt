package io.github.landrynorris.encryption

import java.io.File
import java.security.KeyStore
import java.security.KeyStore.SecretKeyEntry
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

actual object SecureCrypto: Crypto {
    private const val CIPHER = "AES/GCM/NoPadding"
    private const val KEY_SIZE = 256
    private const val TAG_LENGTH = 128

    private val keystore: KeyStore by lazy {
        keyFile.parentFile.mkdirs()
        val result = KeyStore.getInstance(KeyStore.getDefaultType())
        if(keyFile.exists()) {
            result.load(keyFileStream, KEY_FILE_PASSWORD.toCharArray())
        } else {
            result.load(null)
        }
        result
    }

    actual override fun generateKey(alias: String) {
        println("Generating key since it doesn't exist")
        val generator = KeyGenerator.getInstance("AES")

        generator.init(KEY_SIZE)
        val key = generator.generateKey()

        val protection = KeyStore.PasswordProtection("A password".toCharArray())
        keystore.setEntry(alias, SecretKeyEntry(key), protection)

        keystore.store(keyFile.outputStream(), KEY_FILE_PASSWORD.toCharArray())
    }

    private fun getKey(alias: String): SecretKey {
        if(!keystore.isKeyEntry(alias)) generateKey(alias)
        val protection = KeyStore.PasswordProtection("A password".toCharArray())
        val entry = keystore.getEntry(alias, protection) as SecretKeyEntry
        return entry.secretKey
    }

    actual override fun encrypt(data: ByteArray, alias: String): EncryptResult {
        val key = getKey(alias)
        val cipher = Cipher.getInstance(CIPHER)
        cipher.init(Cipher.ENCRYPT_MODE, key)

        return EncryptResult(cipher.iv, cipher.doFinal(data))
    }

    actual override fun decrypt(data: ByteArray, iv: ByteArray, alias: String): ByteArray {
        val key = getKey(alias)
        val cipher = Cipher.getInstance(CIPHER)
        val params = GCMParameterSpec(TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, key, params)

        return cipher.doFinal(data)
    }

    private val keyFile: File get() {
        val home = System.getProperty("user.home")

        return File(home, ".multifactor/keystore/keystore.pkcs12")
    }

    private val keyFileStream get() = keyFile.inputStream()
}

private const val KEY_FILE_PASSWORD = "changeit"
