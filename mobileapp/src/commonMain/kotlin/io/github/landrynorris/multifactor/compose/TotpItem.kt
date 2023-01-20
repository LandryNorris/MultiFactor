package io.github.landrynorris.multifactor.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.unit.sp

@Composable
internal fun TotpItem(pin: String, name: String, progress: Float, onCopyClicked: () -> Unit = {}) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(name, fontSize = 18.sp)
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {

            Text(pin, modifier = Modifier.weight(1f),
                fontSize = 20.sp, letterSpacing = 1.sp)
            CopyButton(onClick = onCopyClicked)
            Box(modifier = Modifier.size(LocalViewConfiguration.current.minimumTouchTargetSize),
                contentAlignment = Alignment.Center) {
                val animatedProgress = animateFloatAsState(progress,
                    ProgressIndicatorDefaults.ProgressAnimationSpec)
                CircularProgressIndicator(progress = animatedProgress.value,
                    modifier = Modifier.fillMaxSize(0.6f))
            }
        }
    }
}
