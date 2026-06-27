package ni.edu.uam.nightbiteapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.data.local.mock.GameResultsData
import ni.edu.uam.nightbiteapp.ui.model.GameResultContent
import ni.edu.uam.nightbiteapp.ui.model.GameResultPalette
import ni.edu.uam.nightbiteapp.ui.model.GameResultType
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.LilitaOne
import ni.edu.uam.nightbiteapp.ui.theme.ResultPurpleBody
import ni.edu.uam.nightbiteapp.ui.theme.ResultPurpleCard
import ni.edu.uam.nightbiteapp.ui.theme.ResultPurpleHeader
import ni.edu.uam.nightbiteapp.ui.theme.ResultPurpleTab
import ni.edu.uam.nightbiteapp.ui.theme.ResultRedAccent
import ni.edu.uam.nightbiteapp.ui.theme.ResultRedBody
import ni.edu.uam.nightbiteapp.ui.theme.ResultRedCard
import ni.edu.uam.nightbiteapp.ui.theme.ResultRedHeader
import ni.edu.uam.nightbiteapp.ui.theme.ResultRedHeaderText
import ni.edu.uam.nightbiteapp.ui.theme.ResultRedMetricBackground
import ni.edu.uam.nightbiteapp.ui.theme.ResultRedTab
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite

@Composable
fun GameResultScreen(
    levelId: Int,
    resultType: GameResultType,
    stars: Int,
    runtimeTimeText: String? = null,
    runtimeCompletedOrders: Int? = null,
    runtimeTotalOrders: Int? = null,
    runtimeAverageDeliveryTimeText: String? = null,
    onContinueToNextLevel: () -> Unit,
    onRetryLevel: () -> Unit,
    onBackToHome: () -> Unit
){
    val content = GameResultsData.getResultContent(
        levelId = levelId,
        resultType = resultType,
        stars = stars
    )

    BackHandler {
        onBackToHome()
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val layout = resultLayoutFor(
            maxWidth = maxWidth,
            maxHeight = maxHeight
        )

        val colors = resultColorsFor(
            palette = content.palette
        )

        ResultBackground(
            palette = content.palette
        )

        ResultCard(
            content = content,
            resultType = resultType,
            layout = layout,
            colors = colors,
            runtimeTimeText = runtimeTimeText,
            runtimeCompletedOrders = runtimeCompletedOrders,
            runtimeTotalOrders = runtimeTotalOrders,
            onContinueToNextLevel = onContinueToNextLevel,
            runtimeAverageDeliveryTimeText = runtimeAverageDeliveryTimeText,
            onRetryLevel = onRetryLevel,
            onBackToHome = onBackToHome,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ResultBackground(
    palette: GameResultPalette
) {
    val backgroundDrawable = when (palette) {
        GameResultPalette.PURPLE -> R.drawable.fondo_estampado_morado
        GameResultPalette.RED -> R.drawable.partida_perdida
    }

    Image(
        painter = painterResource(id = backgroundDrawable),
        contentDescription = "Fondo de resultado",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun ResultCard(
    content: GameResultContent,
    resultType: GameResultType,
    layout: ResultLayout,
    colors: ResultCardColors,
    runtimeTimeText: String?,
    runtimeCompletedOrders: Int?,
    runtimeTotalOrders: Int?,
    runtimeAverageDeliveryTimeText: String?,
    onContinueToNextLevel: () -> Unit,
    onRetryLevel: () -> Unit,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(layout.cardWidth)
            .height(layout.cardHeight),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(layout.cardWidth)
                .height(layout.cardBodyHeight)
                .clip(RoundedCornerShape(24.dp))
                .background(colors.cardBackground)
                .border(
                    width = 3.dp,
                    color = colors.headerBackground,
                    shape = RoundedCornerShape(24.dp)
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = layout.headerDepthOffset)
                .width(layout.headerWidth)
                .height(layout.headerHeight)
                .clip(RoundedCornerShape(18.dp))
                .background(colors.headerDepthBackground)
        )

        ResultHeader(
            title = content.title,
            layout = layout,
            colors = colors,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(layout.cardWidth)
                .height(layout.cardBodyHeight)
                .padding(
                    start = layout.bodyHorizontalPadding,
                    end = layout.bodyHorizontalPadding,
                    top = layout.bodyTopPadding,
                    bottom = layout.bodyBottomPadding
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ResultSubtitle(
                subtitle = content.subtitle,
                message = content.message,
                layout = layout,
                colors = colors
            )

            Spacer(modifier = Modifier.height(layout.subtitleToStarsSpacing))

            Image(
                painter = painterResource(
                    id = starsDrawableFor(content.safeStars)
                ),
                contentDescription = "Resultado de estrellas",
                modifier = Modifier
                    .width(layout.starsWidth)
                    .height(layout.starsHeight),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(layout.starsToMetricsSpacing))

            ResultMetricsBlock(
                content = content,
                layout = layout,
                colors = colors,
                runtimeTimeText = runtimeTimeText,
                runtimeCompletedOrders = runtimeCompletedOrders,
                runtimeTotalOrders = runtimeTotalOrders,
                runtimeAverageDeliveryTimeText = runtimeAverageDeliveryTimeText
            )

            Spacer(modifier = Modifier.height(layout.metricsToActionsSpacing))

            ResultActions(
                content = content,
                resultType = resultType,
                layout = layout,
                onContinueToNextLevel = onContinueToNextLevel,
                onRetryLevel = onRetryLevel,
                onBackToHome = onBackToHome
            )
        }
    }
}

@Composable
private fun ResultHeader(
    title: String,
    layout: ResultLayout,
    colors: ResultCardColors,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(layout.headerWidth)
            .height(layout.headerHeight)
            .clip(RoundedCornerShape(18.dp))
            .background(colors.headerBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title.uppercase(),
            color = colors.headerText,
            fontSize = layout.titleSize,
            fontFamily = LilitaOne,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.8.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge.copy(
                shadow = Shadow(
                    color = colors.titleShadow,
                    offset = Offset(2f, 2f),
                    blurRadius = 2f
                )
            )
        )
    }
}

@Composable
private fun ResultSubtitle(
    subtitle: String,
    message: String,
    layout: ResultLayout,
    colors: ResultCardColors
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = subtitle,
            color = colors.primaryText,
            fontSize = layout.subtitleSize,
            fontFamily = LilitaOne,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            lineHeight = layout.subtitleLineHeight
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = message,
            color = colors.secondaryText,
            fontSize = layout.messageSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = layout.messageLineHeight,
            modifier = Modifier.width(layout.messageWidth)
        )
    }
}

@Composable
private fun ResultMetricsBlock(
    content: GameResultContent,
    layout: ResultLayout,
    colors: ResultCardColors,
    runtimeTimeText: String?,
    runtimeCompletedOrders: Int?,
    runtimeTotalOrders: Int?,
    runtimeAverageDeliveryTimeText: String?
){

    val ordersText = runtimeOrdersText(
        completedOrders = runtimeCompletedOrders,
        totalOrders = runtimeTotalOrders
    ) ?: content.ordersText

    Row(
        modifier = Modifier
            .width(layout.metricsWidth)
            .height(layout.metricsHeight),
        horizontalArrangement = Arrangement.spacedBy(
            space = layout.metricSpacing,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ResultMetric(
            label = "TIEMPO",
            value = runtimeTimeText ?: content.timeText,
            width = layout.metricWidth,
            colors = colors
        )

        ResultMetric(
            label = "PEDIDOS",
            value = ordersText,
            width = layout.metricWidth,
            colors = colors
        )

        ResultMetric(
            label = "PROM. ENTREGA",
            value = runtimeAverageDeliveryTimeText ?: "--",
            width = layout.metricWidth,
            colors = colors
        )
    }
}

@Composable
private fun ResultMetric(
    label: String,
    value: String,
    width: Dp,
    colors: ResultCardColors
) {
    Column(
        modifier = Modifier
            .width(width)
            .clip(RoundedCornerShape(14.dp))
            .background(colors.metricBackground)
            .padding(
                horizontal = 8.dp,
                vertical = 8.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = colors.metricLabel,
            fontSize = 10.sp,
            fontFamily = LilitaOne,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.5.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = value,
            color = colors.metricValue,
            fontSize = 15.sp,
            fontFamily = LilitaOne,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ResultActions(
    content: GameResultContent,
    resultType: GameResultType,
    layout: ResultLayout,
    onContinueToNextLevel: () -> Unit,
    onRetryLevel: () -> Unit,
    onBackToHome: () -> Unit
) {
    Row(
        modifier = Modifier.width(layout.actionsWidth),
        horizontalArrangement = Arrangement.spacedBy(
            space = layout.actionSpacing,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (content.showRetryButton) {
            ResultImageButton(
                drawableId = R.drawable.boton_reintentar,
                contentDescription = "Reintentar nivel",
                size = layout.sideActionButtonSize,
                onClick = onRetryLevel
            )
        }

        if (content.showContinueButton) {
            ResultImageButton(
                drawableId = R.drawable.boton_siguiente,
                contentDescription = continueButtonDescription(resultType),
                size = layout.continueActionButtonSize,
                onClick = onContinueToNextLevel
            )
        }

        if (content.showHomeButton) {
            ResultImageButton(
                drawableId = R.drawable.boton_home,
                contentDescription = "Volver al Home",
                size = layout.sideActionButtonSize,
                onClick = onBackToHome
            )
        }
    }
}

@Composable
private fun ResultImageButton(
    @DrawableRes drawableId: Int,
    contentDescription: String,
    size: Dp,
    onClick: () -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        label = "resultImageButtonScale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isPressed) 0.78f else 1f,
        label = "resultImageButtonAlpha"
    )

    Image(
        painter = painterResource(id = drawableId),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(size)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
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
private fun starsDrawableFor(
    stars: Int
): Int {
    return when (stars.coerceIn(0, 3)) {
        0 -> R.drawable.estrellas_0
        1 -> R.drawable.estrellas_1
        2 -> R.drawable.estrellas_2
        else -> R.drawable.estrellas_3
    }
}

private fun runtimeOrdersText(
    completedOrders: Int?,
    totalOrders: Int?
): String? {
    if (completedOrders == null || totalOrders == null) return null

    val safeTotalOrders = totalOrders.coerceAtLeast(0)
    val safeCompletedOrders = completedOrders.coerceIn(
        minimumValue = 0,
        maximumValue = safeTotalOrders
    )

    return "$safeCompletedOrders/$safeTotalOrders"
}

private fun continueButtonDescription(
    resultType: GameResultType
): String {
    return if (resultType == GameResultType.FINAL_WIN) {
        "Finalizar juego"
    } else {
        "Ir al siguiente nivel"
    }
}

private data class ResultLayout(
    val cardWidth: Dp,
    val cardHeight: Dp,
    val cardBodyHeight: Dp,
    val headerWidth: Dp,
    val headerHeight: Dp,
    val headerDepthOffset: Dp,
    val bodyHorizontalPadding: Dp,
    val bodyTopPadding: Dp,
    val bodyBottomPadding: Dp,
    val titleSize: TextUnit,
    val subtitleSize: TextUnit,
    val subtitleLineHeight: TextUnit,
    val messageSize: TextUnit,
    val messageLineHeight: TextUnit,
    val messageWidth: Dp,
    val subtitleToStarsSpacing: Dp,
    val starsWidth: Dp,
    val starsHeight: Dp,
    val starsToMetricsSpacing: Dp,
    val metricsWidth: Dp,
    val metricsHeight: Dp,
    val metricWidth: Dp,
    val metricSpacing: Dp,
    val metricsToActionsSpacing: Dp,
    val actionsWidth: Dp,
    val actionSpacing: Dp,
    val sideActionButtonSize: Dp,
    val continueActionButtonSize: Dp
)

private fun resultLayoutFor(
    maxWidth: Dp,
    maxHeight: Dp
): ResultLayout {
    val compactHeight = maxHeight < 390.dp

    val cardWidth = (maxWidth * 0.56f).coerceIn(
        minimumValue = 520.dp,
        maximumValue = 650.dp
    )

    val cardHeight = if (compactHeight) {
        318.dp
    } else {
        350.dp
    }.coerceAtMost(maxHeight - 24.dp)

    val cardBodyHeight = cardHeight - if (compactHeight) {
        34.dp
    } else {
        38.dp
    }

    val metricSpacing = if (compactHeight) 12.dp else 16.dp

    val metricsWidth = cardWidth * 0.74f

    val metricWidth =
        ((metricsWidth - (metricSpacing * 2)) / 3).coerceIn(
            minimumValue = 105.dp,
            maximumValue = 135.dp
        )

    return ResultLayout(
        cardWidth = cardWidth,
        cardHeight = cardHeight,
        cardBodyHeight = cardBodyHeight,
        headerWidth = if (compactHeight) 275.dp else 310.dp,
        headerHeight = if (compactHeight) 54.dp else 62.dp,
        headerDepthOffset = if (compactHeight) 7.dp else 8.dp,

        bodyHorizontalPadding = if (compactHeight) 34.dp else 42.dp,
        bodyTopPadding = if (compactHeight) 38.dp else 42.dp,
        bodyBottomPadding = if (compactHeight) 8.dp else 12.dp,

        titleSize = if (compactHeight) 22.sp else 25.sp,
        subtitleSize = if (compactHeight) 18.sp else 20.sp,
        subtitleLineHeight = if (compactHeight) 20.sp else 22.sp,
        messageSize = if (compactHeight) 11.sp else 12.sp,
        messageLineHeight = if (compactHeight) 13.sp else 14.sp,
        messageWidth = cardWidth * 0.78f,

        subtitleToStarsSpacing = if (compactHeight) 6.dp else 8.dp,
        starsWidth = if (compactHeight) 190.dp else 215.dp,
        starsHeight = if (compactHeight) 40.dp else 46.dp,
        starsToMetricsSpacing = if (compactHeight) 16.dp else 20.dp,

        metricsWidth = metricsWidth,
        metricsHeight = if (compactHeight) 50.dp else 58.dp,
        metricWidth = metricWidth,
        metricSpacing = metricSpacing,

        metricsToActionsSpacing = if (compactHeight) 12.dp else 16.dp,
        actionsWidth = cardWidth * 0.46f,
        actionSpacing = if (compactHeight) 18.dp else 22.dp,
        sideActionButtonSize = if (compactHeight) 48.dp else 54.dp,
        continueActionButtonSize = if (compactHeight) 64.dp else 72.dp
    )
}

private data class ResultCardColors(
    val cardBackground: Color,
    val headerBackground: Color,
    val headerDepthBackground: Color,
    val headerText: Color,
    val titleShadow: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val metricBackground: Color,
    val metricLabel: Color,
    val metricValue: Color
)

private fun resultColorsFor(
    palette: GameResultPalette
): ResultCardColors {
    return when (palette) {
        GameResultPalette.PURPLE -> ResultCardColors(
            cardBackground = ResultPurpleCard,
            headerBackground = ResultPurpleHeader,
            headerDepthBackground = ResultPurpleTab,
            headerText = SmokeWhite,
            titleShadow = Color.Black.copy(alpha = 0.35f),
            primaryText = SmokeWhite,
            secondaryText = SmokeWhite.copy(alpha = 0.9f),
            metricBackground = ResultPurpleHeader.copy(alpha = 0.32f),
            metricLabel = CheeseYellow,
            metricValue = SmokeWhite
        )

        GameResultPalette.RED -> ResultCardColors(
            cardBackground = ResultRedCard,
            headerBackground = ResultRedHeader,
            headerDepthBackground = ResultRedTab,
            headerText = ResultRedHeaderText,
            titleShadow = Color.Black.copy(alpha = 0.3f),
            primaryText = SmokeWhite,
            secondaryText = SmokeWhite.copy(alpha = 0.92f),
            metricBackground = ResultRedMetricBackground,
            metricLabel = ResultRedAccent,
            metricValue = SmokeWhite
        )
    }
}