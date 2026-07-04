package io.github.landrynorris.multifactor.components

import androidx.compose.ui.platform.UriHandler
import com.arkivanov.decompose.ComponentContext
import io.github.landrynorris.mobileapp.config.BuildKonfig

interface AboutLogic {
    val state: AboutState
    fun openLegalPage(handler: UriHandler)
}

class AboutComponent(context: ComponentContext):
    ComponentContext by context, AboutLogic {
    override val state = AboutState(BuildKonfig.version, BuildKonfig.buildId)

    override fun openLegalPage(handler: UriHandler) = handler.openUri(privacyPolicyUrl)
}

data class AboutState(val appVersion: String, val buildId: String)

const val privacyPolicyUrl = "https://github.com/LandryNorris/MultiFactor/blob/main/Privacy.md"
