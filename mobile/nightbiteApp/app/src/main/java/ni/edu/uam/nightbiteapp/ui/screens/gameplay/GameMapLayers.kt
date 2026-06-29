package ni.edu.uam.nightbiteapp.ui.screens.gameplay

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun GameMapScene(
    levelId: Int,
    objectiveMode: MapObjectiveMode,
    currentOrderIndex: Int,
    objectiveProgress: Float,
    playerPosition: GamePlayerPosition,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = GameMapData.mapBaseLayer()),
            contentDescription = "Mapa base",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds
        )

        Image(
            painter = painterResource(id = GameMapData.buildingsLayer()),
            contentDescription = "Edificios",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds
        )

        Image(
            painter = painterResource(id = GameMapData.restaurantLayerFor(objectiveMode)),
            contentDescription = "Restaurante",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds
        )

        if (objectiveMode == MapObjectiveMode.GO_TO_DELIVERY) {
            ActiveDeliveryMarker(
                levelId = levelId,
                currentOrderIndex = currentOrderIndex,
                modifier = Modifier.matchParentSize()
            )
        }

        ObjectivePointer(
            levelId = levelId,
            currentOrderIndex = currentOrderIndex,
            objectiveMode = objectiveMode,
            modifier = Modifier.matchParentSize()
        )

        ObjectiveProgressBar(
            objectiveMode = objectiveMode,
            progress = objectiveProgress,
            modifier = Modifier.matchParentSize()
        )

        PlayerCyanBall(
            playerPosition = playerPosition,
            modifier = Modifier.matchParentSize()
        )
    }
}

@Composable
private fun ObjectivePointer(
    levelId: Int,
    currentOrderIndex: Int,
    objectiveMode: MapObjectiveMode,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.zIndex(4f)
    ) {
        val targetPosition = when (objectiveMode) {
            MapObjectiveMode.GO_TO_PICKUP -> GameMapData.getRestaurantPointerBasePosition()
            MapObjectiveMode.GO_TO_DELIVERY -> GameMapData.getActiveBuildingPointerBasePosition(
                levelId = levelId,
                orderIndex = currentOrderIndex
            )
        }

        val pointerX = maxWidth * (targetPosition.x / GameMapData.MAP_BASE_WIDTH)
        val pointerY = maxHeight * (targetPosition.y / GameMapData.MAP_BASE_HEIGHT)

        val pointerWidth = maxWidth * 0.045f
        val pointerHeight = maxHeight * 0.105f

        val transition = rememberInfiniteTransition(
            label = "objectivePointerTransition"
        )

        val bounceOffset by transition.animateFloat(
            initialValue = -8f,
            targetValue = 8f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 650,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "objectivePointerBounce"
        )

        Canvas(
            modifier = Modifier
                .offset(
                    x = pointerX - (pointerWidth / 2),
                    y = pointerY - pointerHeight - 10.dp + bounceOffset.dp
                )
                .width(pointerWidth)
                .height(pointerHeight)
        ) {
            val triangle = Path().apply {
                moveTo(size.width / 2f, size.height)
                lineTo(0f, 0f)
                lineTo(size.width, 0f)
                close()
            }

            drawPath(
                path = triangle,
                color = Color(0xFF7861D8).copy(alpha = 0.88f)
            )
        }
    }
}

@Composable
private fun ActiveDeliveryMarker(
    levelId: Int,
    currentOrderIndex: Int,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.zIndex(3f)
    ) {
        val deliveryPosition = GameMapData.getActiveDeliveryBasePosition(
            levelId = levelId,
            orderIndex = currentOrderIndex
        )

        val markerX = maxWidth * (deliveryPosition.x / GameMapData.MAP_BASE_WIDTH)
        val markerY = maxHeight * (deliveryPosition.y / GameMapData.MAP_BASE_HEIGHT)

        val markerWidth = maxWidth * 0.035f
        val markerHeight = maxHeight * 0.070f

        val transition = rememberInfiniteTransition(
            label = "deliveryMarkerTransition"
        )

        val markerAlpha by transition.animateFloat(
            initialValue = 0.35f,
            targetValue = 0.95f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 520,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "deliveryMarkerAlpha"
        )

        Box(
            modifier = Modifier
                .offset(
                    x = markerX - (markerWidth / 2),
                    y = markerY - (markerHeight / 2)
                )
                .width(markerWidth)
                .height(markerHeight)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFFFD21F).copy(alpha = markerAlpha))
                .border(
                    width = 1.5.dp,
                    color = Color.White.copy(alpha = 0.72f),
                    shape = RoundedCornerShape(50)
                )
        )
    }
}

@Composable
private fun ObjectiveProgressBar(
    objectiveMode: MapObjectiveMode,
    progress: Float,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.zIndex(5f),
        contentAlignment = Alignment.BottomCenter
    ) {
        val safeProgress = progress.coerceIn(0f, 1f)

        val barWidth = maxWidth * 0.37f
        val barHeight = maxHeight * 0.045f
        val fillWidth = barWidth * safeProgress

        val fillColor = when {
            safeProgress <= 0.50f -> Color(0xFFE53935)

            objectiveMode == MapObjectiveMode.GO_TO_PICKUP -> Color(0xFFFFD21F)

            else -> Color(0xFF57DDE3)
        }

        Box(
            modifier = Modifier
                .offset(
                    x = -10.dp,
                    y = (-8).dp
                )
                .width(barWidth)
                .height(barHeight)
                .clip(RoundedCornerShape(50))
                .background(Color.White.copy(alpha = 0.95f))
                .border(
                    width = 1.8.dp,
                    color = Color.White.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(50)
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .offset(x = 5.dp)
                    .width((fillWidth - 10.dp).coerceAtLeast(0.dp))
                    .height(barHeight * 0.48f)
                    .clip(RoundedCornerShape(50))
                    .background(fillColor)
            )
        }
    }
}

@Composable
private fun PlayerCyanBall(
    playerPosition: GamePlayerPosition,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.zIndex(6f)
    ) {
        val playerSize = maxWidth *
                (GameMapData.CELL_SIZE / GameMapData.MAP_BASE_WIDTH) *
                GamePlayerMovement.PLAYER_SIZE_IN_CELLS

        val playerX = maxWidth * (playerPosition.x / GameMapData.MAP_BASE_WIDTH)
        val playerY = maxHeight * (playerPosition.y / GameMapData.MAP_BASE_HEIGHT)

        Box(
            modifier = Modifier
                .offset(
                    x = playerX - (playerSize / 2),
                    y = playerY - (playerSize / 2)
                )
                .size(playerSize)
                .clip(CircleShape)
                .background(Color.Cyan.copy(alpha = 0.95f))
                .border(
                    width = 2.dp,
                    color = Color.White.copy(alpha = 0.85f),
                    shape = CircleShape
                )
        )
    }
}