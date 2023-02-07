package io.github.landrynorris.password.generator

/**
 * Configuration for the password generator
 */
data class PasswordConfig(
    /** Whether the password should include digits */
    var includeDigits: Boolean = true,
    /** Whether the password should include special characters */
    var includeSpecial: Boolean = true,
    /** Length of the generated password */
    var length: Int = 25,
    /** Whether to exclude similar characters */
    var excludeSimilar: Boolean = false)

/**
 * Convenience method that configures a [PasswordConfig]
 * and then returns a random password
 */
fun createPassword(configure: PasswordConfig.() -> Unit): String {
    val config = PasswordConfig()
    config.configure()

    return PasswordGenerator().generate(config)
}
