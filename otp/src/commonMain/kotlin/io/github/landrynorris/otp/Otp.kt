package io.github.landrynorris.otp

import com.soywiz.krypto.HMAC
import kotlin.experimental.and

private val powersOfTen = listOf(0, 10, 100, 1_000, 10_000, 100_000, 1_000_000,
    10_000_000, 100_000_000, 1_000_000_000, 10_000_000_000, 100_000_000_000)

/**
 * Base class for One-Time Passwords.
 * The [secret] is a [ByteArray] holding the secret data.
 * The [name] is a name identifying this OTP. It is mainly meant for models, and can be empty
 * The [codeLength] sets the length of generated codes.
 */
sealed class Otp(open val secret: ByteArray, open val name: String, open val codeLength: Int = 6) {

    private fun hash(value: ByteArray): ByteArray {
        val hash = HMAC.hmacSHA1(secret, value)
        return hash.bytes
    }

    /**
     * Cache the previous pin until the challenge changes.
     * This optimization prevents needless recalculations.
     */
    private var cachedPin: PinCache? = null

    /**
     * Generate a pin using the [secret] and the result of [getValue].
     * This code will have a length of [codeLength] and is padded at the start with zeroes.
     */
    fun generatePin(): String {
        val challenge = getValue()

        if(challenge.contentEquals(cachedPin?.challenge)) return cachedPin!!.pin

        val hash = hash(challenge)
        val offset = hash.last().and(0x0F).toInt()

        val truncatedHash = hashToInt(hash, offset).and(0x7FFFFFFF)
        val fullPin = truncatedHash.mod(powersOfTen[codeLength])

        val pin = pad(fullPin, codeLength)

        cachedPin = PinCache(pin, challenge)
        return pin
    }

    /**
     * Pad the input with zeroes at the start.
     */
    private fun pad(pin: Long, length: Int): String {
        return buildString {
            val result = pin.toString()
            for(i in 0 until length-result.length) {
                append('0')
            }
            append(result)
        }
    }

    /**
     * Hash has MSB at bytes[0]
     */
    private fun hashToInt(bytes: ByteArray, start: Int): Int {
        val data = byteArrayOf(bytes[start], bytes[start+1], bytes[start+2], bytes[start+3])
            .map { it.toUByte().toInt() }
        return data[0].shl(24)
            .or(data[1].shl(16))
            .or(data[2].shl(8))
            .or(data[3])
    }

    /**
     * Get the current challenge for use in [generatePin]
     */
    abstract fun getValue(): ByteArray

    /**
     * Value of [secret], but encoded as a Base32 String
     */
    val secretBase32 get() = Base32.encode(secret)
}

class PinCache(val pin: String, val challenge: ByteArray)

sealed class OtpMethod {
    object HOTP: OtpMethod()
    object TOTP: OtpMethod()
}
