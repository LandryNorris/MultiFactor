package io.github.landrynorris.multifactor.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

//These extensions are not published for iosSimulatorArm64, so we copy them here

@Composable
internal fun <T : Any> Value<T>.subscribeAsState(): State<T> {
    val state = remember(this) { mutableStateOf(value) }

    DisposableEffect(this) {
        val observer: (T) -> Unit = { state.value = it }

        subscribe(observer)

        onDispose {
            unsubscribe(observer)
        }
    }

    return state
}

@Composable
internal fun <C : Any, T : Any> Children(
    stack: ChildStack<C, T>,
    modifier: Modifier = Modifier,
    animation: StackAnimation<C, T>? = null,
    content: @Composable (child: Child.Created<C, T>) -> Unit,
) {
    val holder = rememberSaveableStateHolder()

    holder.retainStates(stack.getConfigurations())

    val anim = animation ?: emptyStackAnimation()

    anim(stack = stack, modifier = modifier) { child ->
        holder.SaveableStateProvider(child.configuration.key()) {
            content(child)
        }
    }
}


private fun ChildStack<*, *>.getConfigurations(): Set<String> =
    items.mapTo(HashSet()) { it.configuration.key() }

private fun Any.key(): String = "${this::class.simpleName}_${hashCode().toString(radix = 36)}"

@Composable
private fun SaveableStateHolder.retainStates(currentKeys: Set<Any>) {
    val keys = remember(this) { Keys(currentKeys) }

    DisposableEffect(this, currentKeys) {
        keys.set.forEach {
            if (it !in currentKeys) {
                removeState(it)
            }
        }

        keys.set = currentKeys

        onDispose {}
    }
}

private class Keys(
    var set: Set<Any>
)

internal fun <C : Any, T : Any> emptyStackAnimation(): StackAnimation<C, T> =
    StackAnimation { stack, modifier, childContent ->
        Box(modifier = modifier) {
            childContent(stack.active)
        }
    }

/**
 * Tracks the [ChildStack] changes and animates between child widget.
 */
internal interface StackAnimation<C : Any, T : Any> {
    @Composable
    operator fun invoke(
        stack: ChildStack<C, T>,
        modifier: Modifier,
        content: @Composable (child: Child.Created<C, T>) -> Unit,
    )
}

/**
 * Factory function for [StackAnimation] while `fun interface` with a `@Composable` function
 * is not supported - [b/221488059](https://issuetracker.google.com/issues/221488059).
 */
@Suppress("FunctionName") // Factory function
internal inline fun <C : Any, T : Any> StackAnimation(
    crossinline content: @Composable (
        stack: ChildStack<C, T>,
        modifier: Modifier,
        content: @Composable (child: Child.Created<C, T>) -> Unit,
    ) -> Unit
): StackAnimation<C, T> =
    object : StackAnimation<C, T> {
        @Composable
        override operator fun invoke(
            stack: ChildStack<C, T>,
            modifier: Modifier,
            content: @Composable (child: Child.Created<C, T>) -> Unit,
        ) {
            content(stack, modifier, content)
        }
    }

