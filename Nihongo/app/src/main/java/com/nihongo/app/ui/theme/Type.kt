package com.nihongo.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp

/**
 * Multi-script typography engineered for Japanese (Kanji/Kana), Bengali (tall vowel diacritics
 * and conjuncts), and English editorial clarity.
 *
 * `includeFontPadding = true` is explicitly enabled so Bengali upper/lower matras and
 * Japanese furigana-adjacent lines are never clipped.
 */
private val MultiScriptPlatformStyle = PlatformTextStyle(
    includeFontPadding = true
)

private val EditorialLineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None
)

val NihongoTypography = Typography(
    // Used for large featured Kanji / Kana character headers
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 52.sp,
        lineHeight = 68.sp,
        letterSpacing = 0.sp,
        platformStyle = MultiScriptPlatformStyle,
        lineHeightStyle = EditorialLineHeightStyle
    ),
    // Used for Flashcard front Japanese words and detail hero headers
    displayMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 38.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
        platformStyle = MultiScriptPlatformStyle,
        lineHeightStyle = EditorialLineHeightStyle
    ),
    // Screen titles
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.2).sp,
        platformStyle = MultiScriptPlatformStyle,
        lineHeightStyle = EditorialLineHeightStyle
    ),
    // Section headers
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
        platformStyle = MultiScriptPlatformStyle,
        lineHeightStyle = EditorialLineHeightStyle
    ),
    // Card titles & Japanese vocabulary entries
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp,
        platformStyle = MultiScriptPlatformStyle,
        lineHeightStyle = EditorialLineHeightStyle
    ),
    // Subtitles & grammar pattern headers
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.1.sp,
        platformStyle = MultiScriptPlatformStyle,
        lineHeightStyle = EditorialLineHeightStyle
    ),
    // Small badges & Romaji subtitles
    titleSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        letterSpacing = 0.1.sp,
        platformStyle = MultiScriptPlatformStyle,
        lineHeightStyle = EditorialLineHeightStyle
    ),
    // Primary Bengali & English explanations and example sentences
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.15.sp,
        platformStyle = MultiScriptPlatformStyle,
        lineHeightStyle = EditorialLineHeightStyle
    ),
    // Secondary explanations, meanings, and notes
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.2.sp,
        platformStyle = MultiScriptPlatformStyle,
        lineHeightStyle = EditorialLineHeightStyle
    ),
    // Metadata, stroke counts, and audio source labels
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.25.sp,
        platformStyle = MultiScriptPlatformStyle,
        lineHeightStyle = EditorialLineHeightStyle
    ),
    // Button text & navigation labels
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        platformStyle = MultiScriptPlatformStyle,
        lineHeightStyle = EditorialLineHeightStyle
    ),
    // JLPT level badges & filter chips
    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.3.sp,
        platformStyle = MultiScriptPlatformStyle,
        lineHeightStyle = EditorialLineHeightStyle
    ),
    // Compact status tags
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.3.sp,
        platformStyle = MultiScriptPlatformStyle,
        lineHeightStyle = EditorialLineHeightStyle
    )
)
