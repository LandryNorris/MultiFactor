package io.github.landrynorris.encryption

import dev.whyoleg.cryptography.BinarySize
import dev.whyoleg.cryptography.BinarySize.Companion.bytes
import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.DelicateCryptographyApi
import dev.whyoleg.cryptography.algorithms.AES
import dev.whyoleg.cryptography.algorithms.PBKDF2
import dev.whyoleg.cryptography.algorithms.SHA256
import kotlin.random.Random

@OptIn(DelicateCryptographyApi::class)
object SoftwareCrypto {

    private const val KEY_SIZE_BYTES = 32
    private const val IV_SIZE_BYTES = 12
    private const val PBKDF2_ITERATIONS = 600_000

    private val aes = CryptographyProvider.Default.get(AES.GCM)
    private val pbkdf2 =
        CryptographyProvider.Default.get(PBKDF2)

    suspend fun encrypt(
        data: ByteArray,
        key: ByteArray,
    ): EncryptResult {
        require(key.size == KEY_SIZE_BYTES) {
            "AES-256 keys must contain exactly 32 bytes"
        }

        val aesKey = aes.keyDecoder().decodeFromByteArray(AES.Key.Format.RAW, key)

        // TODO(Landry): Make randomization more secure
        val iv = ByteArray(IV_SIZE_BYTES)
        Random.nextBytes(iv)

        val ciphertext = aesKey.cipher()
            .encryptWithIv(iv, data)

        return EncryptResult(
            iv = iv,
            data = ciphertext,
        )
    }

    suspend fun decrypt(
        data: ByteArray,
        iv: ByteArray,
        key: ByteArray,
    ): ByteArray {
        require(key.size == KEY_SIZE_BYTES) {
            "AES-256 keys must contain exactly 32 bytes"
        }

        require(iv.size == IV_SIZE_BYTES) {
            "AES-GCM IV must contain exactly 12 bytes"
        }

        val aesKey = aes.keyDecoder().decodeFromByteArray(AES.Key.Format.RAW, key)

        return aesKey.cipher()
            .decryptWithIv(iv, data)
    }

    fun deriveKey(
        password: ByteArray,
        salt: ByteArray,
    ): ByteArray {
        val derivation = pbkdf2.secretDerivation(
            digest = SHA256,
            iterations = PBKDF2_ITERATIONS,
            outputSize = KEY_SIZE_BYTES.bytes,
            salt = salt,
        )

        return derivation
            .deriveSecretBlocking(password)
            .toByteArray()
    }
}