package ni.edu.uam.nightbiteapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import kotlinx.coroutines.delay
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.data.local.mock.NightLevelsData
import ni.edu.uam.nightbiteapp.ui.components.dialogs.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.model.GameResultType
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.DarkText
import ni.edu.uam.nightbiteapp.ui.theme.LilitaOne
import ni.edu.uam.nightbiteapp.ui.theme.OptionPurple
import ni.edu.uam.nightbiteapp.ui.theme.PauseBodyBlue
import ni.edu.uam.nightbiteapp.ui.theme.PauseHeaderDepthPurple
import ni.edu.uam.nightbiteapp.ui.theme.PauseHeaderPurple
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.ui.input.pointer.pointerInput
import ni.edu.uam.nightbiteapp.ui.screens.gameplay.GameMapData
import ni.edu.uam.nightbiteapp.ui.screens.gameplay.GameMapDirection
import androidx.compose.runtime.withFrameNanos
import ni.edu.uam.nightbiteapp.ui.screens.gameplay.GamePlayerMovement
import androidx.compose.ui.draw.clipToBounds
import ni.edu.uam.nightbiteapp.ui.screens.gameplay.GamePlayerPosition
import androidx.compose.runtime.mutableFloatStateOf
import ni.edu.uam.nightbiteapp.ui.screens.gameplay.GameMapScene
import ni.edu.uam.nightbiteapp.ui.screens.gameplay.MapObjectiveMode
import kotlin.math.hypot
import ni.edu.uam.nightbiteapp.game.TutorialGameResult
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.local.preferences.ControlsLayout
import ni.edu.uam.nightbiteapp.data.local.preferences.GameplayPreferences
import ni.edu.uam.nightbiteapp.ui.screens.gameplay.JornadaCountdownOverlay

@Composable
fun GamePlaceholderScreen(
    levelId: Int,
    onNavigateToResult: (GameResultType, Int, TutorialGameResult) -> Unit,
    onRestartLevel: () -> Unit,
    onBackToHome: () -> Unit
) {
    val level = NightLevelsData.getLevelById(levelId)

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val controlsLayout by remember(context) {
        GameplayPreferences.controlsLayoutFlow(context)
    }.collectAsState(
        initial = GameplayPreferences.DEFAULT_CONTROLS_LAYOUT
    )

    var isMusicEnabled by rememberSaveable {
        mutableStateOf(true)
    }

    var showPauseMenu by remember {
        mutableStateOf(false)
    }

    var showRestartConfirmation by remember {
        mutableStateOf(false)
    }

    var showExitConfirmation by remember {
        mutableStateOf(false)
    }

    var elapsedSeconds by remember {
        mutableIntStateOf(0)
    }

    var pressedControl by remember {
        mutableStateOf<GameControlPress?>(null)
    }

    var playerMovementState by remember {
        mutableStateOf(GamePlayerMovement.startState())
    }

    var heldDirection by remember {
        mutableStateOf<GameMapDirection?>(null)
    }

    val totalOrders = remember(levelId) {
        GameMapData.getTotalOrdersForLevel(levelId)
    }

    var objectiveMode by remember {
        mutableStateOf(MapObjectiveMode.GO_TO_PICKUP)
    }

    var currentOrderIndex by remember {
        mutableIntStateOf(0)
    }

    var completedOrders by remember {
        mutableIntStateOf(0)
    }

    var currentLives by remember {
        mutableIntStateOf(3)
    }

    var objectiveElapsedSeconds by remember {
        mutableFloatStateOf(0f)
    }

    var safeZoneElapsedSeconds by remember {
        mutableFloatStateOf(0f)
    }

    var showJornadaCountdown by remember {
        mutableStateOf(true)
    }

    var jornadaCountdownText by remember {
        mutableStateOf("3")
    }

    var totalDeliveryTimeSeconds by remember {
        mutableFloatStateOf(0f)
    }

    val isPaused = showPauseMenu ||
            showRestartConfirmation ||
            showExitConfirmation ||
            showJornadaCountdown

    fun resetCurrentObjectiveTimer() {
        objectiveElapsedSeconds = 0f
        safeZoneElapsedSeconds = 0f
    }

    fun buildGameplayResult(
        stars: Int,
        completedOrdersOverride: Int = completedOrders,
        totalDeliveryTimeSecondsOverride: Float = totalDeliveryTimeSeconds
    ): TutorialGameResult {
        return TutorialGameResult(
            completedOrders = completedOrdersOverride,
            totalOrders = totalOrders,
            score = stars * 100,
            elapsedTimeSeconds = elapsedSeconds.toFloat(),
            totalDeliveryTimeSeconds = totalDeliveryTimeSecondsOverride,
            stars = stars
        )
    }

    fun loseLife() {
        val nextLives = (currentLives - 1).coerceAtLeast(0)
        currentLives = nextLives
        resetCurrentObjectiveTimer()

        if (nextLives <= 0) {
            onNavigateToResult(
                failResultTypeFor(levelId),
                0,
                buildGameplayResult(
                    stars = 0
                )
            )
        }
    }

    fun completeCurrentOrder() {
        val deliveredOrderTime = objectiveElapsedSeconds.coerceAtLeast(0f)
        val nextTotalDeliveryTimeSeconds = totalDeliveryTimeSeconds + deliveredOrderTime
        val nextCompletedOrders = completedOrders + 1

        totalDeliveryTimeSeconds = nextTotalDeliveryTimeSeconds
        completedOrders = nextCompletedOrders

        resetCurrentObjectiveTimer()

        if (nextCompletedOrders >= totalOrders) {
            onNavigateToResult(
                successResultTypeFor(levelId),
                3,
                buildGameplayResult(
                    stars = 3,
                    completedOrdersOverride = nextCompletedOrders,
                    totalDeliveryTimeSecondsOverride = nextTotalDeliveryTimeSeconds
                )
            )
        } else {
            currentOrderIndex += 1
            objectiveMode = MapObjectiveMode.GO_TO_PICKUP
        }
    }

    fun handleActionPressed() {
        when (objectiveMode) {
            MapObjectiveMode.GO_TO_PICKUP -> {
                val pickupPosition = GameMapData.getRestaurantPickupBasePosition()

                if (
                    isPlayerNearTarget(
                        playerPosition = playerMovementState.position,
                        targetX = pickupPosition.x,
                        targetY = pickupPosition.y,
                        radius = GameMapData.PICKUP_TRIGGER_RADIUS
                    )
                ) {
                    objectiveMode = MapObjectiveMode.GO_TO_DELIVERY
                    resetCurrentObjectiveTimer()
                }
            }

            MapObjectiveMode.GO_TO_DELIVERY -> {
                val deliveryPosition = GameMapData.getActiveDeliveryBasePosition(
                    levelId = levelId,
                    orderIndex = currentOrderIndex
                )

                if (
                    isPlayerNearTarget(
                        playerPosition = playerMovementState.position,
                        targetX = deliveryPosition.x,
                        targetY = deliveryPosition.y,
                        radius = GameMapData.DELIVERY_TRIGGER_RADIUS
                    )
                ) {
                    completeCurrentOrder()
                }
            }
        }
    }

    LaunchedEffect(isPaused) {
        if (!isPaused) {
            while (true) {
                delay(1000)
                elapsedSeconds += 1
            }
        }
    }

    LaunchedEffect(
        isPaused,
        objectiveMode,
        currentOrderIndex,
        currentLives
    ) {
        if (!isPaused && currentLives > 0) {
            while (true) {
                delay(100)

                objectiveElapsedSeconds += 0.1f

                val currentTimeout = when (objectiveMode) {
                    MapObjectiveMode.GO_TO_PICKUP -> GameMapData.PICKUP_TIMEOUT_SECONDS
                    MapObjectiveMode.GO_TO_DELIVERY -> GameMapData.DELIVERY_TIMEOUT_SECONDS
                }

                if (objectiveElapsedSeconds >= currentTimeout) {
                    loseLife()
                    break
                }

                val currentCell = GameMapData.cellForBasePosition(
                    x = playerMovementState.position.x,
                    y = playerMovementState.position.y
                )

                val isInSafeZone = GameMapData.isSafeZoneCell(
                    row = currentCell.row,
                    column = currentCell.column
                )

                if (objectiveMode == MapObjectiveMode.GO_TO_DELIVERY && isInSafeZone) {
                    safeZoneElapsedSeconds += 0.1f
                } else {
                    safeZoneElapsedSeconds = 0f
                }

                if (safeZoneElapsedSeconds >= GameMapData.SAFE_ZONE_TIMEOUT_SECONDS) {
                    loseLife()
                    break
                }
            }
        }
    }

    LaunchedEffect(
        heldDirection,
        isPaused
    ) {
        if (heldDirection == null || isPaused) {
            playerMovementState = GamePlayerMovement.stop(playerMovementState)
            return@LaunchedEffect
        }

        var lastFrameTimeNanos = 0L

        while (heldDirection != null && !isPaused) {
            val frameTimeNanos = withFrameNanos { frameTime ->
                frameTime
            }

            if (lastFrameTimeNanos != 0L) {
                val deltaSeconds = (frameTimeNanos - lastFrameTimeNanos) / 1_000_000_000f

                playerMovementState = GamePlayerMovement.update(
                    state = playerMovementState,
                    desiredDirection = heldDirection,
                    deltaSeconds = deltaSeconds
                )
            }

            lastFrameTimeNanos = frameTimeNanos
        }
    }

    LaunchedEffect(showPauseMenu) {
        if (showPauseMenu) {
            heldDirection = null
            pressedControl = null
        }
    }

    LaunchedEffect(levelId) {
        showJornadaCountdown = true
        heldDirection = null
        pressedControl = null

        val steps = listOf(
            "3",
            "2",
            "1",
            "Inicio de Jornada"
        )

        steps.forEach { step ->
            jornadaCountdownText = step

            delay(
                if (step == "Inicio de Jornada") {
                    850
                } else {
                    650
                }
            )
        }

        showJornadaCountdown = false
    }

    BackHandler {
        when {
            showRestartConfirmation -> showRestartConfirmation = false
            showExitConfirmation -> showExitConfirmation = false
            showPauseMenu -> showPauseMenu = false
            else -> showPauseMenu = true
        }
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val layout = gamePlaceholderLayoutFor(
            maxWidth = maxWidth,
            maxHeight = maxHeight
        )

        val currentObjectiveTimeout = when (objectiveMode) {
            MapObjectiveMode.GO_TO_PICKUP -> GameMapData.PICKUP_TIMEOUT_SECONDS
            MapObjectiveMode.GO_TO_DELIVERY -> GameMapData.DELIVERY_TIMEOUT_SECONDS
        }

        val objectiveProgress = 1f - (objectiveElapsedSeconds / currentObjectiveTimeout)

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            GameBackground()

            GameMap(
                levelId = levelId,
                layout = layout,
                objectiveMode = objectiveMode,
                currentOrderIndex = currentOrderIndex,
                objectiveProgress = objectiveProgress,
                playerPosition = playerMovementState.position,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = layout.mapVerticalOffset)
            )

            GameTopHud(
                lives = currentLives,
                stars = starsForCompletedOrders(
                    completedOrders = completedOrders,
                    totalOrders = totalOrders
                ),
                completedOrders = completedOrders,
                totalOrders = totalOrders,
                elapsedSeconds = elapsedSeconds,
                layout = layout,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = layout.hudTopPadding)
            )

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

            GameControls(
                pressedControl = pressedControl,
                controlsLayout = controlsLayout,
                onControlDown = { control ->
                    pressedControl = control

                    if (control == GameControlPress.ACTION) {
                        handleActionPressed()
                    } else {
                        control.toMapDirection()?.let { direction ->
                            heldDirection = direction
                        }
                    }
                },
                onControlUp = { control ->
                    if (pressedControl == control) {
                        pressedControl = null
                    }

                    if (heldDirection == control.toMapDirection()) {
                        heldDirection = null
                    }
                },
                size = layout.directionPadSize,
                directionHorizontalPadding = layout.directionPadEndPadding,
                directionBottomPadding = layout.directionPadBottomPadding,
                actionHorizontalPadding = 10.dp,
                actionBottomPadding = 1.dp,
                modifier = Modifier.fillMaxSize()
            )

            GameFrontBushes(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(layout.frontBushesHeight)
            )

            if (showPauseMenu) {
                PauseOverlay(
                    layout = layout,
                    isMusicEnabled = isMusicEnabled,
                    controlsLayout = controlsLayout,
                    onToggleMusic = {
                        isMusicEnabled = !isMusicEnabled
                    },
                    onToggleControls = {
                        val nextLayout = controlsLayout.opposite()

                        coroutineScope.launch {
                            GameplayPreferences.setControlsLayout(
                                context = context,
                                controlsLayout = nextLayout
                            )
                        }
                    },
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

            if (showJornadaCountdown) {
                JornadaCountdownOverlay(
                    countdownText = jornadaCountdownText,
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(30f)
                )
            }

            GamePlaceholderDialogs(
                showRestartConfirmation = showRestartConfirmation,
                showExitConfirmation = showExitConfirmation,
                levelTitle = level?.title ?: "esta noche",
                onConfirmRestart = {
                    showRestartConfirmation = false
                    heldDirection = null
                    pressedControl = null

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
        painter = painterResource(id = R.drawable.fondo_general_juego),
        contentDescription = "Fondo general del juego",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
private fun GameMap(
    levelId: Int,
    layout: GamePlaceholderLayout,
    objectiveMode: MapObjectiveMode,
    currentOrderIndex: Int,
    objectiveProgress: Float,
    playerPosition: GamePlayerPosition,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(layout.mapWidth)
            .aspectRatio(GameMapData.MAP_ASPECT_RATIO)
            .clip(RoundedCornerShape(8.dp))
            .clipToBounds()
            .border(
                width = 4.dp,
                color = OptionPurple.copy(alpha = 0.78f),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        GameMapScene(
            levelId = levelId,
            objectiveMode = objectiveMode,
            currentOrderIndex = currentOrderIndex,
            objectiveProgress = objectiveProgress,
            playerPosition = playerPosition,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun GameFrontBushes(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.arbustos_frente),
        contentDescription = "Arbustos al frente",
        modifier = modifier.zIndex(4f),
        contentScale = ContentScale.FillWidth
    )
}

@Composable
private fun GameTopHud(
    lives: Int,
    stars: Int,
    completedOrders: Int,
    totalOrders: Int,
    elapsedSeconds: Int,
    layout: GamePlaceholderLayout,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.zIndex(5f),
        horizontalArrangement = Arrangement.spacedBy(layout.hudItemSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = livesDrawableFor(lives)),
            contentDescription = "Vidas",
            modifier = Modifier
                .offset(x = (-50).dp)
                .width(layout.hudLivesWidth)
                .height(layout.hudHeight),
            contentScale = ContentScale.FillBounds
        )

        Box(
            modifier = Modifier.offset(x = (-50).dp)
        ) {
            HudTextChip(
                drawableId = R.drawable.base_timer,
                value = formatGameTime(elapsedSeconds),
                width = layout.hudTimerWidth,
                height = layout.hudHeight,
                textEndPadding = layout.hudTimerTextEndPadding
            )
        }

        Image(
            painter = painterResource(id = starsDrawableFor(stars)),
            contentDescription = "Estrellas",
            modifier = Modifier
                .offset(x = (50).dp)
                .width(layout.hudStarsWidth)
                .height(layout.hudHeight),
            contentScale = ContentScale.FillBounds
        )

        Box(
            modifier = Modifier.offset(x = (50).dp)
        ) {
            HudTextChip(
                drawableId = R.drawable.base_pedidos,
                value = "$completedOrders/$totalOrders",
                width = layout.hudOrdersWidth,
                height = layout.hudHeight,
                textEndPadding = layout.hudOrdersTextEndPadding
            )
        }
    }
}

@Composable
private fun HudTextChip(
    drawableId: Int,
    value: String,
    width: Dp,
    height: Dp,
    textEndPadding: Dp
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(height),
        contentAlignment = Alignment.CenterEnd
    ) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds
        )

        Text(
            text = value,
            color = DarkText,
            fontSize = 17.sp,
            fontFamily = LilitaOne,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.padding(end = textEndPadding)
        )
    }
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
        modifier = modifier.zIndex(6f)
    )
}

@Composable
private fun GameControls(
    pressedControl: GameControlPress?,
    controlsLayout: ControlsLayout,
    onControlDown: (GameControlPress) -> Unit,
    onControlUp: (GameControlPress) -> Unit,
    size: Dp,
    directionHorizontalPadding: Dp,
    directionBottomPadding: Dp,
    actionHorizontalPadding: Dp,
    actionBottomPadding: Dp,
    modifier: Modifier = Modifier
) {
    val directionsOnRight = controlsLayout == ControlsLayout.DIRECTIONS_RIGHT

    Box(
        modifier = modifier.zIndex(6f)
    ) {
        DirectionPad(
            pressedControl = pressedControl,
            onControlDown = onControlDown,
            onControlUp = onControlUp,
            size = size,
            modifier = Modifier
                .align(
                    if (directionsOnRight) {
                        Alignment.BottomEnd
                    } else {
                        Alignment.BottomStart
                    }
                )
                .padding(
                    start = if (directionsOnRight) 0.dp else directionHorizontalPadding,
                    end = if (directionsOnRight) directionHorizontalPadding else 0.dp,
                    bottom = directionBottomPadding
                )
        )

        ActionButtonControl(
            isPressed = pressedControl == GameControlPress.ACTION,
            onControlDown = onControlDown,
            onControlUp = onControlUp,
            size = size * 1f,
            modifier = Modifier
                .align(
                    if (directionsOnRight) {
                        Alignment.BottomStart
                    } else {
                        Alignment.BottomEnd
                    }
                )
                .padding(
                    start = if (directionsOnRight) 4.dp else 0.dp,
                    end = if (directionsOnRight) 0.dp else 4.dp,
                    bottom = 38.dp
                )
        )
    }
}

@Composable
private fun DirectionPad(
    pressedControl: GameControlPress?,
    onControlDown: (GameControlPress) -> Unit,
    onControlUp: (GameControlPress) -> Unit,
    size: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.base_controles),
            contentDescription = "Controles direccionales",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Fit
        )

        val directionOverlay = when (pressedControl) {
            GameControlPress.UP -> R.drawable.control_arriba
            GameControlPress.DOWN -> R.drawable.control_abajo
            GameControlPress.LEFT -> R.drawable.control_izquierda
            GameControlPress.RIGHT -> R.drawable.control_derecha
            else -> null
        }

        if (directionOverlay != null) {
            Image(
                painter = painterResource(id = directionOverlay),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Fit
            )
        }

        ControlTouchArea(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(size * 0.34f),
            onPressStart = { onControlDown(GameControlPress.UP) },
            onPressEnd = { onControlUp(GameControlPress.UP) }
        )

        ControlTouchArea(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(size * 0.34f),
            onPressStart = { onControlDown(GameControlPress.DOWN) },
            onPressEnd = { onControlUp(GameControlPress.DOWN) }
        )

        ControlTouchArea(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(size * 0.34f),
            onPressStart = { onControlDown(GameControlPress.LEFT) },
            onPressEnd = { onControlUp(GameControlPress.LEFT) }
        )

        ControlTouchArea(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(size * 0.34f),
            onPressStart = { onControlDown(GameControlPress.RIGHT) },
            onPressEnd = { onControlUp(GameControlPress.RIGHT) }
        )
    }
}

@Composable
private fun ActionButtonControl(
    isPressed: Boolean,
    onControlDown: (GameControlPress) -> Unit,
    onControlUp: (GameControlPress) -> Unit,
    size: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.base_accion),
            contentDescription = "Botón de acción",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Fit
        )

        if (isPressed) {
            Image(
                painter = painterResource(id = R.drawable.boton_accion),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Fit
            )
        }

        Text(
            text = "A",
            color = Color.White.copy(alpha = 0.92f),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        ControlTouchArea(
            modifier = Modifier.matchParentSize(),
            onPressStart = { onControlDown(GameControlPress.ACTION) },
            onPressEnd = { onControlUp(GameControlPress.ACTION) }
        )
    }
}
@Composable
private fun ControlTouchArea(
    modifier: Modifier = Modifier,
    onPressStart: () -> Unit,
    onPressEnd: () -> Unit
) {
    Box(
        modifier = modifier.pointerInput(Unit) {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false)

                onPressStart()

                try {
                    waitForUpOrCancellation()
                } finally {
                    onPressEnd()
                }
            }
        }
    )
}

@Composable
private fun PauseOverlay(
    layout: GamePlaceholderLayout,
    isMusicEnabled: Boolean,
    controlsLayout: ControlsLayout,
    onToggleMusic: () -> Unit,
    onToggleControls: () -> Unit,
    onContinue: () -> Unit,
    onRestartRequest: () -> Unit,
    onExitRequest: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(10f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.58f))
                .clickable(
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = null
                ) {
                    // Bloquea los clicks del fondo.
                    // No hace nada al tocar fuera del menú.
                }
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            PauseMenuCard(
                layout = layout,
                isMusicEnabled = isMusicEnabled,
                controlsLayout = controlsLayout,
                onToggleMusic = onToggleMusic,
                onToggleControls = onToggleControls,
                onContinue = onContinue,
                onRestartRequest = onRestartRequest,
                onExitRequest = onExitRequest
            )
        }
    }
}

@Composable
private fun PauseMenuCard(
    layout: GamePlaceholderLayout,
    isMusicEnabled: Boolean,
    controlsLayout: ControlsLayout,
    onToggleMusic: () -> Unit,
    onToggleControls: () -> Unit,
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
            PauseSettingsStrip(
                isMusicEnabled = isMusicEnabled,
                controlsLayout = controlsLayout,
                onToggleMusic = onToggleMusic,
                onToggleControls = onToggleControls,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 38.dp)
            )

            PressableImageButton(
                drawableId = R.drawable.boton_reintentar,
                contentDescription = "Reiniciar nivel",
                size = layout.pauseSideButtonSize,
                onClick = onRestartRequest,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = layout.pauseSideButtonHorizontalPadding)
                    .offset(y = layout.pauseSideButtonDownOffset + 28.dp)
            )

            PressableImageButton(
                drawableId = R.drawable.boton_continuar,
                contentDescription = "Continuar nivel",
                size = layout.pauseContinueButtonSize,
                onClick = onContinue,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = layout.pauseContinueButtonUpOffset + 28.dp)
            )

            PressableImageButton(
                drawableId = R.drawable.boton_home,
                contentDescription = "Volver al Home",
                size = layout.pauseSideButtonSize,
                onClick = onExitRequest,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = layout.pauseSideButtonHorizontalPadding)
                    .offset(y = layout.pauseSideButtonDownOffset + 28.dp)
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
private fun PauseSettingsStrip(
    isMusicEnabled: Boolean,
    controlsLayout: ControlsLayout,
    onToggleMusic: () -> Unit,
    onToggleControls: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .width(270.dp)
            .height(46.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF110C4C))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PauseMusicOption(
            isEnabled = isMusicEnabled,
            onClick = onToggleMusic,
            modifier = Modifier.weight(1f)
        )

        PauseControlsOption(
            controlsLayout = controlsLayout,
            onClick = onToggleControls,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PauseMusicOption(
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(34.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(PauseHeaderPurple)
            .clickable {
                onClick()
            }
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Música",
            color = SmokeWhite,
            fontSize = 12.sp,
            fontFamily = LilitaOne,
            fontWeight = FontWeight.Normal,
            maxLines = 1
        )

        PauseSwitch(
            isEnabled = isEnabled
        )
    }
}

@Composable
private fun PauseControlsOption(
    controlsLayout: ControlsLayout,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val arrow = if (controlsLayout == ControlsLayout.DIRECTIONS_RIGHT) {
        "‹"
    } else {
        "›"
    }

    Row(
        modifier = modifier
            .height(34.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(PauseHeaderPurple)
            .clickable {
                onClick()
            }
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Controles",
            color = SmokeWhite,
            fontSize = 12.sp,
            fontFamily = LilitaOne,
            fontWeight = FontWeight.Normal,
            maxLines = 1
        )

        Box(
            modifier = Modifier
                .size(width = 34.dp, height = 26.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = arrow,
                color = Color(0xFF110C4C),
                fontSize = 24.sp,
                fontFamily = LilitaOne,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PauseSwitch(
    isEnabled: Boolean
) {
    Box(
        modifier = Modifier
            .size(width = 44.dp, height = 24.dp)
            .clip(RoundedCornerShape(50))
            .background(Color(0xFF110C4C)),
        contentAlignment = if (isEnabled) {
            Alignment.CenterEnd
        } else {
            Alignment.CenterStart
        }
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .size(18.dp)
                .clip(CircleShape)
                .background(
                    if (isEnabled) {
                        Color(0xFFFFC21A)
                    } else {
                        SmokeWhite
                    }
                )
        )
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

private fun livesDrawableFor(
    lives: Int
): Int {
    return when (lives.coerceIn(0, 3)) {
        3 -> R.drawable.tres_vidas
        2 -> R.drawable.dos_vidas
        1 -> R.drawable.una_vida
        else -> R.drawable.sin_vidas
    }
}

private fun starsDrawableFor(
    stars: Int
): Int {
    return when (stars.coerceIn(0, 3)) {
        3 -> R.drawable.tres_estrellas
        2 -> R.drawable.dos_estrellas
        1 -> R.drawable.una_estrella
        else -> R.drawable.sin_estrellas
    }
}

private fun controlDrawableFor(
    control: GameControlPress
): Int {
    return when (control) {
        GameControlPress.UP -> R.drawable.control_arriba
        GameControlPress.DOWN -> R.drawable.control_abajo
        GameControlPress.LEFT -> R.drawable.control_izquierda
        GameControlPress.RIGHT -> R.drawable.control_derecha
        GameControlPress.ACTION -> R.drawable.boton_accion
    }
}

private fun formatGameTime(
    totalSeconds: Int
): String {
    val safeSeconds = totalSeconds.coerceAtLeast(0)
    val minutes = safeSeconds / 60
    val seconds = safeSeconds % 60

    return "$minutes:${seconds.toString().padStart(2, '0')}"
}

private enum class GameControlPress {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    ACTION
}
private data class GamePlaceholderLayout(
    val mapWidth: Dp,
    val mapVerticalOffset: Dp,

    val pauseButtonSize: Dp,
    val pauseTopPadding: Dp,
    val pauseEndPadding: Dp,

    val hudTopPadding: Dp,
    val hudHeight: Dp,
    val hudItemSpacing: Dp,
    val hudLivesWidth: Dp,
    val hudStarsWidth: Dp,
    val hudTimerWidth: Dp,
    val hudOrdersWidth: Dp,
    val hudTimerTextEndPadding: Dp,
    val hudOrdersTextEndPadding: Dp,

    val directionPadSize: Dp,
    val directionPadEndPadding: Dp,
    val directionPadBottomPadding: Dp,

    val frontBushesHeight: Dp,

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
    val compactWidth = maxWidth < 760.dp

    val mapWidth = (maxWidth * 0.72f).coerceIn(
        minimumValue = if (compactWidth) 520.dp else 640.dp,
        maximumValue = 980.dp
    )

    val hudHeight = if (compactHeight) 34.dp else 40.dp
    val hudSpacing = if (compactWidth) 8.dp else 16.dp

    val directionPadSize = (maxHeight * 0.32f).coerceIn(
        minimumValue = if (compactHeight) 96.dp else 110.dp,
        maximumValue = if (compactHeight) 122.dp else 150.dp
    )

    return GamePlaceholderLayout(
        mapWidth = mapWidth,
        mapVerticalOffset = if (compactHeight) 8.dp else 12.dp,

        pauseButtonSize = if (compactHeight) 50.dp else 58.dp,
        pauseTopPadding = if (compactHeight) 14.dp else 20.dp,
        pauseEndPadding = if (compactHeight) 28.dp else 34.dp,

        hudTopPadding = if (compactHeight) 14.dp else 20.dp,
        hudHeight = hudHeight,
        hudItemSpacing = hudSpacing,
        hudLivesWidth = if (compactWidth) 92.dp else 112.dp,
        hudStarsWidth = if (compactWidth) 92.dp else 112.dp,
        hudTimerWidth = if (compactWidth) 98.dp else 118.dp,
        hudOrdersWidth = if (compactWidth) 98.dp else 118.dp,
        hudTimerTextEndPadding = if (compactWidth) 18.dp else 26.dp,
        hudOrdersTextEndPadding = if (compactWidth) 18.dp else 26.dp,

        directionPadSize = directionPadSize,
        directionPadEndPadding = if (compactHeight) 25.dp else 35.dp,
        directionPadBottomPadding = if (compactHeight) 44.dp else 58.dp,

        frontBushesHeight = if (compactHeight) 46.dp else 58.dp,

        pausePanelWidth = if (compactHeight) 300.dp else 325.dp,
        pausePanelHeight = if (compactHeight) 205.dp else 225.dp,
        pauseHeaderWidth = if (compactHeight) 140.dp else 155.dp,
        pauseHeaderHeight = if (compactHeight) 46.dp else 52.dp,
        pauseHeaderDepthOffset = if (compactHeight) 5.dp else 6.dp,
        pauseBodyHeight = if (compactHeight) 172.dp else 190.dp,
        pauseSideButtonSize = if (compactHeight) 54.dp else 60.dp,
        pauseContinueButtonSize = if (compactHeight) 82.dp else 92.dp,
        pauseSideButtonHorizontalPadding = if (compactHeight) 58.dp else 64.dp,
        pauseSideButtonDownOffset = if (compactHeight) 20.dp else 24.dp,
        pauseContinueButtonUpOffset = if (compactHeight) 4.dp else 6.dp,
        pauseTitleSize = if (compactHeight) 24.sp else 27.sp
    )
}

private fun GameControlPress.toMapDirection(): GameMapDirection? {
    return when (this) {
        GameControlPress.UP -> GameMapDirection.UP
        GameControlPress.DOWN -> GameMapDirection.DOWN
        GameControlPress.LEFT -> GameMapDirection.LEFT
        GameControlPress.RIGHT -> GameMapDirection.RIGHT
        GameControlPress.ACTION -> null
    }
}

private fun isPlayerNearTarget(
    playerPosition: GamePlayerPosition,
    targetX: Float,
    targetY: Float,
    radius: Float
): Boolean {
    val distance = hypot(
        playerPosition.x - targetX,
        playerPosition.y - targetY
    )

    return distance <= radius
}

private fun starsForCompletedOrders(
    completedOrders: Int,
    totalOrders: Int
): Int {
    if (totalOrders <= 0) return 0

    val ratio = completedOrders.toFloat() / totalOrders.toFloat()

    return when {
        ratio >= 1f -> 3
        ratio >= 0.75f -> 2
        ratio >= 0.50f -> 1
        else -> 0
    }
}

private fun successResultTypeFor(
    levelId: Int
): GameResultType {
    return when (levelId) {
        0 -> GameResultType.TUTORIAL_ALL_DELIVERIES
        4 -> GameResultType.FINAL_WIN
        else -> GameResultType.LEVEL_WIN
    }
}

private fun failResultTypeFor(
    levelId: Int
): GameResultType {
    return when (levelId) {
        0 -> GameResultType.TUTORIAL_FIRED
        4 -> GameResultType.FINAL_OUT_OF_LIVES
        else -> GameResultType.LEVEL_OUT_OF_LIVES
    }
}

private fun ControlsLayout.opposite(): ControlsLayout {
    return when (this) {
        ControlsLayout.DIRECTIONS_RIGHT -> ControlsLayout.DIRECTIONS_LEFT
        ControlsLayout.DIRECTIONS_LEFT -> ControlsLayout.DIRECTIONS_RIGHT
    }
}