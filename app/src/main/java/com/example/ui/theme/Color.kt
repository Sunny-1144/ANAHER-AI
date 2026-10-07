package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// =====================================================
// ANAHER LAGOON DESIGN SYSTEM - COLOR TOKENS
// =====================================================

// -----------------------------------------------------
// Light Theme ("Lagoon White") Tokens
// -----------------------------------------------------
val LagoonCanvasLight = Color(0xFFF7FAFC)       // App background / canvas
val LagoonSurfaceLight = Color(0xFFFFFFFF)      // Cards, composer, sidebar
val LagoonSurfaceVariantLight = Color(0xFFEDF5F7) // Hover, user bubble, code header
val LagoonSurfaceSubtleLight = Color(0xFFE0F4F7)  // Pills, badges, selected item
val LagoonBorderLight = Color(0xFFD6E8EC)         // All 1px dividers and outlines
val LagoonTextPrimaryLight = Color(0xFF081B26)    // Body and headings
val LagoonTextSecondaryLight = Color(0xFF436170)  // Labels, helper text
val LagoonTextTertiaryLight = Color(0xFF7897A4)   // Timestamps, captions only (low contrast)

// Brand Accents & Status
val LagoonPrimary = Color(0xFF00838F)             // Buttons, links, active nav
val LagoonPrimaryLight = Color(0xFF00ACC1)        // Hover, gradients
val LagoonSecondary = Color(0xFF0097A7)           // Secondary accents
val LagoonTealDark = Color(0xFF006064)            // Text on light teal chips
val LagoonCyanGlow = Color(0xFF00E5FF)            // Glow, progress shimmer, logo halo
val LagoonCyanHighlight = Color(0xFF18FFFF)       // Tiny highlights only
val LagoonOnline = Color(0xFF00E676)              // Status dot
val LagoonWarning = Color(0xFFFFB300)             // Warnings
val LagoonError = Color(0xFFFF5252)               // Errors
val LagoonErrorTextLight = Color(0xFFD32F2F)      // Errors text on light surfaces
val LagoonAgentPurple = Color(0xFF7C4DFF)         // Agent / autonomy accent

// Convenience Aliases for Lagoon White
val LagoonWhiteCanvas = LagoonCanvasLight
val LagoonWhiteSurface = LagoonSurfaceLight
val LagoonWhiteSurfaceVariant = LagoonSurfaceVariantLight
val LagoonWhiteSurfaceSubtle = LagoonSurfaceSubtleLight
val LagoonWhiteBorder = LagoonBorderLight
val LagoonWhiteTextPrimary = LagoonTextPrimaryLight
val LagoonWhiteTextSecondary = LagoonTextSecondaryLight
val LagoonWhiteTextTertiary = LagoonTextTertiaryLight

// -----------------------------------------------------
// Dark Theme ("Lagoon Abyss") Tokens
// -----------------------------------------------------
val AbyssCanvasDark = Color(0xFF050E14)           // Canvas background
val AbyssSurfaceDark = Color(0xFF0C1B24)          // Surface (cards, panels)
val AbyssSurfaceVariantDark = Color(0xFF142733)   // Surface variant (bubbles, headers)
val AbyssBorderDark = Color(0xFF1D3748)           // Border / outlines
val AbyssTextPrimaryDark = Color(0xFFEBF6FA)      // Primary text
val AbyssTextSecondaryDark = Color(0xFF8FB1C0)    // Secondary text / labels
val AbyssPrimaryDark = Color(0xFF00E5FF)          // Cyan primary on dark
val AbyssOnPrimaryDark = Color(0xFF00363A)        // On-primary text/icon
val AbyssPrimaryContainerDark = Color(0xFF004D54) // Primary container
val AbyssOnPrimaryContainerDark = Color(0xFFE0F7FA) // On-primary container
val AbyssAccentDark = Color(0xFF26E6FF)           // Cyan accent
val AbyssSecondaryDark = Color(0xFF00ACC1)        // Secondary accent

// Convenience Aliases for Lagoon Abyss
val LagoonAbyssCanvas = AbyssCanvasDark
val LagoonAbyssSurface = AbyssSurfaceDark
val LagoonAbyssSurfaceVariant = AbyssSurfaceVariantDark
val LagoonAbyssBorder = AbyssBorderDark
val LagoonAbyssTextPrimary = AbyssTextPrimaryDark
val LagoonAbyssTextSecondary = AbyssTextSecondaryDark
val LagoonAbyssPrimary = AbyssPrimaryDark

// -----------------------------------------------------
// High-Contrast Theme Tokens
// -----------------------------------------------------
val HighContrastPrimary = Color(0xFF004D56)       // Primary deep teal
val HighContrastCanvas = Color(0xFFFFFFFF)        // Pure white canvas
val HighContrastSurface = Color(0xFFFFFFFF)       // Pure white surface
val HighContrastText = Color(0xFF000000)          // Pure black text
val HighContrastBorder = Color(0xFF000000)        // Pure black 1px outlines
val HighContrastSurfaceVariant = Color(0xFFE5EFF2) // High contrast variant
val HighContrastSecondary = Color(0xFF000000)     // Black secondary
val HighContrastError = Color(0xFFD32F2F)         // High contrast error text

// -----------------------------------------------------
// Logo Tile & Splash Gradient (135° Linear Gradient)
// -----------------------------------------------------
val LogoTileGradientStart = Color(0xFF071924)     // Start 0%
val LogoTileGradientMid = Color(0xFF003844)       // Mid 50%
val LogoTileGradientEnd = Color(0xFF005A69)       // End 100%
val LogoTileWave = Color(0x660D2E3B)              // Wave layer at 40% opacity
val LogoTileGlowLine = Color(0xFF00E5FF)          // Cyan glow line

val LogoTileBrush = Brush.linearGradient(
    colors = listOf(
        LogoTileGradientStart,
        LogoTileGradientMid,
        LogoTileGradientEnd
    )
)
