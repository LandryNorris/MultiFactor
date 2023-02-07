package io.github.landrynorris.otp

/**
 * Implementation of RFC 4226 HMAC-Based One-Time Password.
 * Uses a [counter] value as the challenge.
 */
data class Hotp(override val secret: ByteArray, override val name: String,
                val counter: Long, override val codeLength: Int = 6):
    Otp(secret, name, codeLength) {

    /**
     * Same as primary constructor, but takes [secret] as a Base32 encoded String.
     */
    constructor(secret: String, name: String, counter: Long, codeLength: Int = 6):
            this(Base32.decode(secret), name, counter, codeLength)

    /**
     * Converts the value of [counter] to a challenge.
     */
    override fun getValue(): ByteArray {
        return counter.toBytes()
    }

    /**
     * Return a new value with incremented [counter]
     */
    fun incrementCounter(): Hotp {
        return copy(counter = counter + 1)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Hotp

        if (!secret.contentEquals(other.secret)) return false
        if (name != other.name) return false
        if (counter != other.counter) return false
        if (codeLength != other.codeLength) return false

        return true
    }

    override fun hashCode(): Int {
        var result = secret.contentHashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + counter.hashCode()
        result = 31 * result + codeLength
        return result
    }
}
