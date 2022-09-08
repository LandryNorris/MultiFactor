package io.github.landrynorris.autofill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import io.github.landrynorris.autofill.ui.AutoFillUI

class AutoFillActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Starting AutoFill UI")

        setContent {
            AutoFillUI()
        }
    }
}