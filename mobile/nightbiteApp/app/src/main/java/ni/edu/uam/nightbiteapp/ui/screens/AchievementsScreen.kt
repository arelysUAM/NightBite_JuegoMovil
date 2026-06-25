package ni.edu.uam.nightbiteapp.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.data.local.session.UserSession
import ni.edu.uam.nightbiteapp.ui.components.layout.NightBackgroundType
import ni.edu.uam.nightbiteapp.ui.components.layout.NightScreenContainer
import ni.edu.uam.nightbiteapp.ui.design.NightShapes
import ni.edu.uam.nightbiteapp.ui.theme.AchievementsHeaderBlue
import ni.edu.uam.nightbiteapp.ui.theme.AchievementsInnerContainer
import ni.edu.uam.nightbiteapp.ui.theme.AchievementsItemBlue
import ni.edu.uam.nightbiteapp.ui.theme.AchievementsPanelBody
import ni.edu.uam.nightbiteapp.ui.theme.AchievementsTitlePurple
import ni.edu.uam.nightbiteapp.ui.theme.LilitaOne
import ni.edu.uam.nightbiteapp.ui.theme.NightSurface
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite

@Composable
fun AchievementsScreen(
    userSession: UserSession,
    currentLevel: Int,
    starsByLevel: Map<Int, Int>,
    onBackToHome: () -> Unit
) {
    NightScreenContainer(
        background = NightBackgroundType.BluePattern,
        useScreenPadding = true,
        scrollable = false,
        avoidKeyboard = false
    ) { _ ->
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val layout = achievementsLayoutFor(
                maxWidth = maxWidth,
                maxHeight = maxHeight
            )

            AchievementsPanel(
                username = userSession.username.ifBlank {
                    "NombreUsuario"
                },
                currentLevel = currentLevel.coerceIn(1, 5),
                starsByLevel = starsByLevel,
                layout = layout,
                onBackToHome = onBackToHome
            )
        }
    }
}

@Composable
private fun AchievementsPanel(
    username: String,
    currentLevel: Int,
    starsByLevel: Map<Int, Int>,
    layout: AchievementsLayout,
    onBackToHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(layout.panelWidth)
            .height(layout.panelHeight)
            .clip(NightShapes.panel)
            .background(AchievementsPanelBody),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AchievementsHeader(
            username = username,
            currentLevel = currentLevel,
            height = layout.headerHeight,
            closeButtonSize = layout.closeButtonSize,
            onBackToHome = onBackToHome
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(
                    start = layout.contentHorizontalPadding,
                    end = layout.contentHorizontalPadding,
                    top = layout.contentTopPadding,
                    bottom = layout.contentBottomPadding
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SectionTitle(
                text = "INSIGNIAS",
                fontSize = layout.sectionTitleSize
            )

            Spacer(modifier = Modifier.height(layout.badgesTitleSpacing))

            BadgesSection(
                starsByLevel = starsByLevel,
                layout = layout
            )

            Spacer(modifier = Modifier.height(layout.dividerTopSpacing))

            HorizontalDividerLine()

            Spacer(modifier = Modifier.height(layout.recordsTopSpacing))

            RecordsSection(
                layout = layout
            )
        }
    }
}

@Composable
private fun AchievementsHeader(
    username: String,
    currentLevel: Int,
    height: Dp,
    closeButtonSize: Dp,
    onBackToHome: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(AchievementsHeaderBlue)
            .padding(horizontal = 22.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.align(Alignment.CenterStart),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            HeaderText(
                text = username,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            HeaderText(
                text = "LV. $currentLevel",
                fontSize = 18.sp
            )
        }

        ImageButton(
            drawableId = R.drawable.boton_cancelar,
            contentDescription = "Cerrar logros",
            size = closeButtonSize,
            onClick = onBackToHome,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

@Composable
private fun HeaderText(
    text: String,
    fontSize: TextUnit
) {
    Text(
        text = text,
        color = SmokeWhite,
        fontSize = fontSize,
        fontFamily = LilitaOne,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.titleMedium.copy(
            shadow = Shadow(
                color = NightSurface.copy(alpha = 0.45f),
                offset = Offset(1.5f, 1.5f),
                blurRadius = 2f
            )
        )
    )
}

@Composable
private fun SectionTitle(
    text: String,
    fontSize: TextUnit
) {
    Text(
        text = text,
        color = AchievementsTitlePurple,
        fontSize = fontSize,
        fontFamily = LilitaOne,
        fontWeight = FontWeight.Normal,
        letterSpacing = 1.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun BadgesSection(
    starsByLevel: Map<Int, Int>,
    layout: AchievementsLayout
) {
    Row(
        modifier = Modifier
            .width(layout.badgesContainerWidth)
            .height(layout.badgesContainerHeight)
            .clip(RoundedCornerShape(18.dp))
            .background(AchievementsInnerContainer)
            .padding(layout.badgesGap),
        horizontalArrangement = Arrangement.spacedBy(
            space = layout.badgesGap,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(TOTAL_LEVELS) { index ->
            BadgeSlot(
                levelIndex = index,
                earned = (starsByLevel[index] ?: 0) >= REQUIRED_STARS_FOR_BADGE,
                size = layout.badgeSlotSize,
                badgeSize = layout.badgeImageSize
            )
        }
    }
}

@Composable
private fun BadgeSlot(
    levelIndex: Int,
    earned: Boolean,
    size: Dp,
    badgeSize: Dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(18.dp))
            .background(AchievementsItemBlue),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(
                id = if (earned) {
                    badgeDrawableFor(levelIndex)
                } else {
                    R.drawable.insignia_faltante
                }
            ),
            contentDescription = if (earned) {
                "Insignia nivel ${levelIndex + 1}"
            } else {
                "Insignia faltante nivel ${levelIndex + 1}"
            },
            modifier = Modifier.size(badgeSize),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun HorizontalDividerLine() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(3.dp)
            .background(SmokeWhite.copy(alpha = 0.95f))
    )
}

@Composable
private fun RecordsSection(
    layout: AchievementsLayout
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "RECORDS",
            color = AchievementsTitlePurple,
            fontSize = layout.recordsTitleSize,
            fontFamily = LilitaOne,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.8.sp,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(layout.recordsTitleSpacing))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(layout.recordsRowHeight),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            RecordsGroup(
                layout = layout
            )

            Spacer(modifier = Modifier.width(layout.recordsMiddleSpacing))

            Box(
                modifier = Modifier
                    .width(3.dp)
                    .fillMaxHeight()
                    .background(SmokeWhite.copy(alpha = 0.95f))
            )

            Spacer(modifier = Modifier.width(layout.recordsMiddleSpacing))

            RecordsGroup(
                layout = layout
            )
        }
    }
}

@Composable
private fun RecordsGroup(
    layout: AchievementsLayout
) {
    Row(
        modifier = Modifier
            .width(layout.recordsGroupWidth)
            .height(layout.recordsGroupHeight)
            .clip(RoundedCornerShape(18.dp))
            .background(AchievementsInnerContainer)
            .padding(layout.recordsGap),
        horizontalArrangement = Arrangement.spacedBy(
            space = layout.recordsGap,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(4) {
            RecordPlaceholder(
                size = layout.recordBoxSize,
                plusSize = layout.recordPlusSize
            )
        }
    }
}

@Composable
private fun RecordPlaceholder(
    size: Dp,
    plusSize: TextUnit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(18.dp))
            .background(AchievementsItemBlue),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "+",
            color = SmokeWhite,
            fontSize = plusSize,
            fontFamily = LilitaOne,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ImageButton(
    @DrawableRes drawableId: Int,
    contentDescription: String,
    size: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Image(
        painter = painterResource(id = drawableId),
        contentDescription = contentDescription,
        modifier = modifier
            .size(size)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            },
        contentScale = ContentScale.Fit
    )
}

@DrawableRes
private fun badgeDrawableFor(
    levelIndex: Int
): Int {
    return when (levelIndex.coerceIn(0, 4)) {
        0 -> R.drawable.insignia_lv1
        1 -> R.drawable.insignia_lv2
        2 -> R.drawable.insignia_lv3
        3 -> R.drawable.insignia_lv4
        else -> R.drawable.insignia_lv5
    }
}

private data class AchievementsLayout(
    val panelWidth: Dp,
    val panelHeight: Dp,
    val headerHeight: Dp,
    val closeButtonSize: Dp,
    val contentHorizontalPadding: Dp,
    val contentTopPadding: Dp,
    val contentBottomPadding: Dp,
    val sectionTitleSize: TextUnit,
    val badgesTitleSpacing: Dp,
    val badgesContainerWidth: Dp,
    val badgesContainerHeight: Dp,
    val badgesGap: Dp,
    val badgeSlotSize: Dp,
    val badgeImageSize: Dp,
    val dividerTopSpacing: Dp,
    val recordsTopSpacing: Dp,
    val recordsTitleSize: TextUnit,
    val recordsTitleSpacing: Dp,
    val recordsRowHeight: Dp,
    val recordsGroupWidth: Dp,
    val recordsGroupHeight: Dp,
    val recordsGap: Dp,
    val recordsMiddleSpacing: Dp,
    val recordBoxSize: Dp,
    val recordPlusSize: TextUnit
)

private fun achievementsLayoutFor(
    maxWidth: Dp,
    maxHeight: Dp
): AchievementsLayout {
    val compactHeight = maxHeight < 390.dp

    val panelWidth = (maxWidth * 0.84f).coerceIn(
        minimumValue = 640.dp,
        maximumValue = 890.dp
    )

    val desiredPanelHeight = if (compactHeight) {
        340.dp
    } else {
        354.dp
    }

    val panelHeight = desiredPanelHeight.coerceAtMost(
        maxHeight - 24.dp
    )

    val contentHorizontalPadding = 22.dp
    val badgesGap = if (compactHeight) 9.dp else 11.dp
    val recordsGap = if (compactHeight) 9.dp else 11.dp

    val badgesContainerHeight = if (compactHeight) {
        78.dp
    } else {
        88.dp
    }

    val badgeSlotSize = badgesContainerHeight - (badgesGap * 2)

    val recordsGroupHeight = if (compactHeight) {
        60.dp
    } else {
        68.dp
    }

    val recordBoxSize = recordsGroupHeight - (recordsGap * 2)

    return AchievementsLayout(
        panelWidth = panelWidth,
        panelHeight = panelHeight,
        headerHeight = if (compactHeight) 64.dp else 76.dp,
        closeButtonSize = if (compactHeight) 52.dp else 58.dp,
        contentHorizontalPadding = contentHorizontalPadding,
        contentTopPadding = if (compactHeight) 12.dp else 16.dp,
        contentBottomPadding = if (compactHeight) 14.dp else 18.dp,
        sectionTitleSize = if (compactHeight) 15.sp else 17.sp,
        badgesTitleSpacing = if (compactHeight) 6.dp else 8.dp,
        badgesContainerWidth = panelWidth * 0.74f,
        badgesContainerHeight = badgesContainerHeight,
        badgesGap = badgesGap,
        badgeSlotSize = badgeSlotSize,
        badgeImageSize = badgeSlotSize * 0.82f,
        dividerTopSpacing = if (compactHeight) 12.dp else 14.dp,
        recordsTopSpacing = if (compactHeight) 8.dp else 10.dp,
        recordsTitleSize = if (compactHeight) 14.sp else 16.sp,
        recordsTitleSpacing = if (compactHeight) 4.dp else 6.dp,
        recordsRowHeight = if (compactHeight) 64.dp else 74.dp,
        recordsGroupWidth = panelWidth * 0.36f,
        recordsGroupHeight = recordsGroupHeight,
        recordsGap = recordsGap,
        recordsMiddleSpacing = if (compactHeight) 24.dp else 30.dp,
        recordBoxSize = recordBoxSize,
        recordPlusSize = if (compactHeight) 22.sp else 26.sp
    )
}

private const val TOTAL_LEVELS = 5
private const val REQUIRED_STARS_FOR_BADGE = 3