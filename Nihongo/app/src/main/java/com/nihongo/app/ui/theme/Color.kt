package com.nihongo.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * ============================================================================
 * OFFICIAL PERMANENT NIHONGO BRAND PALETTE
 * ============================================================================
 * 1. INK GREEN:          #335749 (RGB: 51, 87, 73)
 * 2. PHOENIX FLOWER RED: #C9432D (RGB: 201, 67, 45)
 * 3. MIST GRAY:          #D6D0C4 (RGB: 214, 208, 196)
 * 4. ANCIENT WOOD BROWN: #4B4038 (RGB: 75, 64, 56)
 * ============================================================================
 */

// Core Official Brand Tokens
val InkGreen = Color(0xFF335749)
val PhoenixFlowerRed = Color(0xFFC9432D)
val MistGray = Color(0xFFD6D0C4)
val AncientWoodBrown = Color(0xFF4B4038)

// Light Theme Botanical & Stationery Derivations
val MistGrayLightSurface = Color(0xFFE4DFD5)      // Elevated learning card surface
val MistGraySoftContainer = Color(0xFFCBC4B6)     // Secondary container / subtle divider fill
val InkGreenLightContainer = Color(0xFFCFDCD6)    // Selected state / active chip fill in light mode
val InkGreenDeep = Color(0xFF233E34)              // Pressed state & high-contrast badge text
val PhoenixRedLightContainer = Color(0xFFF3D7D2)  // Soft highlight / quiz feedback container
val AncientWoodMuted = Color(0xFF6E6056)          // Secondary text / Romaji / captions in light mode
val AncientWoodOutline = Color(0xFFA89F94)        // Subtle card borders & stationery rules
val PurePaperWhite = Color(0xFFF6F3EC)            // Input fields & flashcards front highlight

// Dark Theme Complementary Botanical Derivations
val DarkForestBackground = Color(0xFF18221E)      // Main dark theme background
val DarkMossSurface = Color(0xFF22312B)           // Dark theme learning cards
val DarkWoodContainer = Color(0xFF2E3D36)         // Secondary dark surface / inactive chips
val InkGreenDarkPrimary = Color(0xFF6B9E8A)       // Accessible Ink Green variation for dark mode
val InkGreenDarkContainer = Color(0xFF28463B)     // Selected container in dark mode
val PhoenixRedDarkAccent = Color(0xFFE0634E)      // Accessible Phoenix Flower Red for dark mode
val PhoenixRedDarkContainer = Color(0xFF52231B)   // Quiz alert / feedback container in dark mode
val MistGrayDarkText = Color(0xFFE6E1D6)          // Main readable text in dark mode
val MistGrayDarkMuted = Color(0xFFB5AEA1)         // Secondary text in dark mode
val DarkOutline = Color(0xFF42544C)               // Card borders in dark mode

/**
 * Extended semantic color tokens accessible via `NihongoTheme.colors`
 * so every screen strictly adheres to the official palette.
 */
@Immutable
data class NihongoExtendedColors(
    val inkGreen: Color,
    val phoenixFlowerRed: Color,
    val mistGray: Color,
    val ancientWoodBrown: Color,
    val cardSurface: Color,
    val elevatedSurface: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val borderOutline: Color,
    val selectedContainer: Color,
    val highlightContainer: Color,
    val isDark: Boolean
)

val LightNihongoColors = NihongoExtendedColors(
    inkGreen = InkGreen,
    phoenixFlowerRed = PhoenixFlowerRed,
    mistGray = MistGray,
    ancientWoodBrown = AncientWoodBrown,
    cardSurface = MistGrayLightSurface,
    elevatedSurface = PurePaperWhite,
    primaryText = AncientWoodBrown,
    secondaryText = AncientWoodMuted,
    borderOutline = AncientWoodOutline,
    selectedContainer = InkGreenLightContainer,
    highlightContainer = PhoenixRedLightContainer,
    isDark = false
)

val DarkNihongoColors = NihongoExtendedColors(
    inkGreen = InkGreenDarkPrimary,
    phoenixFlowerRed = PhoenixRedDarkAccent,
    mistGray = DarkForestBackground,
    ancientWoodBrown = MistGrayDarkText,
    cardSurface = DarkMossSurface,
    elevatedSurface = DarkWoodContainer,
    primaryText = MistGrayDarkText,
    secondaryText = MistGrayDarkMuted,
    borderOutline = DarkOutline,
    selectedContainer = InkGreenDarkContainer,
    highlightContainer = PhoenixRedDarkContainer,
    isDark = true
)

val LocalNihongoColors = staticCompositionLocalOf { LightNihongoColors }
