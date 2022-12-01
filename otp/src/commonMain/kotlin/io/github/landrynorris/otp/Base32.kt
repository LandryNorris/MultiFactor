package io.github.landrynorris.otp

private const val table = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"
private val shift = table.length.countTrailingZeroBits()
private val mask = table.length-1

object Base32 {

    fun decode(encoded: String): ByteArray {
        val trimmed = encoded.trim()
            .replace(" ", "")
            .replace("-", "").uppercase()

        val length = encoded.length
        val outLength = length * shift/8
        val result = ByteArray(outLength)

        var next = 0
        var buffer = 0
        var bitsLeft = 0

        for(c in trimmed) {
            if(!table.contains(c)) error("Illegal Character: $c")
            buffer = buffer shl shift
            buffer = buffer or (table.indexOf(c) and mask)
            bitsLeft += shift

            if(bitsLeft >= 8) {
                result[next++] = (buffer shr (bitsLeft-8)).toByte()
                bitsLeft -= 8
            }
        }

        return result
    }
}
