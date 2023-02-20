package io.github.landrynorris.multifactor.components

import com.arkivanov.decompose.ComponentContext

interface AboutLogic {
    fun openLegalPage()
}

class AboutComponent(context: ComponentContext,
                     private val openLegal: () -> Unit):
    ComponentContext by context, AboutLogic {
    override fun openLegalPage() = openLegal()
}
