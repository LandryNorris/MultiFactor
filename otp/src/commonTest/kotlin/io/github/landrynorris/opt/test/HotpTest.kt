package io.github.landrynorris.opt.test

import io.github.landrynorris.otp.Hotp
import kotlin.test.Test
import kotlin.test.assertEquals

class HotpTest {
    @Test
    fun testHotpCounter() {
        var otp = Hotp("abcdefg", "test", 0L, codeLength = 6)
        val values = listOf("042814", "431679", "922982", "480228", "358636")

        values.forEach { value ->
            assertEquals(value, otp.generatePin())
            otp = otp.incrementCounter()
        }
    }
}
