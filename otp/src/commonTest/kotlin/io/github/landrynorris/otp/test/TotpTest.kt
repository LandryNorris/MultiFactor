package io.github.landrynorris.otp.test

import io.github.landrynorris.otp.Totp
import kotlin.test.Test
import kotlin.test.assertEquals

class TotpTest {
    @Test
    fun testTotpProgress() {
        val secret = "abcdefg"
        val totp = Totp(secret, "A name")
        var time = 1234567L

        totp.setTime(time)
        assertEquals(0.23333333f, totp.progress)

        time++
        totp.setTime(time)
        assertEquals(0.26666668f, totp.progress)

        time++
        totp.setTime(time)
        assertEquals(0.3f, totp.progress)
    }

    @Test
    fun testTotpCode8Digit() {
        val secret = "abcdefg"
        val totp = Totp(secret, "A name")

        totp.setTime(123456789)
        assertEquals("006026", totp.generatePin())

        totp.setTime(234567890)
        assertEquals("934913", totp.generatePin())

        totp.setTime(0)
        assertEquals("042814", totp.generatePin())
    }
}