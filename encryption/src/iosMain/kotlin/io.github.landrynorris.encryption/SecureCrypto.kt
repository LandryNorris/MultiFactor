package io.github.landrynorris.encryption

import kotlinx.cinterop.*
import platform.CoreFoundation.*
import platform.Foundation.*
import platform.Security.*
import platform.darwin.NSInteger
import platform.darwin.OSStatus

actual object SecureCrypto {
    private const val ALIAS = "MultiFactorKeyStore"
    private val iv = ByteArray(16) { 0 }
    private val algorithm = kSecKeyAlgorithmECIESEncryptionCofactorVariableIVX963SHA256AESGCM

    actual fun generateKey(alias: String): Unit = memScoped {
        val access = SecAccessControlCreateWithFlags(kCFAllocatorDefault,
            kSecAttrAccessibleWhenUnlockedThisDeviceOnly?.reinterpret(),
            kSecAccessControlPrivateKeyUsage, null)

        val aliasData = (alias as NSString).dataUsingEncoding(NSUTF8StringEncoding)

        val privateKeyProperties = cfDictionaryOf(
            mapOf(
                kSecAttrIsPermanent to kCFBooleanTrue,
                kSecAttrApplicationTag to CFBridgingRetain(aliasData),
                kSecAttrAccessControl to access
            )
        )

        val publicKeyProperties = cfDictionaryOf(
            mapOf(
                kSecAttrIsPermanent to kCFBooleanTrue,
                kSecAttrApplicationTag to CFBridgingRetain(aliasData),
                kSecAttrAccessControl to access
            )
        )

        val properties = cfDictionaryOf(
            mapOf(
                kSecAttrKeyType to kSecAttrKeyTypeEC,
                kSecAttrKeySizeInBits to CFBridgingRetain(NSNumber(256)),
                kSecAttrTokenID to kSecAttrTokenIDSecureEnclave,
                kSecPrivateKeyAttrs to privateKeyProperties,
                kSecPublicKeyAttrs to publicKeyProperties
            )
        )

        val error = alloc<CFErrorRefVar>()

        println("Generating key")
        SecKeyCreateRandomKey(properties, error.ptr)

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
        val aliasData = (alias as NSString).dataUsingEncoding(NSUTF8StringEncoding)

        val query = cfDictionaryOf(
            mapOf(
                kSecClass to kSecClassKey,
                kSecAttrApplicationTag to CFBridgingRetain(aliasData),
                kSecAttrKeyType to kSecAttrKeyTypeEC,
                kSecMatchLimit to kSecMatchLimitOne,
                kSecReturnRef to kCFBooleanTrue
            )
        )

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

    actual fun encrypt(data: ByteArray): EncryptResult {
        val key = getKey(ALIAS)
        val publicKey = SecKeyCopyPublicKey(key) ?: error("No public key found")
        if(!checkCanEncrypt(publicKey)) error("Algorithm is not supported")
        println("Starting to encrypt")

        val encrypted = memScoped {
            val error = alloc<CFErrorRefVar>()
            val ref = data.refTo(0).getPointer(this)

            val cfData = CFDataCreate(kCFAllocatorDefault, ref.reinterpret(), data.size.toLong())
            val cipherData = SecKeyCreateEncryptedData(publicKey, algorithm, cfData, error.ptr)

            println("Result is ${error.value?.errorString() ?: "success"}")

            val length = CFDataGetLength(cipherData)
            println("Length of data is $length")
            CFDataGetBytePtr(cipherData)?.readBytes(length.toInt())
        } ?: error("Unable to encrypt data")

        println("Encrypted data")

        return EncryptResult(iv, encrypted)
    }

    actual fun decrypt(data: ByteArray, iv: ByteArray): ByteArray {
        val key = getKey(ALIAS)
        if(!checkCanDecrypt(key)) error("Algorithm is not supported")
        println("Starting to decrypt")

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

internal inline fun MemScope.cfDictionaryOf(map: Map<CFStringRef?, CFTypeRef?>): CFDictionaryRef? {
    val size = map.size
    val keys = allocArrayOf(*map.keys.toTypedArray())
    val values = allocArrayOf(*map.values.toTypedArray())
    return CFDictionaryCreate(kCFAllocatorDefault,
        keys.reinterpret(), values.reinterpret(),
        size.convert(), null, null)
}
