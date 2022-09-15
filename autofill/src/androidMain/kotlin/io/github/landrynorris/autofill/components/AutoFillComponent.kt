package io.github.landrynorris.autofill.components

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface AutoFillLogic {
    val state: StateFlow<AutoFillState>

    fun select(model: AutoFillModel)
}

class AutoFillComponent(context: ComponentContext,
                        private val onSelected: (AutoFillModel) -> Unit
                        ): ComponentContext by context, AutoFillLogic {
    override val state = MutableStateFlow(AutoFillState())

    override fun select(model: AutoFillModel) {

    }
}

data class AutoFillModel(val name: String)

data class AutoFillState(val models: List<AutoFillModel> = listOf())
