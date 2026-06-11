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
    val ageCheckCardSize: Dp,
    val agePickerWidth: Dp
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
            ageCheckCardSize = 320.dp,
            agePickerWidth = 210.dp
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
            ageCheckCardSize = 420.dp,
            agePickerWidth = 280.dp
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
            ageCheckCardSize = 500.dp,
            agePickerWidth = 320.dp
        )
    }
}