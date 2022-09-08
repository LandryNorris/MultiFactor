package io.github.landrynorris.autofill.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AutoFillUI() {
    Column(Modifier.size(100.dp).background(Color.LightGray)) {
        Text("AutoFill")

    }
}
