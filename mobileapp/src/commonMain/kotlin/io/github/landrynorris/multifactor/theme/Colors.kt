package io.github.landrynorris.multifactor.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val IronGray = ColorPair(
    light = Color(red = 0xDA, green = 0xE0, blue = 0xE3),
    dark = Color(red = 0x3A, green = 0x3C, blue = 0x40))

val Background = ColorPair(
    light = Color(red = 0xCF, green = 0xCF, blue = 0xCF),
    dark = Color(red = 0x84, green = 0x84, blue = 0x82))

val SecondaryGray = ColorPair(
    light = Color(red = 0xE3, green = 0xDD, blue = 0xDA),
    dark = Color(red = 0x40, green = 0x3C, blue = 0x3A))

val lightColors = lightColors(primary = IronGray.light, secondary = SecondaryGray.light,
    onPrimary = Color.Black, onSecondary = Color.Black, background = Background.light)

val darkColors = darkColors(primary = IronGray.dark, secondary = SecondaryGray.dark,
    onPrimary = Color.White, onSecondary = Color.White, background = Background.dark)

internal val colors
    @Composable get() = if(isSystemInDarkTheme()) darkColors else lightColors

data class ColorPair(val light: Color, val dark: Color)
