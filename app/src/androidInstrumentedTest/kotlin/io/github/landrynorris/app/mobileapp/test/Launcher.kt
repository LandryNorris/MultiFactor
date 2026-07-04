package io.github.landrynorris.app.mobileapp.test

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.test.core.app.ActivityScenario
import io.github.landrynorris.app.MainActivity
import kotlinx.coroutines.runBlocking

fun withApplication(rule: ComposeTestRule,
                    test: suspend ComposeTestRule.(ActivityScenario<MainActivity>) -> Unit) {
    val scenario = ActivityScenario.launch(MainActivity::class.java)

    runBlocking {
        rule.test(scenario)
    }
}
