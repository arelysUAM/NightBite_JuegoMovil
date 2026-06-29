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
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@Composable
fun AchievementsScreen(
    userSession: UserSession,
    currentLevel: Int,
    earnedBadgeLevels: Set<Int>,
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

            var selectedRecord by remember {
                mutableStateOf<AchievementRecord?>(null)
            }

            AchievementsPanel(
                username = userSession.username.ifBlank {
                    "NombreUsuario"
                },
                currentLevel = currentLevel.coerceIn(1, 5),
                earnedBadgeLevels = earnedBadgeLevels,
                layout = layout,
                onRecordClick = { record ->
                    selectedRecord = record
                },
                onBackToHome = onBackToHome
            )

            selectedRecord?.let { record ->
                AchievementRecordInfoDialog(
                    record = record,
                    onClose = {
                        selectedRecord = null
                    }
                )
            }
        }
    }
}

@Composable
private fun AchievementsPanel(
    username: String,
    currentLevel: Int,
    earnedBadgeLevels: Set<Int>,
    layout: AchievementsLayout,
    onRecordClick: (AchievementRecord) -> Unit,
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
                earnedBadgeLevels = earnedBadgeLevels,
                layout = layout
            )

            Spacer(modifier = Modifier.height(layout.dividerTopSpacing))

            HorizontalDividerLine()

            Spacer(modifier = Modifier.height(layout.recordsTopSpacing))

            RecordsSection(
                records = achievementRecords,
                layout = layout,
                onRecordClick = onRecordClick
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
    earnedBadgeLevels: Set<Int>,
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
                earned = index in earnedBadgeLevels,
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
    records: List<AchievementRecord>,
    layout: AchievementsLayout,
    onRecordClick: (AchievementRecord) -> Unit
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
                records = records.take(5),
                layout = layout,
                onRecordClick = onRecordClick
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
                records = records.drop(5),
                layout = layout,
                onRecordClick = onRecordClick
            )
        }
    }
}

@Composable
private fun RecordsGroup(
    records: List<AchievementRecord>,
    layout: AchievementsLayout,
    onRecordClick: (AchievementRecord) -> Unit
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
        records.forEach { record ->
            RecordPlaceholder(
                record = record,
                size = layout.recordBoxSize,
                plusSize = layout.recordPlusSize,
                onClick = {
                    onRecordClick(record)
                }
            )
        }
    }
}

@Composable
private fun RecordPlaceholder(
    record: AchievementRecord,
    size: Dp,
    plusSize: TextUnit,
    onClick: () -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(18.dp))
            .background(AchievementsItemBlue)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            },
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
private fun AchievementRecordInfoDialog(
    record: AchievementRecord,
    onClose: () -> Unit
) {
    BackHandler {
        onClose()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x880B1026))
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null
            ) {
                onClose()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(430.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(AchievementsItemBlue)
                .border(
                    width = 6.dp,
                    color = AchievementsTitlePurple,
                    shape = RoundedCornerShape(30.dp)
                )
                .clickable(
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = null
                ) {
                    // Evita que tocar dentro del cuadro cierre el dialog.
                }
                .padding(
                    horizontal = 28.dp,
                    vertical = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Información del logro",
                    tint = SmokeWhite,
                    modifier = Modifier.size(42.dp)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = record.name,
                    color = SmokeWhite,
                    fontSize = 28.sp,
                    fontFamily = LilitaOne,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 2.dp,
                        color = SmokeWhite.copy(alpha = 0.55f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(
                        horizontal = 42.dp,
                        vertical = 7.dp
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = record.scopeLabel,
                    color = SmokeWhite,
                    fontSize = 17.sp,
                    fontFamily = LilitaOne,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = record.description,
                color = SmokeWhite,
                fontSize = 18.sp,
                fontFamily = LilitaOne,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                lineHeight = 23.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            Box(
                modifier = Modifier
                    .width(210.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(AchievementsTitlePurple)
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = null
                    ) {
                        onClose()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Cerrar",
                    color = SmokeWhite,
                    fontSize = 18.sp,
                    fontFamily = LilitaOne,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        }
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
        recordsGroupWidth = panelWidth * 0.40f,
        recordsGroupHeight = recordsGroupHeight,
        recordsGap = recordsGap,
        recordsMiddleSpacing = if (compactHeight) 16.dp else 20.dp,
        recordBoxSize = recordBoxSize,
        recordPlusSize = if (compactHeight) 22.sp else 26.sp
    )
}

private data class AchievementRecord(
    val id: Int,
    val name: String,
    val scopeLabel: String,
    val description: String
)

private val achievementRecords = listOf(
    AchievementRecord(
        id = 1,
        name = "RUTA IMPECABLE",
        scopeLabel = "CUALQUIER JORNADA",
        description = "Completa una jornada entregando todos los pedidos sin fallar ninguno."
    ),
    AchievementRecord(
        id = 2,
        name = "CONDUCTOR NOCTURNO",
        scopeLabel = "TODAS LAS JORNADAS",
        description = "Completa todas las jornadas, sin importar la cantidad de estrellas obtenidas."
    ),
    AchievementRecord(
        id = 3,
        name = "SIN MIEDO A LA OSCURIDAD",
        scopeLabel = "JORNADA DESBLOQUEADA",
        description = "Completa una jornada que antes estaba bloqueada después de superarla."
    ),
    AchievementRecord(
        id = 4,
        name = "PEDIDO FANTASMA",
        scopeLabel = "ZONA DE PELIGRO",
        description = "Entrega un pedido mientras el tiempo está en zona de peligro."
    ),
    AchievementRecord(
        id = 5,
        name = "NOCHE PERFECTA",
        scopeLabel = "CUALQUIER JORNADA",
        description = "Completa cualquier jornada con 3 estrellas y sin perder vidas."
    ),
    AchievementRecord(
        id = 6,
        name = "CERO RETRASOS",
        scopeLabel = "PROMEDIO DE ENTREGA",
        description = "Completa una jornada con un promedio de entrega de 5 segundos o menos."
    ),
    AchievementRecord(
        id = 7,
        name = "PEDIDO CALIENTE",
        scopeLabel = "ENTREGA RÁPIDA",
        description = "Entrega un pedido en menos de 3 segundos."
    ),
    AchievementRecord(
        id = 8,
        name = "TEMERARIO NOCTURNO",
        scopeLabel = "JORNADA 5",
        description = "Completa la Jornada 5 permaneciendo en la zona segura más de 2 segundos por pedido."
    ),
    AchievementRecord(
        id = 9,
        name = "REPARTIDOR DEL MES",
        scopeLabel = "TODAS LAS JORNADAS",
        description = "Completa todas las jornadas con 3 estrellas."
    ),
    AchievementRecord(
        id = 10,
        name = "LEYENDA DE NIGHTBITE",
        scopeLabel = "LOGRO FINAL",
        description = "Desbloquea todas las insignias y todos los logros disponibles."
    )
)

private const val TOTAL_LEVELS = 5