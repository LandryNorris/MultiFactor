package io.github.landrynorris.encryption

import io.github.landrynorris.encryption.swift.Attributes
import kotlinx.cinterop.*
import platform.CoreFoundation.*
import platform.Foundation.CFBridgingRelease
import platform.Foundation.NSString
import platform.Security.*
import platform.darwin.OSStatus

@OptIn(ExperimentalForeignApi::class)
actual object SecureCrypto: Crypto {
    private val iv = ByteArray(16) { 0 }
    private val algorithm = kSecKeyAlgorithmECIESEncryptionCofactorVariableIVX963SHA256AESGCM

    actual override fun generateKey(alias: String): Unit = memScoped {
        val access = SecAccessControlCreateWithFlags(kCFAllocatorDefault,
            kSecAttrAccessibleWhenUnlockedThisDeviceOnly?.reinterpret(),
            kSecAccessControlPrivateKeyUsage, null)

        val error = alloc<CFErrorRefVar>()

        println("Generating key")
        val props = Attributes.keyAttributes(access, alias)
        SecKeyCreateRandomKey(props, error.ptr)

        if(error.value != null) {
            val errorText = error.value?.errorString()
            val code = CFErrorGetCode(error.value)
            println("Error Description is $errorText, code is $code")
        }
    }

    private fun getKey(alias: String): SecKeyRef {
        var initialKey = loadKey(alias)
        if(initialKey == null) {
            generateKey(alias)
            initialKey = loadKey(alias)
        }
        return initialKey ?: error("Unable to generate key")
    }

    private fun loadKey(alias: String): SecKeyRef? = memScoped {
        println("Loading key")
        val query = Attributes.keyQuery(alias)

        val item = alloc<CFArrayRefVar>()
        val result = SecItemCopyMatching(query, item.ptr.reinterpret())

        if(result != 0) println("Query result is ${result.errorString() ?: "success"}")
        val v = item.value
        return v?.reinterpret()
    }

    private fun OSStatus.errorString(): String? {
        if(this == 0) return null
        val cfMessage = SecCopyErrorMessageString(this, null)
        val nsMessage = CFBridgingRelease(cfMessage) as? NSString
        return nsMessage as? String
    }

    actual override fun encrypt(data: ByteArray, alias: String): EncryptResult {
        val key = getKey(alias)
        val publicKey = SecKeyCopyPublicKey(key) ?: error("No public key found")
        if(!checkCanEncrypt(publicKey)) error("Algorithm is not supported")

        val encrypted = memScoped {
            val error = alloc<CFErrorRefVar>()
            val ref = data.refTo(0).getPointer(this)

            val cfData = CFDataCreate(kCFAllocatorDefault, ref.reinterpret(), data.size.toLong())
            val cipherData = SecKeyCreateEncryptedData(publicKey, algorithm, cfData, error.ptr)

            if(error.value != null) {
                println("Result is ${error.value?.errorString()}")
            }

            val length = CFDataGetLength(cipherData)
            CFDataGetBytePtr(cipherData)?.readBytes(length.toInt())
        } ?: error("Unable to encrypt data")

        return EncryptResult(iv, encrypted)
    }

    actual override fun decrypt(data: ByteArray, iv: ByteArray, alias: String): ByteArray {
        val key = getKey(alias)
        if(!checkCanDecrypt(key)) error("Algorithm is not supported")

        return memScoped {
            val error = alloc<CFErrorRefVar>()
            val ref = data.refTo(0).getPointer(this)

            val cfData = CFDataCreate(kCFAllocatorDefault, ref.reinterpret(), data.size.toLong())
            val cipherData = SecKeyCreateDecryptedData(key, algorithm, cfData, error.ptr)

            val length = CFDataGetLength(cipherData)
            return@memScoped CFDataGetBytePtr(cipherData)?.readBytes(length.toInt())
        } ?: error("Unable to decrypt data")
    }

    private fun checkCanEncrypt(key: SecKeyRef): Boolean {
        return SecKeyIsAlgorithmSupported(key, kSecKeyOperationTypeEncrypt, algorithm)
    }

    private fun checkCanDecrypt(key: SecKeyRef): Boolean {
        return SecKeyIsAlgorithmSupported(key, kSecKeyOperationTypeDecrypt, algorithm)
    }

    private fun CFErrorRef.errorString(): String {
        val nsErrorText = CFBridgingRelease(CFErrorCopyDescription(this)) as NSString
        return nsErrorText as String
    }
}
