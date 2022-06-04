package io.github.landrynorris.otp

data class Hotp(override val secret: String, override val name: String,
                val counter: Long, override val codeLength: Int = 6):
    Otp(secret, name, codeLength) {

    override fun getValue(): ByteArray {
        return counter.toBytes()
    }

    fun incrementCounter(): Hotp {
        return copy(counter = counter + 1)
    }
}
