package io.github.landrynorris.encryption

import kotlinx.cinterop.*
import platform.CoreFoundation.*
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSNumber
import platform.Security.*

actual object SecureCrypto {
    private const val ALIAS = "MultiFactorKeyStore"
    private val algorithm = kSecKeyAlgorithmECIESEncryptionCofactorVariableIVX963SHA256AESGCM

    actual fun generateKey(alias: String) {
        println("Generating key")
        val access = SecAccessControlCreateWithFlags(kCFAllocatorDefault,
            kSecAttrAccessibleWhenUnlockedThisDeviceOnly,
            kSecAccessControlPrivateKeyUsage, null)

        val privateKeyAttr = CFDictionaryCreateMutable(null, 2,
            null, null)
        CFDictionaryAddValue(privateKeyAttr, kSecAttrIsPermanent, CFBridgingRetain(NSNumber(true)))
        CFDictionaryAddValue(privateKeyAttr, kSecAttrApplicationTag, CFBridgingRetain(alias.utf8))
        CFDictionaryAddValue(privateKeyAttr, kSecAttrAccessControl, CFBridgingRetain(access))

        val d = CFDictionaryCreateMutable(null, 4, null, null)
        CFDictionaryAddValue(d, kSecAttrKeyType, CFBridgingRetain(kSecAttrKeyTypeEC))
        CFDictionaryAddValue(d, kSecAttrKeySizeInBits, CFBridgingRetain(NSNumber(256)))
        CFDictionaryAddValue(d, kSecAttrTokenID, CFBridgingRetain(kSecAttrTokenIDSecureEnclave))
        CFDictionaryAddValue(d, kSecPrivateKeyAttrs, CFBridgingRetain(privateKeyAttr))

        val error = cValue<CFErrorRefVar>()

        println("Generating key")
        SecKeyCreateRandomKey(d, error)
    }

    private fun getKey(alias: String): SecKeyRef {
        if(loadKey(alias) == null) generateKey(alias)
        return loadKey(alias) ?: error("Unable to generate key")
    }

    private fun loadKey(alias: String): SecKeyRef? {
        val tag = alias.utf8

        val d = CFDictionaryCreateMutable(null, 4, null, null)
        CFDictionaryAddValue(d, kSecClass, kSecClassKey)
        CFDictionaryAddValue(d, kSecAttrApplicationTag, CFBridgingRetain(tag))
        CFDictionaryAddValue(d, kSecAttrKeyType, kSecAttrKeyTypeEC)
        CFDictionaryAddValue(d, kSecReturnRef, CFBridgingRetain(NSNumber(true)))

        memScoped {
            val item = alloc<CFTypeRefVar>()
            SecItemCopyMatching(d, item.ptr)

            println("Key is ${item.value}")
            val v = item.value
            return v?.reinterpret()
        }
    }

    actual fun encrypt(data: ByteArray): EncryptResult {
        val key = getKey(ALIAS)
        if(!checkCanEncrypt(key)) error("Algorithm is not supported")

        val error = cValue<CFErrorRefVar>()
        val encrypted = memScoped {
            val ref = data.refTo(0).getPointer(this)

            val cfData = CFDataCreate(kCFAllocatorDefault, ref.reinterpret(), data.size.toLong())
            val cipherData = SecKeyCreateEncryptedData(key, algorithm, cfData, error)

            val length = CFDataGetLength(cipherData)
            return@memScoped CFDataGetBytePtr(cipherData)?.readBytes(length.toInt())
        } ?: error("Unable to encrypt data")

        return EncryptResult(byteArrayOf(), encrypted)
    }

    actual fun decrypt(data: ByteArray, iv: ByteArray): ByteArray {
        val key = getKey(ALIAS)
        if(!checkCanEncrypt(key)) error("Algorithm is not supported")

        val error = cValue<CFErrorRefVar>()
        return memScoped {
            val ref = data.refTo(0).getPointer(this)

            val cfData = CFDataCreate(kCFAllocatorDefault, ref.reinterpret(), data.size.toLong())
            val cipherData = SecKeyCreateDecryptedData(key, algorithm, cfData, error)

            val length = CFDataGetLength(cipherData)
            return@memScoped CFDataGetBytePtr(cipherData)?.readBytes(length.toInt())
        } ?: error("Unable to encrypt data")
    }

    private fun checkCanEncrypt(key: SecKeyRef): Boolean {
        val publicKey = SecKeyCopyPublicKey(key)
        return SecKeyIsAlgorithmSupported(publicKey, kSecKeyOperationTypeEncrypt, algorithm)
    }
}
