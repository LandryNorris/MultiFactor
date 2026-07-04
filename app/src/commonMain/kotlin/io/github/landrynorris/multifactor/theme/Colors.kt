package io.github.landrynorris.multifactor.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme

val IronGray = ColorPair(
    light = Color(red = 0xDA, green = 0xE0, blue = 0xE3),
    dark = Color(red = 0x3A, green = 0x3C, blue = 0x40))

val darkColorScheme = dynamicColorScheme(IronGray.dark, true, style = PaletteStyle.Neutral)

val lightColorScheme = dynamicColorScheme(IronGray.light, false, style = PaletteStyle.Neutral)

internal val colorScheme
    @Composable get() = if(isSystemInDarkTheme()) darkColorScheme else lightColorScheme

data class ColorPair(val light: Color, val dark: Color)
