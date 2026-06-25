package ni.edu.uam.nightbiteapp.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.zIndex
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.data.local.mock.NightLevelsData
import ni.edu.uam.nightbiteapp.ui.components.dialogs.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.design.NightShapes
import ni.edu.uam.nightbiteapp.ui.model.GameResultType
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.LilitaOne
import ni.edu.uam.nightbiteapp.ui.theme.OptionPurple
import ni.edu.uam.nightbiteapp.ui.theme.OptionRed
import ni.edu.uam.nightbiteapp.ui.theme.OptionsPanelLavender
import ni.edu.uam.nightbiteapp.ui.theme.PauseBodyBlue
import ni.edu.uam.nightbiteapp.ui.theme.PauseHeaderDepthPurple
import ni.edu.uam.nightbiteapp.ui.theme.PauseHeaderPurple
import ni.edu.uam.nightbiteapp.ui.theme.SimulatorHeaderPurple
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite

@Composable
fun GamePlaceholderScreen(
    levelId: Int,
    onNavigateToResult: (GameResultType, Int) -> Unit,
    onRestartLevel: () -> Unit,
    onBackToHome: () -> Unit
) {
    val level = NightLevelsData.getLevelById(levelId)

    var showPauseMenu by remember {
        mutableStateOf(false)
    }

    var showRestartConfirmation by remember {
        mutableStateOf(false)
    }

    var showExitConfirmation by remember {
        mutableStateOf(false)
    }

    BackHandler(
        enabled = showPauseMenu || showRestartConfirmation || showExitConfirmation
    ) {
        when {
            showRestartConfirmation -> showRestartConfirmation = false
            showExitConfirmation -> showExitConfirmation = false
            showPauseMenu -> showPauseMenu = false
        }
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val layout = gamePlaceholderLayoutFor(
            maxWidth = maxWidth,
            maxHeight = maxHeight
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            GameBackground()

            PauseButton(
                size = layout.pauseButtonSize,
                onClick = {
                    showPauseMenu = true
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = layout.pauseTopPadding,
                        end = layout.pauseEndPadding
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = layout.contentTopOffset),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SimulatorHeader(
                    title = "SIMULADOR TEMPORAL",
                    width = layout.headerWidth,
                    height = layout.headerHeight
                )

                Spacer(modifier = Modifier.height(layout.headerToButtonsSpacing))

                ResultOptionsPanel(
                    levelId = levelId,
                    layout = layout,
                    onNavigateToResult = onNavigateToResult
                )
            }

            if (showPauseMenu) {
                PauseOverlay(
                    layout = layout,
                    onContinue = {
                        showPauseMenu = false
                    },
                    onRestartRequest = {
                        showPauseMenu = false
                        showRestartConfirmation = true
                    },
                    onExitRequest = {
                        showPauseMenu = false
                        showExitConfirmation = true
                    }
                )
            }

            GamePlaceholderDialogs(
                showRestartConfirmation = showRestartConfirmation,
                showExitConfirmation = showExitConfirmation,
                levelTitle = level?.title ?: "esta noche",
                onConfirmRestart = {
                    showRestartConfirmation = false
                    onRestartLevel()
                },
                onDismissRestart = {
                    showRestartConfirmation = false
                },
                onConfirmExit = {
                    showExitConfirmation = false
                    onBackToHome()
                },
                onDismissExit = {
                    showExitConfirmation = false
                }
            )
        }
    }
}

@Composable
private fun GameBackground() {
    Image(
        painter = painterResource(id = R.drawable.fondo_game),
        contentDescription = "Fondo temporal del simulador",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun PauseButton(
    size: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PressableImageButton(
        drawableId = R.drawable.boton_pausa,
        contentDescription = "Pausar nivel",
        size = size,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
private fun SimulatorHeader(
    title: String,
    width: Dp,
    height: Dp
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .shadow(
                elevation = 8.dp,
                shape = NightShapes.panel
            )
            .clip(NightShapes.panel)
            .background(SimulatorHeaderPurple),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = SmokeWhite,
            fontSize = 25.sp,
            fontFamily = LilitaOne,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.8.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.35f),
                    offset = Offset(2f, 2f),
                    blurRadius = 2f
                )
            )
        )
    }
}

@Composable
private fun ResultOptionsPanel(
    levelId: Int,
    layout: GamePlaceholderLayout,
    onNavigateToResult: (GameResultType, Int) -> Unit
) {
    val options = resultOptionsFor(levelId)

    Row(
        modifier = Modifier
            .width(layout.optionsPanelWidth)
            .height(layout.optionsPanelHeight)
            .clip(RoundedCornerShape(20.dp))
            .background(OptionsPanelLavender)
            .padding(layout.optionsPanelPadding),
        horizontalArrangement = Arrangement.spacedBy(
            space = layout.optionButtonSpacing,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { option ->
            ResultOptionButton(
                text = option.text,
                width = layout.optionButtonWidth,
                height = layout.optionButtonHeight,
                containerColor = option.color,
                onClick = {
                    onNavigateToResult(
                        option.resultType,
                        option.stars
                    )
                }
            )
        }
    }
}

@Composable
private fun ResultOptionButton(
    text: String,
    width: Dp,
    height: Dp,
    containerColor: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        label = "resultOptionButtonScale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isPressed) 0.82f else 1f,
        label = "resultOptionButtonAlpha"
    )

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
            .shadow(
                elevation = if (isPressed) 2.dp else 6.dp,
                shape = RoundedCornerShape(18.dp)
            )
            .clip(RoundedCornerShape(18.dp))
            .background(containerColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = SmokeWhite,
            fontSize = 15.sp,
            fontFamily = LilitaOne,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            lineHeight = 17.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
private fun PauseOverlay(
    layout: GamePlaceholderLayout,
    onContinue: () -> Unit,
    onRestartRequest: () -> Unit,
    onExitRequest: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.58f)),
        contentAlignment = Alignment.Center
    ) {
        PauseMenuCard(
            layout = layout,
            onContinue = onContinue,
            onRestartRequest = onRestartRequest,
            onExitRequest = onExitRequest
        )
    }
}

@Composable
private fun PauseMenuCard(
    layout: GamePlaceholderLayout,
    onContinue: () -> Unit,
    onRestartRequest: () -> Unit,
    onExitRequest: () -> Unit
) {
    val bodyShape = RoundedCornerShape(20.dp)
    val headerShape = RoundedCornerShape(18.dp)

    Box(
        modifier = Modifier
            .width(layout.pausePanelWidth)
            .height(layout.pausePanelHeight),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(layout.pausePanelWidth)
                .height(layout.pauseBodyHeight)
                .clip(bodyShape)
                .background(PauseBodyBlue)
                .border(
                    width = 3.dp,
                    color = PauseHeaderPurple,
                    shape = bodyShape
                ),
            contentAlignment = Alignment.Center
        ) {
            PressableImageButton(
                drawableId = R.drawable.boton_reintentar,
                contentDescription = "Reiniciar nivel",
                size = layout.pauseSideButtonSize,
                onClick = onRestartRequest,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = layout.pauseSideButtonHorizontalPadding)
                    .offset(y = layout.pauseSideButtonDownOffset)
            )

            PressableImageButton(
                drawableId = R.drawable.boton_continuar,
                contentDescription = "Continuar nivel",
                size = layout.pauseContinueButtonSize,
                onClick = onContinue,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = layout.pauseContinueButtonUpOffset)
            )

            PressableImageButton(
                drawableId = R.drawable.boton_home,
                contentDescription = "Volver al Home",
                size = layout.pauseSideButtonSize,
                onClick = onExitRequest,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = layout.pauseSideButtonHorizontalPadding)
                    .offset(y = layout.pauseSideButtonDownOffset)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = layout.pauseHeaderDepthOffset)
                .width(layout.pauseHeaderWidth)
                .height(layout.pauseHeaderHeight)
                .zIndex(1f)
                .clip(headerShape)
                .background(PauseHeaderDepthPurple)
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .width(layout.pauseHeaderWidth)
                .height(layout.pauseHeaderHeight)
                .zIndex(2f)
                .shadow(
                    elevation = 4.dp,
                    shape = headerShape
                )
                .clip(headerShape)
                .background(PauseHeaderPurple),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "PAUSA",
                color = SmokeWhite,
                fontSize = layout.pauseTitleSize,
                fontFamily = LilitaOne,
                fontWeight = FontWeight.Normal,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium.copy(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.35f),
                        offset = Offset(2f, 2f),
                        blurRadius = 2f
                    )
                )
            )
        }
    }
}

@Composable
private fun PressableImageButton(
    drawableId: Int,
    contentDescription: String,
    size: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        label = "imageButtonScale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isPressed) 0.78f else 1f,
        label = "imageButtonAlpha"
    )

    Image(
        painter = painterResource(id = drawableId),
        contentDescription = contentDescription,
        modifier = modifier
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

@Composable
private fun GamePlaceholderDialogs(
    showRestartConfirmation: Boolean,
    showExitConfirmation: Boolean,
    levelTitle: String,
    onConfirmRestart: () -> Unit,
    onDismissRestart: () -> Unit,
    onConfirmExit: () -> Unit,
    onDismissExit: () -> Unit
) {
    if (showRestartConfirmation) {
        NightMessageDialog(
            title = "Reiniciar nivel",
            message = "¿Estás seguro que quieres reiniciar $levelTitle? Se perderá el intento actual.",
            confirmText = "Reiniciar",
            dismissText = "Cancelar",
            icon = Icons.Default.Refresh,
            iconColor = CheeseYellow,
            onConfirm = onConfirmRestart,
            onDismiss = onDismissRestart
        )
    }

    if (showExitConfirmation) {
        NightMessageDialog(
            title = "Volver al Home",
            message = "¿Estás seguro que quieres volver al Home? Se abandonará el intento actual.",
            confirmText = "Volver",
            dismissText = "Cancelar",
            icon = Icons.Default.Home,
            iconColor = CheeseYellow,
            onConfirm = onConfirmExit,
            onDismiss = onDismissExit
        )
    }
}

private fun resultOptionsFor(
    levelId: Int
): List<ResultOption> {
    return when (levelId) {
        0 -> listOf(
            ResultOption(
                text = "100%\nPedidos",
                resultType = GameResultType.TUTORIAL_ALL_DELIVERIES,
                stars = 3,
                color = OptionPurple
            ),
            ResultOption(
                text = "80%\nPedidos",
                resultType = GameResultType.TUTORIAL_EIGHTY_PERCENT,
                stars = 2,
                color = OptionPurple
            ),
            ResultOption(
                text = "50%\nPedidos",
                resultType = GameResultType.TUTORIAL_HALF_DELIVERIES,
                stars = 1,
                color = OptionPurple
            ),
            ResultOption(
                text = "DESPEDIDO",
                resultType = GameResultType.TUTORIAL_FIRED,
                stars = 0,
                color = OptionRed
            )
        )

        4 -> listOf(
            ResultOption(
                text = "GANADOR\nFINAL",
                resultType = GameResultType.FINAL_WIN,
                stars = 3,
                color = OptionPurple
            ),
            ResultOption(
                text = "Entregas\n+50%",
                resultType = GameResultType.FINAL_INCOMPLETE_AT_LEAST_50,
                stars = 2,
                color = OptionPurple
            ),
            ResultOption(
                text = "Entregas\n-50%",
                resultType = GameResultType.FINAL_INCOMPLETE_UNDER_50,
                stars = 1,
                color = OptionPurple
            ),
            ResultOption(
                text = "SIN VIDAS",
                resultType = GameResultType.FINAL_OUT_OF_LIVES,
                stars = 0,
                color = OptionRed
            )
        )

        else -> listOf(
            ResultOption(
                text = "GANADOR",
                resultType = GameResultType.LEVEL_WIN,
                stars = 3,
                color = OptionPurple
            ),
            ResultOption(
                text = "Entregas\n+50%",
                resultType = GameResultType.LEVEL_INCOMPLETE_AT_LEAST_50,
                stars = 2,
                color = OptionPurple
            ),
            ResultOption(
                text = "Entregas\n-50%",
                resultType = GameResultType.LEVEL_INCOMPLETE_UNDER_50,
                stars = 1,
                color = OptionPurple
            ),
            ResultOption(
                text = "SIN VIDAS",
                resultType = GameResultType.LEVEL_OUT_OF_LIVES,
                stars = 0,
                color = OptionRed
            )
        )
    }
}

private data class ResultOption(
    val text: String,
    val resultType: GameResultType,
    val stars: Int,
    val color: Color
)

private data class GamePlaceholderLayout(
    val pauseButtonSize: Dp,
    val pauseTopPadding: Dp,
    val pauseEndPadding: Dp,
    val contentTopOffset: Dp,
    val headerWidth: Dp,
    val headerHeight: Dp,
    val headerToButtonsSpacing: Dp,
    val optionsPanelWidth: Dp,
    val optionsPanelHeight: Dp,
    val optionsPanelPadding: PaddingValues,
    val optionButtonWidth: Dp,
    val optionButtonHeight: Dp,
    val optionButtonSpacing: Dp,
    val pausePanelWidth: Dp,
    val pausePanelHeight: Dp,
    val pauseHeaderWidth: Dp,
    val pauseHeaderHeight: Dp,
    val pauseHeaderDepthOffset: Dp,
    val pauseBodyHeight: Dp,
    val pauseSideButtonSize: Dp,
    val pauseContinueButtonSize: Dp,
    val pauseSideButtonHorizontalPadding: Dp,
    val pauseSideButtonDownOffset: Dp,
    val pauseContinueButtonUpOffset: Dp,
    val pauseTitleSize: TextUnit
)

private fun gamePlaceholderLayoutFor(
    maxWidth: Dp,
    maxHeight: Dp
): GamePlaceholderLayout {
    val compactHeight = maxHeight < 390.dp

    val headerWidth = (maxWidth * 0.34f).coerceIn(
        minimumValue = 320.dp,
        maximumValue = 390.dp
    )

    val optionsPanelWidth = (maxWidth * 0.55f).coerceIn(
        minimumValue = 520.dp,
        maximumValue = 610.dp
    )

    val optionButtonSpacing = if (compactHeight) 16.dp else 22.dp

    val optionButtonHeight = if (compactHeight) {
        96.dp
    } else {
        108.dp
    }

    val optionButtonWidth =
        ((optionsPanelWidth - (optionButtonSpacing * 3) - 48.dp) / 4)
            .coerceIn(
                minimumValue = 100.dp,
                maximumValue = 118.dp
            )

    return GamePlaceholderLayout(
        pauseButtonSize = if (compactHeight) 54.dp else 60.dp,
        pauseTopPadding = if (compactHeight) 22.dp else 28.dp,
        pauseEndPadding = if (compactHeight) 24.dp else 30.dp,
        contentTopOffset = if (compactHeight) 24.dp else 28.dp,
        headerWidth = headerWidth,
        headerHeight = if (compactHeight) 56.dp else 62.dp,
        headerToButtonsSpacing = if (compactHeight) 90.dp else 110.dp,
        optionsPanelWidth = optionsPanelWidth,
        optionsPanelHeight = if (compactHeight) 126.dp else 140.dp,
        optionsPanelPadding = PaddingValues(
            horizontal = 24.dp,
            vertical = if (compactHeight) 14.dp else 16.dp
        ),
        optionButtonWidth = optionButtonWidth,
        optionButtonHeight = optionButtonHeight,
        optionButtonSpacing = optionButtonSpacing,
        pausePanelWidth = if (compactHeight) 300.dp else 325.dp,
        pausePanelHeight = if (compactHeight) 174.dp else 190.dp,
        pauseHeaderWidth = if (compactHeight) 165.dp else 182.dp,
        pauseHeaderHeight = if (compactHeight) 58.dp else 64.dp,
        pauseHeaderDepthOffset = if (compactHeight) 7.dp else 8.dp,
        pauseBodyHeight = if (compactHeight) 145.dp else 158.dp,
        pauseSideButtonSize = if (compactHeight) 54.dp else 60.dp,
        pauseContinueButtonSize = if (compactHeight) 82.dp else 92.dp,
        pauseSideButtonHorizontalPadding = if (compactHeight) 58.dp else 64.dp,
        pauseSideButtonDownOffset = if (compactHeight) 20.dp else 24.dp,
        pauseContinueButtonUpOffset = if (compactHeight) 4.dp else 6.dp,
        pauseTitleSize = if (compactHeight) 29.sp else 32.sp
    )
}