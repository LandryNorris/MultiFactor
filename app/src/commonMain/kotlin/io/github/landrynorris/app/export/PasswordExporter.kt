package io.github.landrynorris.app.export

import io.github.landrynorris.app.PasswordKeystoreAlias
import io.github.landrynorris.app.models.PasswordModel
import io.github.landrynorris.app.repository.OtpRepository
import io.github.landrynorris.app.repository.PasswordRepository
import io.github.landrynorris.encryption.SecureCrypto
import io.github.landrynorris.encryption.SoftwareCrypto
import io.github.landrynorris.otp.Otp
import kotlin.io.encoding.Base64
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class PasswordExporter(
    private val passwordRepository: PasswordRepository,
    private val otpRepository: OtpRepository,
) {
    private val crypto = SoftwareCrypto
    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    suspend fun createExportModel(
        encryptionPassword: ByteArray,
        key: ByteArray,
        salt: ByteArray,
    ): ExportedPasswordsFile {
        val passwordModelList = buildList {
            loadPasswords { model, passwordBytes ->
                add(toExportedPassword(model, passwordBytes, key))
            }
        }
        val otpModelList = buildList { loadOtp { otp -> add(toExportedOtp(otp, key)) } }
        return ExportedPasswordsFile(
            header = createHeader(salt),
            contents =
                ExportedPasswordsContents(
                    passwordModelList = passwordModelList,
                    otpModelList = otpModelList,
                ),
        )
    }

    suspend fun createExportFileContents(encryptionPassword: ByteArray): String {
        val salt = crypto.generateSalt(16)
        val key = crypto.deriveKey(encryptionPassword, salt)

        try {
            val model = createExportModel(encryptionPassword, key, salt)
            val modelStringContents = json.encodeToString(model)
            val modelBytes = modelStringContents.encodeToByteArray()
            val encryptedModelContents = crypto.encrypt(modelBytes, key)
            val modelStringLength = modelStringContents.length

            return buildString {
                appendLine("MultiFactor Export")
                appendLine(modelStringLength)
                appendLine("---")
                appendLine(modelStringContents)
                appendLine("---")
                appendLine(Base64.encode(encryptedModelContents.data))
                appendLine(Base64.encode(encryptedModelContents.iv))
            }
        } finally {
            key.fill(0)
        }
    }

    private suspend fun toExportedOtp(otp: Otp, key: ByteArray): ExportedOtp {
        val secretResult = crypto.encrypt(otp.secret, key)
        val nameResult = crypto.encrypt(otp.name.encodeToByteArray(), key)
        return ExportedOtp(
            secretBase64 = Base64.encode(secretResult.data),
            secretIvBase64 = Base64.encode(secretResult.iv),
            nameBase64 = Base64.encode(nameResult.data),
            nameIvBase64 = Base64.encode(nameResult.iv),
        )
    }

    private suspend fun toExportedPassword(
        model: PasswordModel,
        passwordBytes: ByteArray,
        key: ByteArray,
    ): ExportedPassword {
        val domainResult = crypto.encrypt(model.domain?.encodeToByteArray() ?: byteArrayOf(), key)
        val appIdResult = crypto.encrypt(model.appId?.encodeToByteArray() ?: byteArrayOf(), key)
        val passwordResult = crypto.encrypt(passwordBytes, key)
        return ExportedPassword(
            domainBase64 = Base64.encode(domainResult.data),
            domainIvBase64 = Base64.encode(domainResult.iv),
            appIdBase64 = Base64.encode(appIdResult.data),
            appIdIvBase64 = Base64.encode(appIdResult.iv),
            passwordBase64 = Base64.encode(passwordResult.data),
            passwordIvBase64 = Base64.encode(passwordResult.iv),
        )
    }

    private fun createHeader(salt: ByteArray): ExportedHeader {
        val info = crypto.getCryptographyInformation()
        return ExportedHeader(
            saltBase64 = Base64.encode(salt),
            iterations = info.iterations,
            keySizeBytes = info.keySizeBytes,
            kdf = info.name,
        )
    }

    private suspend fun loadPasswords(
        onPasswordDecrypted: suspend (PasswordModel, ByteArray) -> Unit
    ) {
        val passwords = passwordRepository.getPasswordsFlow().firstOrNull() ?: return

        passwords.forEach { passwordModel ->
            val decryptedPassword =
                SecureCrypto.decrypt(
                    passwordModel.encryptedValue,
                    passwordModel.salt,
                    PasswordKeystoreAlias,
                )

            onPasswordDecrypted(passwordModel, decryptedPassword)
            decryptedPassword.fill(0)
        }
    }

    private suspend fun loadOtp(onOtpDecrypted: suspend (Otp) -> Unit) {
        val otpList = otpRepository.getOtpModelFlow().firstOrNull() ?: return

        otpList.forEach { otpModel -> onOtpDecrypted(otpModel.otp) }
    }
}

@Serializable
data class ExportedPasswordsFile(
    val header: ExportedHeader,
    val contents: ExportedPasswordsContents,
)

@Serializable
data class ExportedPasswordsContents(
    val passwordModelList: List<ExportedPassword>,
    val otpModelList: List<ExportedOtp>,
)

@Serializable
data class ExportedHeader(
    val version: Int = 1,
    val kdf: String,
    val iterations: Int,
    val keySizeBytes: Int,
    val saltBase64: String,
)

@Serializable
data class ExportedPassword(
    val domainBase64: String,
    val domainIvBase64: String,
    val appIdBase64: String,
    val appIdIvBase64: String,
    val passwordBase64: String,
    val passwordIvBase64: String,
)

@Serializable
data class ExportedOtp(
    val secretBase64: String,
    val secretIvBase64: String,
    val nameBase64: String,
    val nameIvBase64: String,
)
