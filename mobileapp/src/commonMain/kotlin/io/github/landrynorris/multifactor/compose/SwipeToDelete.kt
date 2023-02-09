package io.github.landrynorris.multifactor.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
internal fun LazyItemScope.SwipeToDelete(onDelete: () -> Unit, content: @Composable () -> Unit) {
    val state = rememberDismissState(confirmStateChange = {
        if(it == DismissValue.DismissedToStart) {
            onDelete()
            true
        } else false
    })
    SwipeToDismiss(modifier = Modifier.animateItemPlacement(),
        state = state,
        background = { SwipeBackground(state) },
        dismissContent = { content() },
    directions = setOf(DismissDirection.EndToStart))
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun SwipeBackground(state: DismissState) {
    val direction = state.dismissDirection ?: return
    val color by animateColorAsState(
        when (state.targetValue) {
            DismissValue.DismissedToStart -> Color.Red
            else -> Color.Transparent
        }
    )
    val alignment = when (direction) {
        DismissDirection.StartToEnd -> Alignment.CenterStart
        DismissDirection.EndToStart -> Alignment.CenterEnd
    }
    val icon = Icons.Default.Delete
    val scale by animateFloatAsState(
        if (state.targetValue == DismissValue.Default) 0.75f else 1f
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = alignment
    ) {
        if(direction == DismissDirection.EndToStart) {
            Icon(
                icon,
                contentDescription = "Delete",
                modifier = Modifier.scale(scale)
            )
        }
    }
}
