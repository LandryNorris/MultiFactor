package io.github.landrynorris.encryption

import kotlinx.cinterop.*
import platform.CoreFoundation.*
import platform.Foundation.*
import platform.Security.*
import platform.darwin.NSInteger
import platform.darwin.OSStatus

actual object SecureCrypto {
    private const val ALIAS = "MultiFactorKeyStore"
    private val algorithm = kSecKeyAlgorithmECIESEncryptionCofactorVariableIVX963SHA256AESGCM

    actual fun generateKey(alias: String): Unit = memScoped {
        val access = SecAccessControlCreateWithFlags(kCFAllocatorDefault,
            kSecAttrAccessibleWhenUnlockedThisDeviceOnly?.reinterpret(),
            kSecAccessControlPrivateKeyUsage, null)

        val aliasData = (alias as NSString).dataUsingEncoding(NSUTF8StringEncoding)
        val privateKeyAttr = CFDictionaryCreateMutable(null, 3,
            null, null)
        CFDictionaryAddValue(privateKeyAttr, kSecAttrIsPermanent, kCFBooleanTrue)

        CFDictionaryAddValue(privateKeyAttr, kSecAttrApplicationTag, CFBridgingRetain(aliasData))
        CFDictionaryAddValue(privateKeyAttr, kSecAttrAccessControl, access)

        val d = CFDictionaryCreateMutable(null, 4, null, null)
        CFDictionaryAddValue(d, kSecAttrKeyType, kSecAttrKeyTypeEC)
        CFDictionaryAddValue(d, kSecAttrKeySizeInBits, CFBridgingRetain(NSNumber(256)))
        //CFDictionaryAddValue(d, kSecAttrTokenID, kSecAttrTokenIDSecureEnclave)
        CFDictionaryAddValue(d, kSecPrivateKeyAttrs, privateKeyAttr)

        val error = alloc<CFErrorRefVar>()

        println("Generating key")
        SecKeyCreateRandomKey(d, error.ptr)

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

        val d = CFDictionaryCreateMutable(null, 4, null, null)
        CFDictionaryAddValue(d, kSecClass, kSecClassKey)
        CFDictionaryAddValue(d, kSecAttrApplicationTag, CFBridgingRetain(aliasData))
        CFDictionaryAddValue(d, kSecAttrKeyType, kSecAttrKeyTypeEC)
        CFDictionaryAddValue(d, kSecMatchLimit, kSecMatchLimitOne)
        CFDictionaryAddValue(d, kSecReturnRef, kCFBooleanTrue)

        val item = alloc<CFArrayRefVar>()
        val result = SecItemCopyMatching(d, item.ptr.reinterpret())

        println("Query result is ${result.errorString() ?: "success"}")
        println("Key is ${item.value}")
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
        println("Got key for encryption")
        if(!checkCanEncrypt(key)) error("Algorithm is not supported")
        println("Starting to encrypt")

        val publicKey = SecKeyCopyPublicKey(key)
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

        return EncryptResult(byteArrayOf(), encrypted)
    }

    actual fun decrypt(data: ByteArray, iv: ByteArray): ByteArray {
        val key = getKey(ALIAS)
        if(!checkCanEncrypt(key)) error("Algorithm is not supported")

        val publicKey = SecKeyCopyPublicKey(key)
        val error = cValue<CFErrorRefVar>()
        return memScoped {
            val ref = data.refTo(0).getPointer(this)

            val cfData = CFDataCreate(kCFAllocatorDefault, ref.reinterpret(), data.size.toLong())
            val cipherData = SecKeyCreateDecryptedData(publicKey, algorithm, cfData, error)

            val length = CFDataGetLength(cipherData)
            return@memScoped CFDataGetBytePtr(cipherData)?.readBytes(length.toInt())
        } ?: error("Unable to encrypt data")
    }

    private fun checkCanEncrypt(key: SecKeyRef): Boolean {
        val publicKey = SecKeyCopyPublicKey(key)
        println("Got public key")
        return SecKeyIsAlgorithmSupported(publicKey, kSecKeyOperationTypeEncrypt, algorithm)
    }

    private fun CFErrorRef.errorString(): String {
        val nsErrorText = CFBridgingRelease(CFErrorCopyDescription(this)) as NSString
        return nsErrorText as String
    }
}

internal inline fun MemScope.cfDictionaryOf(vararg items: Pair<CFStringRef?, CFTypeRef?>): CFDictionaryRef? =
    cfDictionaryOf(mapOf(*items))

internal inline fun MemScope.cfDictionaryOf(map: Map<CFStringRef?, CFTypeRef?>): CFDictionaryRef? {
    val size = map.size
    val keys = allocArrayOf(*map.keys.toTypedArray())
    val values = allocArrayOf(*map.values.toTypedArray())
    return CFDictionaryCreate(
        kCFAllocatorDefault,
        keys.reinterpret(),
        values.reinterpret(),
        size.convert(),
        null,
        null
    )
}
