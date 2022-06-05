package io.github.landrynorris.multifactor

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TotpItem(pin: String, name: String, progress: Float) {
    Column(modifier = Modifier.fillMaxWidth()
        .background(Theme.colors.background)) {
        Text(name, fontSize = 18.sp)
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {

            Text(pin, modifier = Modifier.weight(1f),
                fontSize = 20.sp, letterSpacing = 1.sp)
            Box(modifier = Modifier.size(LocalViewConfiguration.current.minimumTouchTargetSize),
                contentAlignment = Alignment.Center) {
                val animatedProgress = animateFloatAsState(progress, ProgressIndicatorDefaults.ProgressAnimationSpec)
                CircularProgressIndicator(progress = animatedProgress.value,
                    modifier = Modifier.fillMaxSize(0.6f))
            }
        }
    }
}

@Preview
@Composable
fun TotpPreview() {
    TotpItem("123456", "My Pin", 0.7f)
}
