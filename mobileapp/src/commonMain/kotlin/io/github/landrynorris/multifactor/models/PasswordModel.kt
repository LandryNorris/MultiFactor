package io.github.landrynorris.multifactor.models

import io.github.landrynorris.database.PasswordEntry
import io.github.landrynorris.encryption.Crypto
import io.github.landrynorris.multifactor.NameKeystoreAlias

data class PasswordModel(val id: Long, val name: String, val salt: ByteArray,
                         val encryptedValue: ByteArray, val domain: String?, val appId: String?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PasswordModel

        if (id != other.id) return false
        if (name != other.name) return false
        if (!salt.contentEquals(other.salt)) return false
        if (!encryptedValue.contentEquals(other.encryptedValue)) return false
        if(domain != other.domain) return false
        if(appId != other.appId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + salt.contentHashCode()
        result = 31 * result + encryptedValue.contentHashCode()
        result = 31 * result + domain.hashCode()
        return result
    }
}

fun PasswordEntry.toModel(crypto: Crypto) = PasswordModel(id,
    name = crypto.decrypt(name, nameSalt, NameKeystoreAlias).decodeToString(),
    salt = passwordSalt,
    encryptedValue ?: byteArrayOf(), domain = domain?.decodeToString(), appId = appId)
