package io.github.landrynorris.multifactor.components

import androidx.compose.ui.platform.UriHandler
import com.arkivanov.decompose.ComponentContext

interface AboutLogic {
    fun openLegalPage(handler: UriHandler)
}

class AboutComponent(context: ComponentContext):
    ComponentContext by context, AboutLogic {
    override fun openLegalPage(handler: UriHandler) = handler.openUri(privacyPolicyUrl)
}

const val privacyPolicyUrl = "https://github.com/LandryNorris/MultiFactor/blob/main/Privacy.md"
