package io.github.landrynorris.multifactor.platform

import androidx.compose.runtime.Composable

@Composable
internal expect fun Dialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit)
