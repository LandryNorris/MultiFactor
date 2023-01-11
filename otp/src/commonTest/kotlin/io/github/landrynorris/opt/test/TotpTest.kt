package io.github.landrynorris.opt.test

import io.github.landrynorris.otp.Totp
import kotlin.test.Test
import kotlin.test.assertEquals

class TotpTest {
    private val otp = Totp("12345678901234567890", "test", codeLength = 8)

    @Test
    fun testTotp59() {
        otp.setTime(59)

        assertEquals("94287082", otp.generatePin())
    }

    @Test
    fun testTotp1111111109() {
        otp.setTime(1111111109)

        assertEquals("07081804", otp.generatePin())
    }

    @Test
    fun testTotp1234567890() {
        otp.setTime(1234567890)

        assertEquals("89005924", otp.generatePin())
    }

    @Test
    fun testTotp2000000000() {
        otp.setTime(2000000000)

        assertEquals("69279037", otp.generatePin())
    }
}
