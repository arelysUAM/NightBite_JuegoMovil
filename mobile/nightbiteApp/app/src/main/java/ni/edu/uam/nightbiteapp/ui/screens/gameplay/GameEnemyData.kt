package ni.edu.uam.nightbiteapp.ui.screens.gameplay

import kotlin.math.hypot
import java.util.ArrayDeque

object GameEnemyData {

    /*
     * Tutorial = levelId 0
     * Jornada 2 / Sombras errantes = levelId 1
     * Jornada 3 / Lobos callejeros = levelId 2
     */
    const val SHADOW_LEVEL_ID = 1
    const val STREET_WOLF_LEVEL_ID = 2

    const val SHADOW_SIZE_IN_CELLS = 0.62f
    const val STREET_WOLF_SIZE_IN_CELLS = 0.62f

    const val ENEMY_COLLISION_COOLDOWN_SECONDS = 1.15f

    // Se deja para que no se rompa código viejo si aún lo usas.
    const val SHADOW_COLLISION_COOLDOWN_SECONDS = ENEMY_COLLISION_COOLDOWN_SECONDS

    private const val STREET_WOLF_CHASE_SECONDS = 1f
    private const val STREET_WOLF_CHASE_SPEED = 150f
    private const val STREET_WOLF_RETURN_SPEED = 240f
    private const val STREET_WOLF_DETECTION_RADIUS = GameMapData.CELL_SIZE * 2.2f

    private val wanderingShadowRoutes = listOf(
        GameEnemyRoute(
            id = "shadow_vertical_left",
            type = GameEnemyType.WANDERING_SHADOW,
            start = MapGridPoint(column = 1, row = 4),
            end = MapGridPoint(column = 1, row = 8),
            speedCellsPerSecond = 1.65f
        ),
        GameEnemyRoute(
            id = "shadow_horizontal_top",
            type = GameEnemyType.WANDERING_SHADOW,
            start = MapGridPoint(column = 17, row = 4),
            end = MapGridPoint(column = 24, row = 4),
            speedCellsPerSecond = 2.15f
        ),
        GameEnemyRoute(
            id = "shadow_horizontal_bottom",
            type = GameEnemyType.WANDERING_SHADOW,
            start = MapGridPoint(column = 6, row = 10),
            end = MapGridPoint(column = 16, row = 10),
            speedCellsPerSecond = 2.0f
        )
    )

    private val streetWolfRoutes = listOf(
        GameEnemyRoute(
            id = "wolf_vertical_top",
            type = GameEnemyType.STREET_WOLF,
            start = MapGridPoint(column = 7, row = 1),
            end = MapGridPoint(column = 7, row = 3),
            speedCellsPerSecond = 2.45f
        ),
        GameEnemyRoute(
            id = "wolf_vertical_right",
            type = GameEnemyType.STREET_WOLF,
            start = MapGridPoint(column = 23, row = 5),
            end = MapGridPoint(column = 23, row = 7),
            speedCellsPerSecond = 2.55f
        ),
        GameEnemyRoute(
            id = "wolf_horizontal_left",
            type = GameEnemyType.STREET_WOLF,
            start = MapGridPoint(column = 2, row = 8),
            end = MapGridPoint(column = 4, row = 8),
            speedCellsPerSecond = 2.4f
        )
    )

    fun hasEnemiesForLevel(
        levelId: Int
    ): Boolean {
        return levelId == SHADOW_LEVEL_ID ||
                levelId == STREET_WOLF_LEVEL_ID
    }

    fun initialEnemyPositionsForLevel(
        levelId: Int
    ): List<GameEnemyPosition> {
        return enemyPositionsForLevel(
            levelId = levelId,
            elapsedSeconds = 0f
        )
    }

    fun enemyPositionsForLevel(
        levelId: Int,
        elapsedSeconds: Float
    ): List<GameEnemyPosition> {
        return routesForLevel(levelId).map { route ->
            positionForRoute(
                route = route,
                elapsedSeconds = elapsedSeconds
            )
        }
    }

    fun updateEnemyPositionsForLevel(
        levelId: Int,
        elapsedSeconds: Float,
        deltaSeconds: Float,
        currentPositions: List<GameEnemyPosition>,
        playerPosition: GamePlayerPosition
    ): List<GameEnemyPosition> {
        return when (levelId) {
            SHADOW_LEVEL_ID -> enemyPositionsForLevel(
                levelId = levelId,
                elapsedSeconds = elapsedSeconds
            )

            STREET_WOLF_LEVEL_ID -> updateStreetWolfPositions(
                elapsedSeconds = elapsedSeconds,
                deltaSeconds = deltaSeconds,
                currentPositions = currentPositions,
                playerPosition = playerPosition
            )

            else -> emptyList()
        }
    }

    fun playerCollidesWithEnemy(
        playerPosition: GamePlayerPosition,
        enemyPosition: GameEnemyPosition
    ): Boolean {
        val playerRadius =
            GameMapData.CELL_SIZE * (GamePlayerMovement.PLAYER_SIZE_IN_CELLS / 2f)

        val distance = hypot(
            playerPosition.x - enemyPosition.x,
            playerPosition.y - enemyPosition.y
        )

        return distance <= playerRadius + enemyPosition.radius
    }

    fun sizeInCellsFor(
        type: GameEnemyType
    ): Float {
        return when (type) {
            GameEnemyType.WANDERING_SHADOW -> SHADOW_SIZE_IN_CELLS
            GameEnemyType.STREET_WOLF -> STREET_WOLF_SIZE_IN_CELLS
        }
    }

    private fun updateStreetWolfPositions(
        elapsedSeconds: Float,
        deltaSeconds: Float,
        currentPositions: List<GameEnemyPosition>,
        playerPosition: GamePlayerPosition
    ): List<GameEnemyPosition> {
        return streetWolfRoutes.map { route ->
            val patrolPosition = positionForRoute(
                route = route,
                elapsedSeconds = elapsedSeconds
            )

            val currentPosition = currentPositions.firstOrNull { enemy ->
                enemy.id == route.id
            } ?: patrolPosition

            val distanceToPlayer = distanceBetween(
                startX = currentPosition.x,
                startY = currentPosition.y,
                endX = playerPosition.x,
                endY = playerPosition.y
            )

            when (currentPosition.behaviorState) {
                GameEnemyBehaviorState.PATROLLING -> {
                    if (distanceToPlayer <= STREET_WOLF_DETECTION_RADIUS) {
                        val movedPosition = moveAlongStreetPath(
                            startX = patrolPosition.x,
                            startY = patrolPosition.y,
                            targetX = playerPosition.x,
                            targetY = playerPosition.y,
                            maxDistance = STREET_WOLF_CHASE_SPEED * deltaSeconds
                        )

                        patrolPosition.copy(
                            x = movedPosition.x,
                            y = movedPosition.y,
                            behaviorState = GameEnemyBehaviorState.CHASING,
                            chaseRemainingSeconds = (STREET_WOLF_CHASE_SECONDS - deltaSeconds)
                                .coerceAtLeast(0f)
                        )
                    } else {
                        patrolPosition.copy(
                            behaviorState = GameEnemyBehaviorState.PATROLLING,
                            chaseRemainingSeconds = 0f
                        )
                    }
                }

                GameEnemyBehaviorState.CHASING -> {
                    val nextRemainingSeconds =
                        (currentPosition.chaseRemainingSeconds - deltaSeconds)
                            .coerceAtLeast(0f)

                    val movedPosition = moveAlongStreetPath(
                        startX = currentPosition.x,
                        startY = currentPosition.y,
                        targetX = playerPosition.x,
                        targetY = playerPosition.y,
                        maxDistance = STREET_WOLF_CHASE_SPEED * deltaSeconds
                    )

                    if (nextRemainingSeconds <= 0f) {
                        currentPosition.copy(
                            x = movedPosition.x,
                            y = movedPosition.y,
                            behaviorState = GameEnemyBehaviorState.RETURNING,
                            chaseRemainingSeconds = 0f
                        )
                    } else {
                        currentPosition.copy(
                            x = movedPosition.x,
                            y = movedPosition.y,
                            behaviorState = GameEnemyBehaviorState.CHASING,
                            chaseRemainingSeconds = nextRemainingSeconds
                        )
                    }
                }

                GameEnemyBehaviorState.RETURNING -> {
                    val movedPosition = moveAlongStreetPath(
                        startX = currentPosition.x,
                        startY = currentPosition.y,
                        targetX = patrolPosition.x,
                        targetY = patrolPosition.y,
                        maxDistance = STREET_WOLF_RETURN_SPEED * deltaSeconds
                    )

                    val reachedPatrolPosition = distanceBetween(
                        startX = movedPosition.x,
                        startY = movedPosition.y,
                        endX = patrolPosition.x,
                        endY = patrolPosition.y
                    ) <= 2f

                    if (reachedPatrolPosition) {
                        patrolPosition.copy(
                            behaviorState = GameEnemyBehaviorState.PATROLLING,
                            chaseRemainingSeconds = 0f
                        )
                    } else {
                        currentPosition.copy(
                            x = movedPosition.x,
                            y = movedPosition.y,
                            behaviorState = GameEnemyBehaviorState.RETURNING,
                            chaseRemainingSeconds = 0f
                        )
                    }
                }
            }
        }
    }

    private fun routesForLevel(
        levelId: Int
    ): List<GameEnemyRoute> {
        return when (levelId) {
            SHADOW_LEVEL_ID -> wanderingShadowRoutes
            STREET_WOLF_LEVEL_ID -> streetWolfRoutes
            else -> emptyList()
        }
    }

    private fun positionForRoute(
        route: GameEnemyRoute,
        elapsedSeconds: Float
    ): GameEnemyPosition {
        val start = centerForCell(route.start)
        val end = centerForCell(route.end)

        val distance = distanceBetween(
            startX = start.x,
            startY = start.y,
            endX = end.x,
            endY = end.y
        )

        if (distance <= 0f) {
            return GameEnemyPosition(
                id = route.id,
                type = route.type,
                x = start.x,
                y = start.y,
                radius = enemyRadiusFor(route.type)
            )
        }

        val traveledDistance =
            elapsedSeconds *
                    route.speedCellsPerSecond *
                    GameMapData.CELL_SIZE

        val cycleDistance = distance * 2f
        val cyclePosition = traveledDistance % cycleDistance

        val distanceOnRoute =
            if (cyclePosition <= distance) {
                cyclePosition
            } else {
                cycleDistance - cyclePosition
            }

        val progress = distanceOnRoute / distance

        val x = start.x + ((end.x - start.x) * progress)
        val y = start.y + ((end.y - start.y) * progress)

        return GameEnemyPosition(
            id = route.id,
            type = route.type,
            x = x,
            y = y,
            radius = enemyRadiusFor(route.type)
        )
    }

    private fun centerForCell(
        point: MapGridPoint
    ): GameEnemyBasePosition {
        return GameEnemyBasePosition(
            x = (point.column + 0.5f) * GameMapData.CELL_SIZE,
            y = (point.row + 0.5f) * GameMapData.CELL_SIZE
        )
    }

    private fun enemyRadiusFor(
        type: GameEnemyType
    ): Float {
        return GameMapData.CELL_SIZE * (sizeInCellsFor(type) / 2f)
    }

    private fun moveAlongStreetPath(
        startX: Float,
        startY: Float,
        targetX: Float,
        targetY: Float,
        maxDistance: Float
    ): GameEnemyBasePosition {
        val startCell = GameMapData.cellForBasePosition(
            x = startX,
            y = startY
        )

        val targetCell = GameMapData.cellForBasePosition(
            x = targetX,
            y = targetY
        )

        val startIsWalkable = GameMapData.isWalkableCell(
            row = startCell.row,
            column = startCell.column
        )

        val targetIsWalkable = GameMapData.isWalkableCell(
            row = targetCell.row,
            column = targetCell.column
        )

        if (!startIsWalkable || !targetIsWalkable) {
            return GameEnemyBasePosition(
                x = startX,
                y = startY
            )
        }

        if (startCell == targetCell) {
            return moveToward(
                startX = startX,
                startY = startY,
                targetX = targetX,
                targetY = targetY,
                maxDistance = maxDistance
            )
        }

        val path = findPath(
            startCell = startCell,
            targetCell = targetCell
        )

        if (path.size < 2) {
            return GameEnemyBasePosition(
                x = startX,
                y = startY
            )
        }

        val nextCell = path[1]
        val nextCellCenter = centerForCell(nextCell)

        return moveToward(
            startX = startX,
            startY = startY,
            targetX = nextCellCenter.x,
            targetY = nextCellCenter.y,
            maxDistance = maxDistance
        )
    }

    private fun findPath(
        startCell: GameMapCell,
        targetCell: GameMapCell
    ): List<GameMapCell> {
        if (startCell == targetCell) {
            return listOf(startCell)
        }

        val queue = ArrayDeque<GameMapCell>()
        val visited = mutableSetOf<GameMapCell>()
        val previous = mutableMapOf<GameMapCell, GameMapCell>()

        queue.add(startCell)
        visited.add(startCell)

        while (queue.isNotEmpty()) {
            val currentCell = queue.removeFirst()

            walkableNeighborsOf(currentCell).forEach { neighbor ->
                if (neighbor !in visited) {
                    visited.add(neighbor)
                    previous[neighbor] = currentCell

                    if (neighbor == targetCell) {
                        return rebuildPath(
                            startCell = startCell,
                            targetCell = targetCell,
                            previous = previous
                        )
                    }

                    queue.add(neighbor)
                }
            }
        }

        return emptyList()
    }

    private fun rebuildPath(
        startCell: GameMapCell,
        targetCell: GameMapCell,
        previous: Map<GameMapCell, GameMapCell>
    ): List<GameMapCell> {
        val path = mutableListOf<GameMapCell>()
        var currentCell = targetCell

        path.add(currentCell)

        while (currentCell != startCell) {
            currentCell = previous[currentCell] ?: return emptyList()
            path.add(currentCell)
        }

        return path.asReversed()
    }

    private fun walkableNeighborsOf(
        cell: GameMapCell
    ): List<GameMapCell> {
        return listOf(
            GameMapCell(
                row = cell.row - 1,
                column = cell.column
            ),
            GameMapCell(
                row = cell.row + 1,
                column = cell.column
            ),
            GameMapCell(
                row = cell.row,
                column = cell.column - 1
            ),
            GameMapCell(
                row = cell.row,
                column = cell.column + 1
            )
        ).filter { neighbor ->
            GameMapData.isWalkableCell(
                row = neighbor.row,
                column = neighbor.column
            )
        }
    }

    private fun centerForCell(
        cell: GameMapCell
    ): GameEnemyBasePosition {
        return GameEnemyBasePosition(
            x = GameMapData.cellCenterX(cell.column),
            y = GameMapData.cellCenterY(cell.row)
        )
    }

    private fun moveToward(
        startX: Float,
        startY: Float,
        targetX: Float,
        targetY: Float,
        maxDistance: Float
    ): GameEnemyBasePosition {
        val distance = distanceBetween(
            startX = startX,
            startY = startY,
            endX = targetX,
            endY = targetY
        )

        if (distance <= 0f || distance <= maxDistance) {
            return GameEnemyBasePosition(
                x = targetX,
                y = targetY
            )
        }

        val progress = maxDistance / distance

        return GameEnemyBasePosition(
            x = startX + ((targetX - startX) * progress),
            y = startY + ((targetY - startY) * progress)
        )
    }

    private fun distanceBetween(
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float
    ): Float {
        return hypot(
            endX - startX,
            endY - startY
        )
    }
}

enum class GameEnemyType {
    WANDERING_SHADOW,
    STREET_WOLF
}

enum class GameEnemyBehaviorState {
    PATROLLING,
    CHASING,
    RETURNING
}

data class GameEnemyRoute(
    val id: String,
    val type: GameEnemyType,
    val start: MapGridPoint,
    val end: MapGridPoint,
    val speedCellsPerSecond: Float
)

data class GameEnemyPosition(
    val id: String,
    val type: GameEnemyType,
    val x: Float,
    val y: Float,
    val radius: Float,
    val behaviorState: GameEnemyBehaviorState = GameEnemyBehaviorState.PATROLLING,
    val chaseRemainingSeconds: Float = 0f
)

private data class GameEnemyBasePosition(
    val x: Float,
    val y: Float
)