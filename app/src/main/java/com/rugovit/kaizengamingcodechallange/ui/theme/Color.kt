package com.rugovit.kaizengamingcodechallange.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Blue = Color(0xFF0094FF)
val Orange = Color(0xFFE7410F)
val Yellow = Color(0xFFFFAB30)
val DarkGray = Color(0xFF343434)
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)

val LightColorScheme = lightColorScheme(
    primary = Blue,
    secondary = Orange,
    //tertiary = Yellow, TODO: Add this as tertiary color maybe
    background = DarkGray,
    surface = White,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = Black
)
val DarkColorScheme = darkColorScheme(
    primary = Blue,
    secondary = Orange,
    //tertiary = Yellow, TODO: Add this as tertiary color maybe
    background = DarkGray,
    surface = White,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = Black
)