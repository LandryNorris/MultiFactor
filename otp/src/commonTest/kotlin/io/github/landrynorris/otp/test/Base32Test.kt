package io.github.landrynorris.otp.test

import io.github.landrynorris.otp.Base32
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContentEquals

class Base32Test {
    @Test
    fun testReplaceSimilar() {
        val encoded = "abcdefghijklmnop"
        val encodedWithSpaces = "a bcd efghi  jklm nop"
        val encodedReplaceIWith1 = "abcdefgh1jklmnop"
        val encodedReplaceOWith0 = "abcdefghijklmn0p"
        val encodedWithDashes = "abc-de-fghij--klm---nop"

        assertContentEquals(Base32.decode(encoded), Base32.decode(encodedWithSpaces))
        assertContentEquals(Base32.decode(encoded), Base32.decode(encodedReplaceIWith1))
        assertContentEquals(Base32.decode(encoded), Base32.decode(encodedReplaceOWith0))
        assertContentEquals(Base32.decode(encoded), Base32.decode(encodedWithDashes))
    }

    @Test
    fun testEncodeAndDecode() {
        val data = Random.Default.nextBytes(1000)
        val encoded = Base32.encode(data)
        val decoded = Base32.decode(encoded)

        assertContentEquals(data, decoded)
    }
}