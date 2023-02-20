package io.github.landrynorris.multifactor.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import io.github.landrynorris.multifactor.components.Setting
import io.github.landrynorris.multifactor.components.SettingsLogic

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Settings(logic: SettingsLogic) {
    val state by logic.passwordSettings.collectAsState(listOf())
    LazyColumn(Modifier.fillMaxSize()) {
        stickyHeader { Text("Password Settings") }
        items(state) { item ->
            when(item.value) {
                is Boolean ->
                    BooleanSettingsSwitch(item as Setting<Boolean>, item.value, item.onValueChanged)
                is Int -> IntSettingsBox(item as Setting<Int>, item.value, item.onValueChanged)
            }
        }
        item {
            MultiFactorTextButton("About", onClick = logic::navigateToAbout)
        }
    }
}

@Composable
internal fun BooleanSettingsSwitch(setting: Setting<Boolean>,
                          value: Boolean, onValueChanged: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().contentDescription(setting.name),
        verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(setting.name, fontSize = 24.sp)
            Text(setting.description, maxLines = 2)
        }

        Switch(value, onValueChanged, modifier = Modifier.contentDescription("switch"))
    }
}



@Composable
internal fun IntSettingsBox(setting: Setting<Int>,
                   value: Int, onValueChanged: (Int) -> Unit) {
    Row(Modifier.fillMaxWidth().contentDescription(setting.name),
        verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(setting.name, fontSize = 24.sp)
            Text(setting.description, maxLines = 2)
        }

        val text = if(value == -1) "" else value.toString()
        TextField(value = text, onValueChange = { onValueChanged(it.toIntOrNull() ?: -1) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.contentDescription("IntSetting"))
    }
}
