package io.github.landrynorris.multifactor.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider

@Composable
internal actual fun Dialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
    Popup(positionProvider()) {
        content()
    }
}

fun positionProvider(): PopupPositionProvider {
    return object: PopupPositionProvider {
        override fun calculatePosition(
            anchorBounds: IntRect, windowSize: IntSize,
            layoutDirection: LayoutDirection, popupContentSize: IntSize
        ): IntOffset {
            return IntOffset(windowSize.width/2 - popupContentSize.width/2,
                windowSize.height/2 - popupContentSize.height/2)
        }
    }
}
