package io.github.landrynorris.password.generator

data class PasswordConfig(var includeDigits: Boolean = true,
                          var includeSpecial: Boolean = true,
                          var length: Int = 25,
                          var excludeSimilar: Boolean = false)

fun createPassword(configure: PasswordConfig.() -> Unit): String {
    val config = PasswordConfig()
    config.configure()

    return PasswordGenerator().generate(config)
}
