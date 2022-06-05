package io.github.landrynorris.multifactor.models

import io.github.landrynorris.multifactor.PasswordEntry

data class PasswordModel(val id: Long, val name: String, val hash: ByteArray, val encryptdValue: ByteArray)

fun PasswordEntry.toModel() = PasswordModel(id, name, hash ?: byteArrayOf(),
    encryptedValue ?: byteArrayOf())
