package io.github.landrynorris.password.generator.test

import io.github.landrynorris.password.generator.createPassword
import io.github.landrynorris.password.generator.digits
import io.github.landrynorris.password.generator.similar
import io.github.landrynorris.password.generator.special
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class PasswordGeneratorTest {
    @Test
    fun testLength() {
        assertEquals(10, createPassword {
            length = 10
        }.length)

        assertEquals(25, createPassword {
            length = 25
        }.length)

        assertEquals(100, createPassword {
            length = 100
        }.length)
    }

    @Test
    fun testExcludeSpecialCharacters() {
        repeat(20) {
            val generated = createPassword {
                length = 25
                includeSpecial = false
            }

            assertFalse(special.any { it in generated })
        }
    }

    @Test
    fun testExcludeDigits() {
        repeat(20) {
            val generated = createPassword {
                length = 25
                includeDigits = false
            }

            assertFalse(digits.any { it in generated })
        }
    }

    @Test
    fun testExcludeSimilar() {
        repeat(20) {
            val generated = createPassword {
                length = 25
                excludeSimilar = true
            }

            assertFalse(similar.any { it in generated })
        }
    }
}