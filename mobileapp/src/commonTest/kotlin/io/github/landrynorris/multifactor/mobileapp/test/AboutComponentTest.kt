package io.github.landrynorris.multifactor.mobileapp.test

import androidx.compose.ui.platform.UriHandler
import io.github.landrynorris.mobileapp.config.BuildKonfig
import io.github.landrynorris.multifactor.components.AboutComponent
import io.github.landrynorris.multifactor.components.AboutLogic
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AboutComponentTest {
    @Test
    fun testClickPrivacyPolicy() {
        val component = createComponent()

        var lastUri: String? = null
        component.openLegalPage(object: UriHandler {
            override fun openUri(uri: String) {
                lastUri = uri
            }
        })

        assertNotNull(lastUri)
    }

    @Test
    fun testVersionInfo() {
        val component = createComponent()

        assertEquals(BuildKonfig.version, component.state.appVersion)
        assertEquals(BuildKonfig.buildId, component.state.buildId)
    }

    private fun createComponent(): AboutLogic {
        return AboutComponent(createContext())
    }
}