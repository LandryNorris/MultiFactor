package io.github.landrynorris.encryption

expect object KeyStore {
    fun getKey(): ByteArray
}