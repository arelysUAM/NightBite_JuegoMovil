package ni.edu.uam.nightbiteapp.ui.screens.gameplay

import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToInt

data class GamePlayerMovementState(
    val position: GamePlayerPosition,
    val currentDirection: GameMapDirection? = null
)

data class GamePlayerPosition(
    val x: Float,
    val y: Float
)

object GamePlayerMovement {

    const val PLAYER_SIZE_IN_CELLS = 0.60f

    private const val PLAYER_SPEED = 365f
    private const val MAX_DELTA_SECONDS = 0.05f

    private val playerRadius = GameMapData.CELL_SIZE * (PLAYER_SIZE_IN_CELLS / 2f)
    private val turnTolerance = GameMapData.CELL_SIZE * 0.50f

    fun startState(): GamePlayerMovementState {
        val startNode = GameMapData.getStartNode()

        return GamePlayerMovementState(
            position = GamePlayerPosition(
                x = startNode.x,
                y = startNode.y
            ),
            currentDirection = null
        )
    }

    fun stop(
        state: GamePlayerMovementState
    ): GamePlayerMovementState {
        return state.copy(
            position = keepPositionInsideMap(state.position),
            currentDirection = null
        )
    }

    fun update(
        state: GamePlayerMovementState,
        desiredDirection: GameMapDirection?,
        deltaSeconds: Float
    ): GamePlayerMovementState {
        if (desiredDirection == null) {
            return stop(state)
        }

        val safeDeltaSeconds = deltaSeconds.coerceIn(
            minimumValue = 0f,
            maximumValue = MAX_DELTA_SECONDS
        )

        var position = keepPositionInsideMap(state.position)
        var currentDirection = state.currentDirection

        if (canTurnToDirection(position, desiredDirection)) {
            position = snapToLaneCenter(
                position = position,
                direction = desiredDirection
            )
            currentDirection = desiredDirection
        }

        if (currentDirection == null) {
            return state.copy(
                position = position,
                currentDirection = null
            )
        }

        position = snapToLaneCenter(
            position = position,
            direction = currentDirection
        )

        val nextPosition = movePosition(
            position = position,
            direction = currentDirection,
            distance = PLAYER_SPEED * safeDeltaSeconds
        )

        if (!canOccupyPosition(nextPosition)) {
            return GamePlayerMovementState(
                position = position,
                currentDirection = null
            )
        }

        return GamePlayerMovementState(
            position = nextPosition,
            currentDirection = currentDirection
        )
    }

    private fun movePosition(
        position: GamePlayerPosition,
        direction: GameMapDirection,
        distance: Float
    ): GamePlayerPosition {
        return when (direction) {
            GameMapDirection.UP -> position.copy(
                y = position.y - distance
            )

            GameMapDirection.DOWN -> position.copy(
                y = position.y + distance
            )

            GameMapDirection.LEFT -> position.copy(
                x = position.x - distance
            )

            GameMapDirection.RIGHT -> position.copy(
                x = position.x + distance
            )
        }
    }

    private fun canTurnToDirection(
        position: GamePlayerPosition,
        direction: GameMapDirection
    ): Boolean {
        val nearestCell = nearestCellFor(position)

        val centerX = GameMapData.cellCenterX(nearestCell.column)
        val centerY = GameMapData.cellCenterY(nearestCell.row)

        val isCloseEnoughToTurn = when (direction) {
            GameMapDirection.UP,
            GameMapDirection.DOWN -> abs(position.x - centerX) <= turnTolerance

            GameMapDirection.LEFT,
            GameMapDirection.RIGHT -> abs(position.y - centerY) <= turnTolerance
        }

        if (!isCloseEnoughToTurn) {
            return false
        }

        val currentCellIsWalkable = GameMapData.isWalkableCell(
            row = nearestCell.row,
            column = nearestCell.column
        )

        if (!currentCellIsWalkable) {
            return false
        }

        val targetCell = targetCellForDirection(
            cell = nearestCell,
            direction = direction
        )

        return GameMapData.isWalkableCell(
            row = targetCell.row,
            column = targetCell.column
        )
    }

    private fun snapToLaneCenter(
        position: GamePlayerPosition,
        direction: GameMapDirection
    ): GamePlayerPosition {
        val nearestCell = nearestCellFor(position)

        val snappedPosition = when (direction) {
            GameMapDirection.UP,
            GameMapDirection.DOWN -> position.copy(
                x = GameMapData.cellCenterX(nearestCell.column)
            )

            GameMapDirection.LEFT,
            GameMapDirection.RIGHT -> position.copy(
                y = GameMapData.cellCenterY(nearestCell.row)
            )
        }

        return keepPositionInsideMap(snappedPosition)
    }

    private fun canOccupyPosition(
        position: GamePlayerPosition
    ): Boolean {
        if (!isInsideMapWithPlayerRadius(position)) {
            return false
        }

        val diagonalRadius = playerRadius * 0.70f

        val pointsToCheck = listOf(
            position,

            position.copy(
                x = position.x + playerRadius
            ),
            position.copy(
                x = position.x - playerRadius
            ),
            position.copy(
                y = position.y + playerRadius
            ),
            position.copy(
                y = position.y - playerRadius
            ),

            position.copy(
                x = position.x + diagonalRadius,
                y = position.y + diagonalRadius
            ),
            position.copy(
                x = position.x - diagonalRadius,
                y = position.y + diagonalRadius
            ),
            position.copy(
                x = position.x + diagonalRadius,
                y = position.y - diagonalRadius
            ),
            position.copy(
                x = position.x - diagonalRadius,
                y = position.y - diagonalRadius
            )
        )

        return pointsToCheck.all { point ->
            isWalkablePoint(point)
        }
    }

    private fun isWalkablePoint(
        point: GamePlayerPosition
    ): Boolean {
        if (point.x < 0f || point.x >= GameMapData.MAP_BASE_WIDTH) {
            return false
        }

        if (point.y < 0f || point.y >= GameMapData.MAP_BASE_HEIGHT) {
            return false
        }

        val cell = cellForPosition(point) ?: return false

        return GameMapData.isWalkableCell(
            row = cell.row,
            column = cell.column
        )
    }

    private fun isInsideMapWithPlayerRadius(
        position: GamePlayerPosition
    ): Boolean {
        return position.x >= playerRadius &&
                position.x <= GameMapData.MAP_BASE_WIDTH - playerRadius &&
                position.y >= playerRadius &&
                position.y <= GameMapData.MAP_BASE_HEIGHT - playerRadius
    }

    private fun keepPositionInsideMap(
        position: GamePlayerPosition
    ): GamePlayerPosition {
        return GamePlayerPosition(
            x = position.x.coerceIn(
                minimumValue = playerRadius,
                maximumValue = GameMapData.MAP_BASE_WIDTH - playerRadius
            ),
            y = position.y.coerceIn(
                minimumValue = playerRadius,
                maximumValue = GameMapData.MAP_BASE_HEIGHT - playerRadius
            )
        )
    }

    private fun cellForPosition(
        position: GamePlayerPosition
    ): GameMapCell? {
        if (position.x < 0f || position.x >= GameMapData.MAP_BASE_WIDTH) {
            return null
        }

        if (position.y < 0f || position.y >= GameMapData.MAP_BASE_HEIGHT) {
            return null
        }

        val column = floor(position.x / GameMapData.CELL_SIZE).toInt()
        val row = floor(position.y / GameMapData.CELL_SIZE).toInt()

        if (!GameMapData.isInsideGrid(row, column)) {
            return null
        }

        return GameMapCell(
            row = row,
            column = column
        )
    }

    private fun nearestCellFor(
        position: GamePlayerPosition
    ): GameMapCell {
        val column = ((position.x - (GameMapData.CELL_SIZE / 2f)) / GameMapData.CELL_SIZE)
            .roundToInt()
            .coerceIn(0, GameMapData.GRID_COLUMNS - 1)

        val row = ((position.y - (GameMapData.CELL_SIZE / 2f)) / GameMapData.CELL_SIZE)
            .roundToInt()
            .coerceIn(0, GameMapData.GRID_ROWS - 1)

        return GameMapCell(
            row = row,
            column = column
        )
    }

    private fun targetCellForDirection(
        cell: GameMapCell,
        direction: GameMapDirection
    ): GameMapCell {
        return when (direction) {
            GameMapDirection.UP -> cell.copy(
                row = cell.row - 1
            )

            GameMapDirection.DOWN -> cell.copy(
                row = cell.row + 1
            )

            GameMapDirection.LEFT -> cell.copy(
                column = cell.column - 1
            )

            GameMapDirection.RIGHT -> cell.copy(
                column = cell.column + 1
            )
        }
    }
}