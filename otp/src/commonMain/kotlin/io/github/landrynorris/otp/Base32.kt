package io.github.landrynorris.otp

private const val table = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"
private val shift = table.length.countTrailingZeroBits()
private const val mask = table.length-1

/**
 * Base32 is a human-readable and case-insensitive encoding.
 * The valid characters are 'ABCDEFGHIJKLMNOPQRSTUVWXYZ234567'.
 * This implementation converts some characters to valid characters
 * that look similar during the [decode] process.
 */
object Base32 {

    /**
     * Decode a [String] to [ByteArray]. For the purposes of decoding,
     * several characters are converted to valid Base32 characters or ignored.
     *
     * | input     | result    |
     * |-----------|-----------|
     * | ' '       | ignored   |
     * | '1'       | 'I'       |
     * | '0'       | 'O'       |
     * | '-'       | ignored   |
     * | lowercase | uppercase |
     *
     */
    fun decode(encoded: String): ByteArray {
        val trimmed = encoded.trim()
            .replace(" ", "")
            .replace("1", "I")
            .replace("0", "O")
            .replace("-", "").uppercase()

        val length = trimmed.length
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

    /**
     * Encode a [ByteArray] as a Base32 [String]
     */
    fun encode(data: ByteArray): String {
        var result = ""
        var currentByte: Int
        var digit: Int
        var i = 0

        while(i < data.size) {
            currentByte = data[i++].toInt() and 255
            result += table[currentByte shr 3]
            digit = (currentByte and 7) shl 2

            if(i >= data.size) {
                result += table[digit]
                break
            }

            currentByte = data[i++].toInt() and 255
            result += table[digit or (currentByte shr 6)]
            result += table[(currentByte shr 1) and 31]
            digit = (currentByte and 1) shl 4

            if(i >= data.size) {
                result += table[digit]
                break
            }

            currentByte = data[i++].toInt() and 255
            result += table[digit or (currentByte shr 4)]
            digit = (currentByte and 15) shl 1
            if(i >= data.size) {
                result += table[digit]
                break
            }

            currentByte = data[i++].toInt() and 255
            result += table[digit or (currentByte shr 7)]
            result += table[(currentByte shr 2) and 31]
            digit = (currentByte and 3) shl 3
            if(i >= data.size) {
                result += table[digit]
                break
            }

            currentByte = data[i++].toInt() and 255
            result += table[digit or (currentByte shr 5)]
            result += table[currentByte and 31]
        }

        return result
    }
}
