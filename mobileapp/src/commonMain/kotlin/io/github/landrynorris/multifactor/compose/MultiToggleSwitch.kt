package io.github.landrynorris.multifactor.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp

@Composable
internal fun MultiToggleSwitch(currentIndex: Int, names: List<String>, onToggleChanged: (Int) -> Unit) {
    val selectedTint = MaterialTheme.colors.primary
    val unselectedTint = Color.Unspecified

    Row(modifier = Modifier
        .height(IntrinsicSize.Min)
        .border(BorderStroke(1.dp, Color.LightGray))) {
        names.forEachIndexed { index, toggleState ->
            val isSelected = index == currentIndex
            val backgroundTint = if (isSelected) selectedTint else unselectedTint
            val textColor = if (isSelected) Color.White else Color.Unspecified

            if (index != 0) {
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
            }

            Row(
                modifier = Modifier
                    .background(backgroundTint)
                    .padding(vertical = 6.dp, horizontal = 8.dp)
                    .toggleable(
                        value = isSelected,
                        enabled = true,
                        onValueChange = { selected ->
                            if (selected) {
                                onToggleChanged(index)
                            }
                        })
            ) {
                Text(toggleState.toUpperCase(Locale.current), color = textColor, modifier = Modifier.padding(4.dp))
            }

        }
    }
}