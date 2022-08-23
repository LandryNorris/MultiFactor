package io.github.landrynorris.password.generator

const val lowercase = "abcdefghijklmnopqrstuvwxyz"
const val uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
const val digits = "0123456789"
const val special = """`~!@#$%^&*()-_=+[]{}\|;:"',./<>?"""
const val similar = "Il|b6C(gq9G6O0S5"

class PasswordGenerator {

    fun generate(config: PasswordConfig): String {
        var validChars = lowercase + uppercase
        if(config.includeDigits) validChars += digits
        if(config.includeSpecial) validChars += special
        if(config.excludeSimilar) validChars = validChars.filter { it !in similar }

        return (0 until config.length).map { validChars.random() }.joinToString("")
    }
}