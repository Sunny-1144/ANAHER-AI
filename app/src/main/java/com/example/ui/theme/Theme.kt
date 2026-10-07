package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// =====================================================
// ANAHER LAGOON DESIGN SYSTEM - THEME SPECIFICATION
// =====================================================

enum class ThemeMode {
    SYSTEM, LIGHT, DARK, HIGH_CONTRAST
}

// -----------------------------------------------------
// Shape Specifications
// radius 8 (chips) / 12 (cards, code blocks) / 16 (panels) / 24 (composer, bubbles) / full (pills, avatars)
// -----------------------------------------------------
object AnaherShapes {
    val Chip = RoundedCornerShape(8.dp)
    val Card = RoundedCornerShape(12.dp)
    val CodeBlock = RoundedCornerShape(12.dp)
    val Panel = RoundedCornerShape(16.dp)
    val Sheet = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    val Bubble = RoundedCornerShape(24.dp)
    val Composer = RoundedCornerShape(24.dp)
    val Pill = CircleShape
    val Avatar = CircleShape
}

val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),      // Chips
    medium = RoundedCornerShape(12.dp),    // Cards, Code blocks
    large = RoundedCornerShape(16.dp),     // Panels
    extraLarge = RoundedCornerShape(24.dp) // Composer, Message Bubbles
)

// -----------------------------------------------------
// Extended Design System Color Tokens Holder
// -----------------------------------------------------
@Immutable
data class LagoonCustomColors(
    val canvas: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val surfaceSubtle: Color,
    val border: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val primary: Color,
    val primaryLight: Color,
    val secondary: Color,
    val tealDark: Color,
    val cyanGlow: Color,
    val cyanHighlight: Color,
    val online: Color,
    val warning: Color,
    val error: Color,
    val agentPurple: Color,
    val tileGradientStart: Color,
    val tileGradientMid: Color,
    val tileGradientEnd: Color,
    val tileGlowLine: Color,
    val tileWave: Color
)

val LocalLagoonColors = staticCompositionLocalOf {
    LagoonCustomColors(
        canvas = LagoonCanvasLight,
        surface = LagoonSurfaceLight,
        surfaceVariant = LagoonSurfaceVariantLight,
        surfaceSubtle = LagoonSurfaceSubtleLight,
        border = LagoonBorderLight,
        textPrimary = LagoonTextPrimaryLight,
        textSecondary = LagoonTextSecondaryLight,
        textTertiary = LagoonTextTertiaryLight,
        primary = LagoonPrimary,
        primaryLight = LagoonPrimaryLight,
        secondary = LagoonSecondary,
        tealDark = LagoonTealDark,
        cyanGlow = LagoonCyanGlow,
        cyanHighlight = LagoonCyanHighlight,
        online = LagoonOnline,
        warning = LagoonWarning,
        error = LagoonErrorTextLight,
        agentPurple = LagoonAgentPurple,
        tileGradientStart = LogoTileGradientStart,
        tileGradientMid = LogoTileGradientMid,
        tileGradientEnd = LogoTileGradientEnd,
        tileGlowLine = LogoTileGlowLine,
        tileWave = LogoTileWave
    )
}

// Convenient access from MaterialTheme
val MaterialTheme.lagoonColors: LagoonCustomColors
    @Composable
    @ReadOnlyComposable
    get() = LocalLagoonColors.current

// -----------------------------------------------------
// 1. Light Theme ("Lagoon White") ColorScheme
// -----------------------------------------------------
val LagoonLightColorScheme = lightColorScheme(
    primary = LagoonPrimary,
    onPrimary = Color.White,
    primaryContainer = LagoonSurfaceSubtleLight,
    onPrimaryContainer = LagoonTealDark,
    secondary = LagoonSecondary,
    onSecondary = Color.White,
    secondaryContainer = LagoonSurfaceSubtleLight,
    onSecondaryContainer = LagoonTealDark,
    tertiary = LagoonAgentPurple,
    onTertiary = Color.White,
    background = LagoonCanvasLight,
    onBackground = LagoonTextPrimaryLight,
    surface = LagoonSurfaceLight,
    onSurface = LagoonTextPrimaryLight,
    surfaceVariant = LagoonSurfaceVariantLight,
    onSurfaceVariant = LagoonTextSecondaryLight,
    outline = LagoonBorderLight,
    outlineVariant = LagoonBorderLight.copy(alpha = 0.5f),
    error = LagoonErrorTextLight,
    onError = Color.White
)

// -----------------------------------------------------
// 2. Dark Theme ("Lagoon Abyss") ColorScheme
// -----------------------------------------------------
val LagoonDarkColorScheme = darkColorScheme(
    primary = AbyssPrimaryDark,
    onPrimary = AbyssOnPrimaryDark,
    primaryContainer = AbyssPrimaryContainerDark,
    onPrimaryContainer = AbyssOnPrimaryContainerDark,
    secondary = AbyssSecondaryDark,
    onSecondary = Color.White,
    secondaryContainer = AbyssSurfaceVariantDark,
    onSecondaryContainer = AbyssTextPrimaryDark,
    tertiary = LagoonAgentPurple,
    onTertiary = Color.White,
    background = AbyssCanvasDark,
    onBackground = AbyssTextPrimaryDark,
    surface = AbyssSurfaceDark,
    onSurface = AbyssTextPrimaryDark,
    surfaceVariant = AbyssSurfaceVariantDark,
    onSurfaceVariant = AbyssTextSecondaryDark,
    outline = AbyssBorderDark,
    outlineVariant = AbyssBorderDark.copy(alpha = 0.6f),
    error = LagoonError,
    onError = Color.White
)

// -----------------------------------------------------
// 3. High-Contrast ColorScheme
// -----------------------------------------------------
val LagoonHighContrastColorScheme = lightColorScheme(
    primary = HighContrastPrimary,
    onPrimary = Color.White,
    primaryContainer = HighContrastSurfaceVariant,
    onPrimaryContainer = HighContrastText,
    secondary = HighContrastText,
    onSecondary = Color.White,
    tertiary = HighContrastPrimary,
    onTertiary = Color.White,
    background = HighContrastCanvas,
    onBackground = HighContrastText,
    surface = HighContrastSurface,
    onSurface = HighContrastText,
    surfaceVariant = HighContrastSurfaceVariant,
    onSurfaceVariant = HighContrastText,
    outline = HighContrastBorder,
    outlineVariant = HighContrastBorder,
    error = HighContrastError,
    onError = Color.White
)

// -----------------------------------------------------
// Main Theme Composable with Theme Switching Logic
// -----------------------------------------------------
@Composable
fun AnaherTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemDark
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.HIGH_CONTRAST -> false
    }

    val colorScheme: ColorScheme = when (themeMode) {
        ThemeMode.HIGH_CONTRAST -> LagoonHighContrastColorScheme
        ThemeMode.DARK -> LagoonDarkColorScheme
        ThemeMode.LIGHT -> LagoonLightColorScheme
        ThemeMode.SYSTEM -> if (isDark) LagoonDarkColorScheme else LagoonLightColorScheme
    }

    val customColors = when (themeMode) {
        ThemeMode.HIGH_CONTRAST -> LagoonCustomColors(
            canvas = HighContrastCanvas,
            surface = HighContrastSurface,
            surfaceVariant = HighContrastSurfaceVariant,
            surfaceSubtle = HighContrastSurfaceVariant,
            border = HighContrastBorder,
            textPrimary = HighContrastText,
            textSecondary = HighContrastText,
            textTertiary = HighContrastText,
            primary = HighContrastPrimary,
            primaryLight = HighContrastPrimary,
            secondary = HighContrastSecondary,
            tealDark = HighContrastPrimary,
            cyanGlow = LagoonCyanGlow,
            cyanHighlight = LagoonCyanHighlight,
            online = LagoonOnline,
            warning = LagoonWarning,
            error = HighContrastError,
            agentPurple = LagoonAgentPurple,
            tileGradientStart = LogoTileGradientStart,
            tileGradientMid = LogoTileGradientMid,
            tileGradientEnd = LogoTileGradientEnd,
            tileGlowLine = LogoTileGlowLine,
            tileWave = LogoTileWave
        )
        ThemeMode.DARK -> LagoonCustomColors(
            canvas = AbyssCanvasDark,
            surface = AbyssSurfaceDark,
            surfaceVariant = AbyssSurfaceVariantDark,
            surfaceSubtle = AbyssSurfaceVariantDark,
            border = AbyssBorderDark,
            textPrimary = AbyssTextPrimaryDark,
            textSecondary = AbyssTextSecondaryDark,
            textTertiary = AbyssTextSecondaryDark,
            primary = AbyssPrimaryDark,
            primaryLight = AbyssAccentDark,
            secondary = AbyssSecondaryDark,
            tealDark = AbyssPrimaryDark,
            cyanGlow = LagoonCyanGlow,
            cyanHighlight = AbyssAccentDark,
            online = LagoonOnline,
            warning = LagoonWarning,
            error = LagoonError,
            agentPurple = LagoonAgentPurple,
            tileGradientStart = LogoTileGradientStart,
            tileGradientMid = LogoTileGradientMid,
            tileGradientEnd = LogoTileGradientEnd,
            tileGlowLine = LogoTileGlowLine,
            tileWave = LogoTileWave
        )
        ThemeMode.LIGHT, ThemeMode.SYSTEM -> {
            if (isDark) {
                LagoonCustomColors(
                    canvas = AbyssCanvasDark,
                    surface = AbyssSurfaceDark,
                    surfaceVariant = AbyssSurfaceVariantDark,
                    surfaceSubtle = AbyssSurfaceVariantDark,
                    border = AbyssBorderDark,
                    textPrimary = AbyssTextPrimaryDark,
                    textSecondary = AbyssTextSecondaryDark,
                    textTertiary = AbyssTextSecondaryDark,
                    primary = AbyssPrimaryDark,
                    primaryLight = AbyssAccentDark,
                    secondary = AbyssSecondaryDark,
                    tealDark = AbyssPrimaryDark,
                    cyanGlow = LagoonCyanGlow,
                    cyanHighlight = AbyssAccentDark,
                    online = LagoonOnline,
                    warning = LagoonWarning,
                    error = LagoonError,
                    agentPurple = LagoonAgentPurple,
                    tileGradientStart = LogoTileGradientStart,
                    tileGradientMid = LogoTileGradientMid,
                    tileGradientEnd = LogoTileGradientEnd,
                    tileGlowLine = LogoTileGlowLine,
                    tileWave = LogoTileWave
                )
            } else {
                LagoonCustomColors(
                    canvas = LagoonCanvasLight,
                    surface = LagoonSurfaceLight,
                    surfaceVariant = LagoonSurfaceVariantLight,
                    surfaceSubtle = LagoonSurfaceSubtleLight,
                    border = LagoonBorderLight,
                    textPrimary = LagoonTextPrimaryLight,
                    textSecondary = LagoonTextSecondaryLight,
                    textTertiary = LagoonTextTertiaryLight,
                    primary = LagoonPrimary,
                    primaryLight = LagoonPrimaryLight,
                    secondary = LagoonSecondary,
                    tealDark = LagoonTealDark,
                    cyanGlow = LagoonCyanGlow,
                    cyanHighlight = LagoonCyanHighlight,
                    online = LagoonOnline,
                    warning = LagoonWarning,
                    error = LagoonErrorTextLight,
                    agentPurple = LagoonAgentPurple,
                    tileGradientStart = LogoTileGradientStart,
                    tileGradientMid = LogoTileGradientMid,
                    tileGradientEnd = LogoTileGradientEnd,
                    tileGlowLine = LogoTileGlowLine,
                    tileWave = LogoTileWave
                )
            }
        }
    }

    CompositionLocalProvider(LocalLagoonColors provides customColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}
