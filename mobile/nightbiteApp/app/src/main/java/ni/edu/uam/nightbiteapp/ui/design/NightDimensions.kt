package ni.edu.uam.nightbiteapp.ui.design

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class NightDimensions(
    val screenHorizontalPadding: Dp,
    val screenVerticalPadding: Dp,
    val contentMaxWidth: Dp,
    val cardMaxWidth: Dp,
    val iconButtonSize: Dp,
    val levelButtonSize: Dp,
    val levelButtonContainerSize: Dp,
    val sectionSpacing: Dp,
    val itemSpacing: Dp,
    val ageCheckCardWidth: Dp,
    val ageCheckCardHeight: Dp,
    val agePickerWidth: Dp,
    val settingsCardWidth: Dp,
    val settingsCardHeight: Dp,
    val settingsInnerWidth: Dp,
    val settingsOptionRowWidth: Dp,
    val settingsActionButtonWidth: Dp,
    val registerCardWidth: Dp
)

fun nightDimensionsFor(windowSize: NightWindowSize): NightDimensions {
    return when (windowSize) {
        NightWindowSize.Compact -> NightDimensions(
            screenHorizontalPadding = 20.dp,
            screenVerticalPadding = 14.dp,
            contentMaxWidth = 560.dp,
            cardMaxWidth = 420.dp,
            iconButtonSize = 48.dp,
            levelButtonSize = 76.dp,
            levelButtonContainerSize = 94.dp,
            sectionSpacing = 20.dp,
            itemSpacing = 8.dp,

            // Compact
            settingsCardWidth = 340.dp,
            settingsCardHeight = 292.dp,
            settingsInnerWidth = 250.dp,
            settingsOptionRowWidth = 220.dp,
            settingsActionButtonWidth = 130.dp,
            registerCardWidth = 420.dp,

            ageCheckCardWidth = 220.dp,
            ageCheckCardHeight = 200.dp,
            agePickerWidth = 130.dp
        )

        NightWindowSize.Medium -> NightDimensions(
            screenHorizontalPadding = 32.dp,
            screenVerticalPadding = 20.dp,
            contentMaxWidth = 720.dp,
            cardMaxWidth = 560.dp,
            iconButtonSize = 58.dp,
            levelButtonSize = 88.dp,
            levelButtonContainerSize = 108.dp,
            sectionSpacing = 32.dp,
            itemSpacing = 12.dp,
            // Medium
            settingsCardWidth = 390.dp,
            settingsCardHeight = 300.dp,
            settingsInnerWidth = 280.dp,
            settingsOptionRowWidth = 250.dp,
            settingsActionButtonWidth = 140.dp,
            registerCardWidth = 530.dp,

            ageCheckCardWidth = 290.dp,
            ageCheckCardHeight = 250.dp,
            agePickerWidth = 180.dp
        )

        NightWindowSize.Expanded -> NightDimensions(
            screenHorizontalPadding = 48.dp,
            screenVerticalPadding = 28.dp,
            contentMaxWidth = 900.dp,
            cardMaxWidth = 640.dp,
            iconButtonSize = 66.dp,
            levelButtonSize = 96.dp,
            levelButtonContainerSize = 118.dp,
            sectionSpacing = 40.dp,
            itemSpacing = 16.dp,
            // Expanded
            settingsCardWidth = 430.dp,
            settingsCardHeight = 320.dp,
            settingsInnerWidth = 310.dp,
            settingsOptionRowWidth = 280.dp,
            settingsActionButtonWidth = 150.dp,
            registerCardWidth = 580.dp,

            ageCheckCardWidth = 330.dp,
            ageCheckCardHeight = 270.dp,
            agePickerWidth = 200.dp
        )
    }
}