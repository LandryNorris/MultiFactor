package io.github.landrynorris.multifactor.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun LazyItemScope.SwipeToDelete(onDelete: () -> Unit, content: @Composable () -> Unit) {
    val state = rememberSwipeToDismissBoxState()
    SwipeToDismissBox(modifier = Modifier.animateItem(),
        state = state,
        backgroundContent = { SwipeBackground(state) },
        content = { content() },
        enableDismissFromEndToStart = true,
        enableDismissFromStartToEnd = false,
        onDismiss = {
            if(it == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SwipeBackground(state: SwipeToDismissBoxState) {
    val direction = state.dismissDirection
    val color by animateColorAsState(
        when (state.targetValue) {
            SwipeToDismissBoxValue.EndToStart -> Color.Red
            else -> Color.Transparent
        }
    )
    val alignment = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
        else -> Alignment.CenterEnd
    }
    val icon = Icons.Default.Delete
    val scale by animateFloatAsState(
        if (state.targetValue == SwipeToDismissBoxValue.Settled) 0.75f else 1f
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = alignment
    ) {
        if(direction == SwipeToDismissBoxValue.EndToStart) {
            Icon(
                icon,
                contentDescription = "Delete",
                modifier = Modifier.scale(scale)
            )
        }
    }
}
