package io.github.landrynorris.app.compose

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

fun Modifier.contentDescription(description: String): Modifier {
    return semantics { contentDescription = description }
}
