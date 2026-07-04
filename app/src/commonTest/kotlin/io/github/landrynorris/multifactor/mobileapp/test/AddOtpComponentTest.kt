package io.github.landrynorris.multifactor.mobileapp.test

import io.github.landrynorris.multifactor.components.CreateOtpComponent
import io.github.landrynorris.multifactor.components.CreateOtpLogic
import io.github.landrynorris.multifactor.models.OtpModel
import io.github.landrynorris.otp.Hotp
import io.github.landrynorris.otp.Otp
import io.github.landrynorris.otp.OtpMethod
import io.github.landrynorris.otp.Totp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AddOtpComponentTest {

    @Test
    fun testTypeName() {
        val component = createComponent()

        assertEquals("", component.state.value.name)
        assertEquals("", component.state.value.secret)

        component.nameChanged("A")
        assertEquals("A", component.state.value.name)
        assertEquals("", component.state.value.secret)

        component.nameChanged("A value")
        assertEquals("A value", component.state.value.name)
        assertEquals("", component.state.value.secret)
    }

    @Test
    fun testTypeSecret() {
        val component = createComponent()

        assertEquals("", component.state.value.name)
        assertEquals("", component.state.value.secret)

        component.secretChanged("A")
        assertEquals("A", component.state.value.secret)
        assertEquals("", component.state.value.name)

        component.secretChanged("A secret value")
        assertEquals("A secret value", component.state.value.secret)
        assertEquals("", component.state.value.name)
    }

    @Test
    fun testChangeMethod() {
        val component = createComponent()

        component.methodChanged(OtpMethod.TOTP)
        assertEquals(OtpMethod.TOTP, component.state.value.type)

        component.methodChanged(OtpMethod.TOTP)
        assertEquals(OtpMethod.TOTP, component.state.value.type)

        component.methodChanged(OtpMethod.HOTP)
        assertEquals(OtpMethod.HOTP, component.state.value.type)
    }

    @Test
    fun testCreateOtpEntry() {
        var lastCreatedOtp: OtpModel? = null
        val component = createComponent { lastCreatedOtp = it }

        component.nameChanged("A name")
        component.secretChanged("A secret value")
        component.methodChanged(OtpMethod.HOTP)

        assertNull(lastCreatedOtp)
        component.confirm()

        val hotpModel = OtpModel(-1, Hotp("A secret value", "A name", 0))
        assertEquals(hotpModel, lastCreatedOtp)

        assertEquals("", component.state.value.name)
        assertEquals("", component.state.value.secret)

        component.nameChanged("Another name")
        component.secretChanged("Another secret value")
        component.methodChanged(OtpMethod.TOTP)
        component.confirm()

        val totpModel = OtpModel(-1, Totp("Another secret value", "Another name"))
        assertEquals(totpModel, lastCreatedOtp)
    }

    private fun createComponent(onOtpCreated: (OtpModel) -> Unit = {}): CreateOtpLogic {
        return CreateOtpComponent(createContext(), onOtpCreated)
    }
}
