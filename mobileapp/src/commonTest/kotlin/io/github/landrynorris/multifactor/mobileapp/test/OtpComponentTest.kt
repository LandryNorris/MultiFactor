package io.github.landrynorris.multifactor.mobileapp.test

import io.github.landrynorris.database.AppDatabase
import io.github.landrynorris.multifactor.components.*
import io.github.landrynorris.multifactor.models.OtpModel
import io.github.landrynorris.multifactor.repository.OtpRepository
import io.github.landrynorris.otp.Hotp
import io.github.landrynorris.otp.OtpMethod
import io.github.landrynorris.otp.Totp
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OtpComponentTest {

    @Test
    fun testCreateOtp() = runBlocking {
        val component = createComponent()
        assertFalse(component.state.value.isAdding)
        val oldOtpList = component.state.value.otpList
        assertTrue(oldOtpList.isEmpty())

        component.addOtpPressed()

        assertTrue(component.state.value.isAdding)

        val testOtp = Totp("A secret", "A value")
        component.createOtpLogic.enter(OtpModel(-1L, testOtp))

        val otpState = component.awaitNonEmptyOtpList()
        val newOtpList = otpState.otpList
        assertEquals(1, newOtpList.size)
    }

    @Test
    fun testIsAddingState() {
        val component = createComponent()

        assertFalse(component.state.value.isAdding)
        component.addOtpPressed()
        assertTrue(component.state.value.isAdding)
        component.addOtpPressed()
        assertFalse(component.state.value.isAdding)
    }

    @Test
    fun testHotpCounter() = runBlocking {
        val component = createComponent()

        val testOtp = Hotp("A secret", "A value", 0)
        component.createOtpLogic.enter(OtpModel(-1L, testOtp))

        val codes = listOf("760185", "139820", "724745", "506633", "181744",
            "604248", "869305", "895970", "723575", "374912")

        repeat(10) {
            delay(100)
            val otpState = component.awaitNonEmptyOtpList()

            //val otpState = component.state.value
            assertEquals(1, otpState.otpList.size)

            val hotp = otpState.otpList.first()
            assertEquals(codes[it], hotp.pin)

            component.incrementClicked(0)
        }
    }

    private suspend fun OtpLogic.awaitNonEmptyOtpList(): OtpScreenState {
        return withTimeout(1000) {
            var otpState: OtpScreenState
            do {
                otpState = state.value
            } while(otpState.otpList.isEmpty())
            otpState
        }
    }

    private fun createOtpRepository(): OtpRepository {
        val driver = createInMemoryTestDriver()
        val database = AppDatabase(driver)
        return OtpRepository(database)
    }

    private fun createComponent(): OtpLogic {
        return OtpComponent(createContext(), createOtpRepository())
    }

    private fun CreateOtpLogic.enter(model: OtpModel) {
        methodChanged(if(model.otp is Totp) OtpMethod.TOTP else OtpMethod.HOTP)
        nameChanged(model.otp.name)
        secretChanged(model.otp.secret)
        confirm()
    }
}