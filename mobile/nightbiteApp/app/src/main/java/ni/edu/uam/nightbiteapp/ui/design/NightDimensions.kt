package ni.edu.uam.nightbiteapp.ui.design

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

    val registerCardWidth: Dp,

    // HomeScreen
    val homeHeaderWidth: Dp,
    val homeHeaderHeight: Dp,
    val homeHeaderTitleSize: TextUnit,
    val homeHeaderSubtitleSize: TextUnit,
    val homeLevelsRowWidth: Dp,
    val homeLevelBoxWidth: Dp,
    val homeLevelBoxHeight: Dp,
    val homeLevelCubeWidth: Dp,
    val homeLevelCubeHeight: Dp,
    val homeLevelMarkerSize: Dp,
    val homeLevelLockSize: Dp,
    val homeStarSize: Dp,
    val homeLevelLabelWidth: Dp,
    val homeLevelLabelHeight: Dp,
    val homeBottomBarWidth: Dp,
    val homeBottomBarHeight: Dp,
    val homeBottomIconSize: Dp,
    val homeBottomSelectedIconSize: Dp,
    val homeBottomItemWidth: Dp
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

            ageCheckCardWidth = 220.dp,
            ageCheckCardHeight = 200.dp,
            agePickerWidth = 130.dp,

            settingsCardWidth = 340.dp,
            settingsCardHeight = 292.dp,
            settingsInnerWidth = 250.dp,
            settingsOptionRowWidth = 220.dp,
            settingsActionButtonWidth = 130.dp,

            registerCardWidth = 420.dp,

            // HomeScreen - Compact
            homeHeaderWidth = 230.dp,
            homeHeaderHeight = 58.dp,
            homeHeaderTitleSize = 16.sp,
            homeHeaderSubtitleSize = 9.sp,
            homeLevelsRowWidth = 540.dp,
            homeLevelBoxWidth = 86.dp,
            homeLevelBoxHeight = 112.dp,
            homeLevelCubeWidth = 70.dp,
            homeLevelCubeHeight = 48.dp,
            homeLevelMarkerSize = 54.dp,
            homeLevelLockSize = 42.dp,
            homeStarSize = 25.dp,
            homeLevelLabelWidth = 72.dp,
            homeLevelLabelHeight = 18.dp,
            homeBottomBarWidth = 360.dp,
            homeBottomBarHeight = 58.dp,
            homeBottomIconSize = 42.dp,
            homeBottomSelectedIconSize = 48.dp,
            homeBottomItemWidth = 100.dp
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

            ageCheckCardWidth = 290.dp,
            ageCheckCardHeight = 250.dp,
            agePickerWidth = 180.dp,

            settingsCardWidth = 390.dp,
            settingsCardHeight = 300.dp,
            settingsInnerWidth = 280.dp,
            settingsOptionRowWidth = 250.dp,
            settingsActionButtonWidth = 140.dp,

            registerCardWidth = 530.dp,

            // HomeScreen - Medium
            homeHeaderWidth = 280.dp,
            homeHeaderHeight = 64.dp,
            homeHeaderTitleSize = 19.sp,
            homeHeaderSubtitleSize = 10.sp,
            homeLevelsRowWidth = 690.dp,
            homeLevelBoxWidth = 110.dp,
            homeLevelBoxHeight = 138.dp,
            homeLevelCubeWidth = 88.dp,
            homeLevelCubeHeight = 60.dp,
            homeLevelMarkerSize = 66.dp,
            homeLevelLockSize = 48.dp,
            homeStarSize = 31.dp,
            homeLevelLabelWidth = 88.dp,
            homeLevelLabelHeight = 21.dp,
            homeBottomBarWidth = 500.dp,
            homeBottomBarHeight = 66.dp,
            homeBottomIconSize = 50.dp,
            homeBottomSelectedIconSize = 58.dp,
            homeBottomItemWidth = 135.dp
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

            ageCheckCardWidth = 330.dp,
            ageCheckCardHeight = 270.dp,
            agePickerWidth = 200.dp,

            settingsCardWidth = 430.dp,
            settingsCardHeight = 320.dp,
            settingsInnerWidth = 310.dp,
            settingsOptionRowWidth = 280.dp,
            settingsActionButtonWidth = 150.dp,

            registerCardWidth = 580.dp,

            // HomeScreen - Expanded
            homeHeaderWidth = 320.dp,
            homeHeaderHeight = 70.dp,
            homeHeaderTitleSize = 21.sp,
            homeHeaderSubtitleSize = 11.sp,
            homeLevelsRowWidth = 820.dp,
            homeLevelBoxWidth = 124.dp,
            homeLevelBoxHeight = 152.dp,
            homeLevelCubeWidth = 98.dp,
            homeLevelCubeHeight = 66.dp,
            homeLevelMarkerSize = 74.dp,
            homeLevelLockSize = 54.dp,
            homeStarSize = 35.dp,
            homeLevelLabelWidth = 98.dp,
            homeLevelLabelHeight = 23.dp,
            homeBottomBarWidth = 560.dp,
            homeBottomBarHeight = 72.dp,
            homeBottomIconSize = 56.dp,
            homeBottomSelectedIconSize = 64.dp,
            homeBottomItemWidth = 150.dp
        )
    }
}