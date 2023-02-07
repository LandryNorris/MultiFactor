package io.github.landrynorris.otp

/**
 * Convert a [Long] into a [ByteArray] of size 8. The MSB is first, and the LSB is last.
 */
fun Long.toBytes(): ByteArray {
    return byteArrayOf(
        this.and(0xFF.toLong().shl(56)).shr(56).toByte(),
        this.and(0xFF.toLong().shl(48)).shr(48).toByte(),
        this.and(0xFF.toLong().shl(40)).shr(40).toByte(),
        this.and(0xFF.toLong().shl(32)).shr(32).toByte(),
        this.and(0xFF.toLong().shl(24)).shr(24).toByte(),
        this.and(0xFF.toLong().shl(16)).shr(16).toByte(),
        this.and(0xFF.toLong().shl(8)).shr(8).toByte(),
        this.and(0xFF.toLong()).toByte(),
    )
}