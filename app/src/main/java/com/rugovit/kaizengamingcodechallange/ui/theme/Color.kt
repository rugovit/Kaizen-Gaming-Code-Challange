package com.rugovit.kaizengamingcodechallange.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Blue = Color(0xFF0094FF)
val Orange = Color(0xFFE7410F)
val Yellow = Color(0xFFFFAB30)
val LighterGray = Color(0xFFD3D3D3) // Light gray for surfaces
val LightGray = Color(0xFF989797) // Slightly lighter gray for surfaces
val DarkGray = Color(0xFF343434)
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)

val LightColorScheme = lightColorScheme( //TODO pick a better color scheme for light mode
    // Brand
    primary            = Blue,
    onPrimary          = White,
    // Accent #1
    secondary          = Orange,
    onSecondary        = White,
    // Accent #2 (for your yellow star)
    tertiary           = Yellow,
    onTertiary         = Black,
    // Canvas
    background         = White,
    onBackground       = Black,
    // Surfaces (cards, sheets, headers)
    surface            = DarkGray,
    onSurface          = White,
    surfaceVariant     = LightGray,
    onSurfaceVariant   = LighterGray
)

val DarkColorScheme = darkColorScheme(
    // Brand
    primary            = Blue,
    onPrimary          = White,
    // Accent #1
    secondary          = Orange,
    onSecondary        = White,
    // Accent #2 (for your yellow star)
    tertiary           = Yellow,
    onTertiary         = Black,
    // Canvas
    background         = White,
    onBackground       = Black,
    // Surfaces (cards, sheets, headers)
    surface            = DarkGray,
    onSurface          = White,
    surfaceVariant     = LightGray,
    onSurfaceVariant   = LighterGray
)