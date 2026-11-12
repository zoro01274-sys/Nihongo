package com.nihongo.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val NihongoLightColorScheme = lightColorScheme(
    primary = InkGreen,
    onPrimary = PurePaperWhite,
    primaryContainer = InkGreenLightContainer,
    onPrimaryContainer = InkGreenDeep,

    secondary = PhoenixFlowerRed,
    onSecondary = PurePaperWhite,
    secondaryContainer = PhoenixRedLightContainer,
    onSecondaryContainer = AncientWoodBrown,

    tertiary = AncientWoodBrown,
    onTertiary = PurePaperWhite,
    tertiaryContainer = MistGraySoftContainer,
    onTertiaryContainer = AncientWoodBrown,

    background = MistGray,
    onBackground = AncientWoodBrown,

    surface = MistGrayLightSurface,
    onSurface = AncientWoodBrown,
    surfaceVariant = MistGraySoftContainer,
    onSurfaceVariant = AncientWoodMuted,

    error = PhoenixFlowerRed,
    onError = PurePaperWhite,
    errorContainer = PhoenixRedLightContainer,
    onErrorContainer = AncientWoodBrown,

    outline = AncientWoodOutline,
    outlineVariant = MistGraySoftContainer
)

private val NihongoDarkColorScheme = darkColorScheme(
    primary = InkGreenDarkPrimary,
    onPrimary = DarkForestBackground,
    primaryContainer = InkGreenDarkContainer,
    onPrimaryContainer = MistGrayDarkText,

    secondary = PhoenixRedDarkAccent,
    onSecondary = DarkForestBackground,
    secondaryContainer = PhoenixRedDarkContainer,
    onSecondaryContainer = MistGrayDarkText,

    tertiary = MistGrayDarkText,
    onTertiary = DarkForestBackground,
    tertiaryContainer = DarkWoodContainer,
    onTertiaryContainer = MistGrayDarkText,

    background = DarkForestBackground,
    onBackground = MistGrayDarkText,

    surface = DarkMossSurface,
    onSurface = MistGrayDarkText,
    surfaceVariant = DarkWoodContainer,
    onSurfaceVariant = MistGrayDarkMuted,

    error = PhoenixRedDarkAccent,
    onError = DarkForestBackground,
    errorContainer = PhoenixRedDarkContainer,
    onErrorContainer = MistGrayDarkText,

    outline = DarkOutline,
    outlineVariant = DarkWoodContainer
)

@Composable
fun NihongoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) NihongoDarkColorScheme else NihongoLightColorScheme
    val extendedColors = if (darkTheme) DarkNihongoColors else LightNihongoColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.surface.toArgb()
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !darkTheme
                insetsController.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(LocalNihongoColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = NihongoTypography,
            shapes = NihongoShapes,
            content = content
        )
    }
}

/**
 * Convenience accessor for extended NIHONGO semantic tokens across all Composables.
 */
object NihongoTheme {
    val colors: NihongoExtendedColors
        @Composable
        @ReadOnlyComposable
        get() = LocalNihongoColors.current
}
