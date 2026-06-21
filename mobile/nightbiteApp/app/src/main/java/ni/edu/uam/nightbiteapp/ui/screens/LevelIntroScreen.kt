package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.ui.components.dialogs.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.layout.NightBackgroundType
import ni.edu.uam.nightbiteapp.ui.components.layout.NightScreenContainer
import ni.edu.uam.nightbiteapp.ui.design.NightDimensions
import ni.edu.uam.nightbiteapp.ui.design.NightShapes
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.model.NightLevel
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.DarkText
import ni.edu.uam.nightbiteapp.ui.theme.NightSurface
import ni.edu.uam.nightbiteapp.ui.theme.PizzaRed
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite

@Composable
fun LevelIntroScreen(
    level: NightLevel?,
    onStartLevel: () -> Unit,
    onBackToHome: () -> Unit
) {
    if (level == null) {
        LevelNotFoundDialog(
            onBackToHome = onBackToHome
        )
        return
    }

    NightScreenContainer(
        background = NightBackgroundType.PurplePattern,
        useScreenPadding = false,
        scrollable = false,
        avoidKeyboard = false
    ) { dimensions ->
        val layout = remember(dimensions) {
            levelIntroLayoutFor(dimensions)
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            BackButton(
                layout = layout,
                onBackToHome = onBackToHome,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(
                        start = dimensions.screenHorizontalPadding,
                        top = dimensions.screenVerticalPadding
                    )
            )

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(layout.panelWidth)
                    .height(layout.panelHeight)
            ) {
                LevelInfoPanel(
                    level = level,
                    layout = layout,
                    onStartLevel = onStartLevel,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = layout.panelOffsetY)
                )

                LevelTitleHeader(
                    title = "Nivel ${level.id + 1}",
                    layout = layout,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = layout.headerOffsetY)
                )
            }
        }
    }
}

@Composable
private fun BackButton(
    layout: LevelIntroLayout,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.boton_volver),
        contentDescription = "Volver al mapa",
        modifier = modifier
            .size(layout.backButtonSize)
            .clickable {
                onBackToHome()
            },
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun LevelTitleHeader(
    title: String,
    layout: LevelIntroLayout,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(layout.headerWidth)
            .height(layout.headerTotalHeight),
        contentAlignment = Alignment.TopCenter
    ) {
        Surface(
            modifier = Modifier
                .width(layout.headerWidth)
                .height(layout.headerHeight)
                .offset(y = layout.headerDepthOffset),
            shape = NightShapes.pill,
            color = Color(0xFF6D4BB2)
        ) {}

        Surface(
            modifier = Modifier
                .width(layout.headerWidth)
                .height(layout.headerHeight),
            shape = NightShapes.pill,
            color = Color(0xFFD7C9FF)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = layout.headerTitleSize,
                        color = Color(0xFF5F4BA0)
                    )
                )
            }
        }
    }
}

@Composable
private fun LevelInfoPanel(
    level: NightLevel,
    layout: LevelIntroLayout,
    onStartLevel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(layout.infoPanelWidth)
            .height(layout.infoPanelHeight),
        shape = RoundedCornerShape(layout.panelCornerRadius),
        color = Color(0xFF48CED2)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = layout.panelHorizontalPadding,
                    end = layout.panelHorizontalPadding,
                    top = layout.panelTopPadding,
                    bottom = layout.panelBottomPadding
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LevelEnemySide(
                level = level,
                layout = layout
            )

            Box(
                modifier = Modifier
                    .width(layout.dividerWidth)
                    .height(layout.dividerHeight)
                    .background(NightSurface.copy(alpha = 0.62f))
            )

            Spacer(modifier = Modifier.width(layout.dividerSpacing))

            LevelTextSide(
                level = level,
                layout = layout,
                onStartLevel = onStartLevel
            )
        }
    }
}

@Composable
private fun LevelEnemySide(
    level: NightLevel,
    layout: LevelIntroLayout
) {
    Column(
        modifier = Modifier.width(layout.leftColumnWidth),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        EnemyPlaceholder(
            layout = layout
        )

        Spacer(modifier = Modifier.height(layout.warningTopSpacing))

        WarningTriangle(
            layout = layout
        )

        Spacer(modifier = Modifier.height(layout.warningTextSpacing))

        Text(
            text = warningTextForEnemy(level.enemyName),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = layout.warningTextSize,
                color = PizzaRed,
                lineHeight = layout.warningLineHeight
            )
        )
    }
}

@Composable
private fun EnemyPlaceholder(
    layout: LevelIntroLayout
) {
    Box(
        modifier = Modifier
            .size(
                width = layout.enemyImageWidth,
                height = layout.enemyImageHeight
            )
            .background(
                color = SmokeWhite,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = layout.enemyImageBorder,
                color = Color(0xFF174D74),
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "?",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displaySmall.copy(
                fontSize = layout.enemyQuestionSize,
                color = Color(0xFF174D74)
            )
        )
    }
}

@Composable
private fun WarningTriangle(
    layout: LevelIntroLayout
) {
    Box(
        modifier = Modifier
            .width(layout.warningTriangleWidth)
            .height(layout.warningTriangleHeight),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val trianglePath = Path().apply {
                moveTo(size.width / 2f, 0f)
                lineTo(0f, size.height)
                lineTo(size.width, size.height)
                close()
            }

            drawPath(
                path = trianglePath,
                color = Color(0xFFFF1010)
            )
        }

        Text(
            text = "!",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = layout.warningIconSize,
                color = SmokeWhite
            ),
            modifier = Modifier.offset(y = layout.warningIconOffsetY)
        )
    }
}

@Composable
private fun LevelTextSide(
    level: NightLevel,
    layout: LevelIntroLayout,
    onStartLevel: () -> Unit
) {
    Column(
        modifier = Modifier.width(layout.rightColumnWidth),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MessageReceivedCard(
            message = level.narrativeMessage,
            layout = layout
        )

        Spacer(modifier = Modifier.height(layout.infoCardsSpacing))

        LevelDetailsCard(
            description = level.enemyDescription,
            behavior = level.enemyBehavior,
            tip = level.survivalTip,
            layout = layout
        )

        Spacer(modifier = Modifier.height(layout.playButtonTopSpacing))

        PlayButton(
            layout = layout,
            onStartLevel = onStartLevel
        )
    }
}

@Composable
private fun MessageReceivedCard(
    message: String,
    layout: LevelIntroLayout
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        WhiteInfoCard(
            modifier = Modifier
                .width(layout.messageCardWidth)
                .height(layout.messageCardHeight)
        ) {
            Text(
                text = "Mensaje recibido",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = layout.sectionTitleSize,
                    color = Color(0xFF111060)
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = layout.bodyTextSize,
                    color = DarkText,
                    lineHeight = layout.bodyLineHeight
                ),
                maxLines = layout.messageMaxLines
            )
        }

        Spacer(modifier = Modifier.width(layout.messageIconSpacing))

        MessageIcon(
            layout = layout
        )
    }
}

@Composable
private fun MessageIcon(
    layout: LevelIntroLayout
) {
    Canvas(
        modifier = Modifier.size(layout.messageIconSize)
    ) {
        val strokeColor = Color(0xFF18718E)
        val strokeWidth = layout.messageIconStroke.toPx()

        drawRoundRect(
            color = strokeColor,
            topLeft = Offset(
                x = size.width * 0.10f,
                y = size.height * 0.16f
            ),
            size = androidx.compose.ui.geometry.Size(
                width = size.width * 0.76f,
                height = size.height * 0.52f
            ),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                x = size.width * 0.12f,
                y = size.width * 0.12f
            ),
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = strokeWidth
            )
        )

        val tailPath = Path().apply {
            moveTo(size.width * 0.36f, size.height * 0.68f)
            lineTo(size.width * 0.28f, size.height * 0.90f)
            lineTo(size.width * 0.56f, size.height * 0.68f)
        }

        drawPath(
            path = tailPath,
            color = strokeColor,
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = strokeWidth
            )
        )

        repeat(3) { index ->
            drawCircle(
                color = strokeColor,
                radius = size.width * 0.035f,
                center = Offset(
                    x = size.width * (0.30f + index * 0.16f),
                    y = size.height * 0.42f
                )
            )
        }
    }
}

@Composable
private fun LevelDetailsCard(
    description: String,
    behavior: String,
    tip: String,
    layout: LevelIntroLayout
) {
    WhiteInfoCard(
        modifier = Modifier
            .width(layout.detailsCardWidth)
            .height(layout.detailsCardHeight)
    ) {
        DetailSection(
            title = "Descripción",
            content = description,
            layout = layout
        )

        Spacer(modifier = Modifier.height(layout.detailSectionSpacing))

        DetailSection(
            title = "Comportamiento",
            content = behavior,
            layout = layout
        )

        Spacer(modifier = Modifier.height(layout.detailSectionSpacing))

        DetailSection(
            title = "Consejo",
            content = tip,
            layout = layout
        )
    }
}

@Composable
private fun DetailSection(
    title: String,
    content: String,
    layout: LevelIntroLayout
) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium.copy(
            fontSize = layout.sectionTitleSize,
            color = Color(0xFF111060)
        )
    )

    Spacer(modifier = Modifier.height(2.dp))

    Text(
        text = content,
        style = MaterialTheme.typography.bodySmall.copy(
            fontSize = layout.bodyTextSize,
            color = DarkText,
            lineHeight = layout.bodyLineHeight
        ),
        maxLines = layout.detailMaxLines
    )
}

@Composable
private fun WhiteInfoCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.border(
            width = 2.5.dp,
            color = Color(0xFF174D74),
            shape = RoundedCornerShape(6.dp)
        ),
        shape = RoundedCornerShape(6.dp),
        color = SmokeWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = NightSpacing.small,
                    vertical = NightSpacing.small
                ),
            content = content
        )
    }
}

@Composable
private fun PlayButton(
    layout: LevelIntroLayout,
    onStartLevel: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(layout.playButtonWidth)
            .height(layout.playButtonHeight)
            .clickable {
                onStartLevel()
            },
        shape = NightShapes.pill,
        color = Color(0xFFD7C9FF)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "JUGAR",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = layout.playButtonTextSize,
                    color = Color(0xFF6B57A5)
                )
            )
        }
    }
}

private fun warningTextForEnemy(
    enemyName: String
): String {
    return when (enemyName) {
        "Sin enemigo" -> "NO ENTRES AL\nRESTAURANTE"
        "Sombra errante" -> "NO MIRES LAS\nSOMBRAS"
        "Perro espectral" -> "MANEJA LO MÁS\nRÁPIDO QUE PUEDAS"
        "Cliente deforme" -> "NO TE ACERQUES\nDEMASIADO"
        "Repartidor perdido" -> "NO SIGAS SU\nRUTA"
        else -> "SOBREVIVE LA\nJORNADA"
    }
}

@Composable
private fun LevelNotFoundDialog(
    onBackToHome: () -> Unit
) {
    NightMessageDialog(
        title = "Nivel no encontrado",
        message = "No se pudo cargar la información de esta noche.",
        confirmText = "Volver",
        dismissText = null,
        icon = Icons.Default.Warning,
        iconColor = CheeseYellow,
        onConfirm = onBackToHome,
        onDismiss = onBackToHome
    )
}

private fun levelIntroLayoutFor(
    dimensions: NightDimensions
): LevelIntroLayout {
    return when {
        dimensions.contentMaxWidth <= 560.dp -> LevelIntroLayout(
            backButtonSize = 52.dp,

            panelWidth = 520.dp,
            panelHeight = 300.dp,
            panelOffsetY = 4.dp,

            headerWidth = 220.dp,
            headerHeight = 50.dp,
            headerTotalHeight = 60.dp,
            headerDepthOffset = 7.dp,
            headerTitleSize = 21.sp,
            headerOffsetY = 0.dp,

            infoPanelWidth = 470.dp,
            infoPanelHeight = 260.dp,
            panelCornerRadius = 18.dp,
            panelHorizontalPadding = 20.dp,
            panelTopPadding = 34.dp,
            panelBottomPadding = 12.dp,

            leftColumnWidth = 120.dp,
            rightColumnWidth = 278.dp,
            dividerWidth = 3.dp,
            dividerHeight = 208.dp,
            dividerSpacing = 16.dp,

            enemyImageWidth = 88.dp,
            enemyImageHeight = 76.dp,
            enemyImageBorder = 5.dp,
            enemyQuestionSize = 32.sp,

            warningTopSpacing = 14.dp,
            warningTriangleWidth = 42.dp,
            warningTriangleHeight = 36.dp,
            warningIconSize = 22.sp,
            warningIconOffsetY = 5.dp,
            warningTextSpacing = 6.dp,
            warningTextSize = 10.sp,
            warningLineHeight = 12.sp,

            messageCardWidth = 236.dp,
            messageCardHeight = 44.dp,
            messageIconSize = 28.dp,
            messageIconSpacing = 8.dp,
            messageIconStroke = 2.dp,
            messageMaxLines = 2,

            detailsCardWidth = 278.dp,
            detailsCardHeight = 116.dp,
            infoCardsSpacing = 8.dp,
            detailSectionSpacing = 4.dp,
            detailMaxLines = 2,

            sectionTitleSize = 11.sp,
            bodyTextSize = 7.sp,
            bodyLineHeight = 9.sp,

            playButtonTopSpacing = 10.dp,
            playButtonWidth = 130.dp,
            playButtonHeight = 32.dp,
            playButtonTextSize = 13.sp
        )

        dimensions.contentMaxWidth <= 720.dp -> LevelIntroLayout(
            backButtonSize = 60.dp,

            panelWidth = 650.dp,
            panelHeight = 340.dp,
            panelOffsetY = 4.dp,

            headerWidth = 260.dp,
            headerHeight = 54.dp,
            headerTotalHeight = 64.dp,
            headerDepthOffset = 8.dp,
            headerTitleSize = 25.sp,
            headerOffsetY = 0.dp,

            infoPanelWidth = 570.dp,
            infoPanelHeight = 292.dp,
            panelCornerRadius = 20.dp,
            panelHorizontalPadding = 26.dp,
            panelTopPadding = 40.dp,
            panelBottomPadding = 16.dp,

            leftColumnWidth = 142.dp,
            rightColumnWidth = 350.dp,
            dividerWidth = 3.dp,
            dividerHeight = 234.dp,
            dividerSpacing = 20.dp,

            enemyImageWidth = 104.dp,
            enemyImageHeight = 90.dp,
            enemyImageBorder = 5.dp,
            enemyQuestionSize = 38.sp,

            warningTopSpacing = 16.dp,
            warningTriangleWidth = 50.dp,
            warningTriangleHeight = 42.dp,
            warningIconSize = 26.sp,
            warningIconOffsetY = 6.dp,
            warningTextSpacing = 7.dp,
            warningTextSize = 12.sp,
            warningLineHeight = 14.sp,

            messageCardWidth = 296.dp,
            messageCardHeight = 52.dp,
            messageIconSize = 32.dp,
            messageIconSpacing = 10.dp,
            messageIconStroke = 2.5.dp,
            messageMaxLines = 2,

            detailsCardWidth = 350.dp,
            detailsCardHeight = 128.dp,
            infoCardsSpacing = 9.dp,
            detailSectionSpacing = 5.dp,
            detailMaxLines = 2,

            sectionTitleSize = 13.sp,
            bodyTextSize = 8.sp,
            bodyLineHeight = 10.sp,

            playButtonTopSpacing = 12.dp,
            playButtonWidth = 158.dp,
            playButtonHeight = 36.dp,
            playButtonTextSize = 14.sp
        )

        else -> LevelIntroLayout(
            backButtonSize = 66.dp,

            panelWidth = 760.dp,
            panelHeight = 370.dp,
            panelOffsetY = 4.dp,

            headerWidth = 300.dp,
            headerHeight = 58.dp,
            headerTotalHeight = 68.dp,
            headerDepthOffset = 9.dp,
            headerTitleSize = 29.sp,
            headerOffsetY = 0.dp,

            infoPanelWidth = 640.dp,
            infoPanelHeight = 318.dp,
            panelCornerRadius = 22.dp,
            panelHorizontalPadding = 32.dp,
            panelTopPadding = 44.dp,
            panelBottomPadding = 18.dp,

            leftColumnWidth = 158.dp,
            rightColumnWidth = 398.dp,
            dividerWidth = 3.dp,
            dividerHeight = 258.dp,
            dividerSpacing = 22.dp,

            enemyImageWidth = 118.dp,
            enemyImageHeight = 100.dp,
            enemyImageBorder = 6.dp,
            enemyQuestionSize = 44.sp,

            warningTopSpacing = 18.dp,
            warningTriangleWidth = 56.dp,
            warningTriangleHeight = 46.dp,
            warningIconSize = 28.sp,
            warningIconOffsetY = 6.dp,
            warningTextSpacing = 8.dp,
            warningTextSize = 13.sp,
            warningLineHeight = 16.sp,

            messageCardWidth = 330.dp,
            messageCardHeight = 54.dp,
            messageIconSize = 36.dp,
            messageIconSpacing = 12.dp,
            messageIconStroke = 2.5.dp,
            messageMaxLines = 2,

            detailsCardWidth = 398.dp,
            detailsCardHeight = 142.dp,
            infoCardsSpacing = 10.dp,
            detailSectionSpacing = 6.dp,
            detailMaxLines = 2,

            sectionTitleSize = 15.sp,
            bodyTextSize = 9.sp,
            bodyLineHeight = 11.sp,

            playButtonTopSpacing = 14.dp,
            playButtonWidth = 170.dp,
            playButtonHeight = 38.dp,
            playButtonTextSize = 15.sp
        )
    }
}

private data class LevelIntroLayout(
    val backButtonSize: Dp,

    val panelWidth: Dp,
    val panelHeight: Dp,
    val panelOffsetY: Dp,

    val headerWidth: Dp,
    val headerHeight: Dp,
    val headerTotalHeight: Dp,
    val headerDepthOffset: Dp,
    val headerTitleSize: TextUnit,
    val headerOffsetY: Dp,

    val infoPanelWidth: Dp,
    val infoPanelHeight: Dp,
    val panelCornerRadius: Dp,
    val panelHorizontalPadding: Dp,
    val panelTopPadding: Dp,
    val panelBottomPadding: Dp,

    val leftColumnWidth: Dp,
    val rightColumnWidth: Dp,
    val dividerWidth: Dp,
    val dividerHeight: Dp,
    val dividerSpacing: Dp,

    val enemyImageWidth: Dp,
    val enemyImageHeight: Dp,
    val enemyImageBorder: Dp,
    val enemyQuestionSize: TextUnit,

    val warningTopSpacing: Dp,
    val warningTriangleWidth: Dp,
    val warningTriangleHeight: Dp,
    val warningIconSize: TextUnit,
    val warningIconOffsetY: Dp,
    val warningTextSpacing: Dp,
    val warningTextSize: TextUnit,
    val warningLineHeight: TextUnit,

    val messageCardWidth: Dp,
    val messageCardHeight: Dp,
    val messageIconSize: Dp,
    val messageIconSpacing: Dp,
    val messageIconStroke: Dp,
    val messageMaxLines: Int,

    val detailsCardWidth: Dp,
    val detailsCardHeight: Dp,
    val infoCardsSpacing: Dp,
    val detailSectionSpacing: Dp,
    val detailMaxLines: Int,

    val sectionTitleSize: TextUnit,
    val bodyTextSize: TextUnit,
    val bodyLineHeight: TextUnit,

    val playButtonTopSpacing: Dp,
    val playButtonWidth: Dp,
    val playButtonHeight: Dp,
    val playButtonTextSize: TextUnit
)