package io.github.landrynorris.password.generator

/** List of lowercase characters */
const val lowercase = "abcdefghijklmnopqrstuvwxyz"
/** List of uppercase characters */
const val uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
/** List of digits */
const val digits = "0123456789"
/** List of special characters */
const val special = """`~!@#$%^&*()-_=+[]{}\|;:"',./<>?"""
/** List of similar characters */
const val similar = "Il|b6C(gq9G6O0S5"

class PasswordGenerator {

    /**
     * Generate a random password conforming to the given [config]
     */
    fun generate(config: PasswordConfig): String {
        var validChars = lowercase + uppercase
        if(config.includeDigits) validChars += digits
        if(config.includeSpecial) validChars += special
        if(config.excludeSimilar) validChars = validChars.filter { it !in similar }

        return (0 until config.length).map { validChars.random() }.joinToString("")
    }
}