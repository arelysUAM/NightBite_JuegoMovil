package ni.edu.uam.nightbiteapp.ui.screens.gameplay

import androidx.annotation.DrawableRes
import ni.edu.uam.nightbiteapp.R

/**
 * Datos base del mapa jugable en Compose.
 *
 * Este archivo NO dibuja nada.
 * Solo define:
 * - medidas base del mapa
 * - cuadrícula transitable
 * - nodos para movimiento
 * - edificios
 * - puntos de entrega
 * - patrones de pedidos por nivel
 * - capas PNG del mapa
 */
object GameMapData {

    // =========================================================
    // MEDIDAS BASE DEL PNG
    // =========================================================

    const val MAP_BASE_WIDTH = 1475f
    const val MAP_BASE_HEIGHT = 767f

    const val GRID_COLUMNS = 25
    const val GRID_ROWS = 13
    const val CELL_SIZE = 59f

    const val MAP_ASPECT_RATIO = MAP_BASE_WIDTH / MAP_BASE_HEIGHT

    // =========================================================
    // TIEMPOS BASE
    // =========================================================

    const val TUTORIAL_TOTAL_ORDERS = 8

    const val PICKUP_TIMEOUT_SECONDS = 10f
    const val SAFE_ZONE_TIMEOUT_SECONDS = 10f
    const val DELIVERY_TIMEOUT_SECONDS = 7f

    const val DELIVERY_TRIGGER_RADIUS = CELL_SIZE * 0.45f
    const val PICKUP_TRIGGER_RADIUS = CELL_SIZE * 0.55f

    // =========================================================
    // MAPA LÓGICO
    // x = calle transitable
    // Z = zona segura / zona de recolección
    // o = zona no transitable
    // =========================================================

    val routeGrid = listOf(
        "xxxxxxxxxxxxxxxxxxxxxxxxx",
        "xooooooxooooxooooxoooooox",
        "xooooooxooooxooooxoooooox",
        "xooooooxooooxooooxoooooox",
        "xxxxxxxxxxxxxxxxxxxxxxxxx",
        "oxoooooooxooooooxooooooxo",
        "oxoooooooxooooooxooooooxo",
        "oxoooooooxooooooxooooooxo",
        "xxxxxxxooxooooooxooxxxxxx",
        "xoooooxooxooZZooxooxxooox",
        "xoooooxxxxxxxxxxxooxxooox",
        "xoooooxoooooooooxooooooox",
        "xxxxxxxoooooooooxxxxxxxxx"
    )

    /**
     * Nodo inicial del repartidor.
     *
     * Está en la primera Z:
     * row 9, column 12 en coordenadas 0-based.
     */
    const val START_NODE_ID = "safe_9_12"

    // =========================================================
    // CAPAS PNG DEL MAPA
    // =========================================================

    @DrawableRes
    fun mapBaseLayer(): Int {
        return R.drawable.mapa_base
    }

    @DrawableRes
    fun buildingsLayer(): Int {
        return R.drawable.edificios
    }

    @DrawableRes
    fun restaurantLayerFor(
        mode: MapObjectiveMode
    ): Int {
        return when (mode) {
            MapObjectiveMode.GO_TO_PICKUP -> R.drawable.restaurante_activo
            MapObjectiveMode.GO_TO_DELIVERY -> R.drawable.restaurante_neutro
        }
    }

    // =========================================================
    // RESTAURANTE / ZONA SEGURA
    // Coordenadas 1-based, igual como las estás midiendo en diseño.
    // =========================================================

    val RESTAURANT_POINTER_ANCHOR = MapGridAnchor(
        column = 13f,
        row = 6f
    )

    val RESTAURANT_PICKUP_ANCHOR = MapGridAnchor(
        column = 13.5f,
        row = 10f
    )

    val SAFE_ZONE_POINTS = listOf(
        MapGridPoint(column = 13, row = 10),
        MapGridPoint(column = 14, row = 10)
    )

    // =========================================================
    // EDIFICIOS Y PUNTOS DE ENTREGA
    // Coordenadas 1-based.
    // Si está "entre columna 10 y 11", usamos 10.5f.
    // =========================================================

    val buildings = listOf(
        DeliveryBuilding(
            id = 1,
            pointerAnchor = MapGridAnchor(3f, 2f),
            deliveryPoints = listOf(
                MapGridAnchor(3f, 5f),
                MapGridAnchor(1f, 3f)
            )
        ),

        DeliveryBuilding(
            id = 2,
            pointerAnchor = MapGridAnchor(5f, 2f),
            deliveryPoints = listOf(
                MapGridAnchor(5f, 5f)
            )
        ),

        DeliveryBuilding(
            id = 3,
            pointerAnchor = MapGridAnchor(6.5f, 2f),
            deliveryPoints = listOf(
                MapGridAnchor(6f, 5f)
            )
        ),

        DeliveryBuilding(
            id = 4,
            pointerAnchor = MapGridAnchor(10.5f, 2f),
            deliveryPoints = listOf(
                MapGridAnchor(10.5f, 5f)
            )
        ),

        DeliveryBuilding(
            id = 5,
            pointerAnchor = MapGridAnchor(15.5f, 2f),
            deliveryPoints = listOf(
                MapGridAnchor(18f, 3f),
                MapGridAnchor(15.5f, 5f)
            )
        ),

        DeliveryBuilding(
            id = 6,
            pointerAnchor = MapGridAnchor(20f, 2f),
            deliveryPoints = listOf(
                MapGridAnchor(20f, 5f)
            )
        ),

        DeliveryBuilding(
            id = 7,
            pointerAnchor = MapGridAnchor(23.5f, 2f),
            deliveryPoints = listOf(
                MapGridAnchor(23.5f, 1f),
                MapGridAnchor(25f, 3f)
            )
        ),

        DeliveryBuilding(
            id = 8,
            pointerAnchor = MapGridAnchor(4.5f, 6f),
            deliveryPoints = listOf(
                MapGridAnchor(5f, 5f)
            )
        ),

        DeliveryBuilding(
            id = 9,
            pointerAnchor = MapGridAnchor(8f, 6f),
            deliveryPoints = listOf(
                MapGridAnchor(7f, 5f)
            )
        ),

        DeliveryBuilding(
            id = 10,
            pointerAnchor = MapGridAnchor(8.5f, 8f),
            deliveryPoints = listOf(
                MapGridAnchor(7f, 9f),
                MapGridAnchor(10f, 8f)
            )
        ),

        DeliveryBuilding(
            id = 11,
            pointerAnchor = MapGridAnchor(19f, 6f),
            deliveryPoints = listOf(
                MapGridAnchor(17f, 7f)
            )
        ),

        DeliveryBuilding(
            id = 12,
            pointerAnchor = MapGridAnchor(22f, 6f),
            deliveryPoints = listOf(
                MapGridAnchor(24f, 7f),
                MapGridAnchor(22f, 9f)
            )
        ),

        DeliveryBuilding(
            id = 13,
            pointerAnchor = MapGridAnchor(3f, 10f),
            deliveryPoints = listOf(
                MapGridAnchor(1f, 11f),
                MapGridAnchor(3f, 13f)
            )
        ),

        DeliveryBuilding(
            id = 14,
            pointerAnchor = MapGridAnchor(5f, 10f),
            deliveryPoints = listOf(
                MapGridAnchor(5f, 9f)
            )
        ),

        DeliveryBuilding(
            id = 15,
            pointerAnchor = MapGridAnchor(18.5f, 8f),
            deliveryPoints = listOf(
                MapGridAnchor(20f, 9f)
            )
        ),

        DeliveryBuilding(
            id = 16,
            pointerAnchor = MapGridAnchor(18.5f, 10f),
            deliveryPoints = listOf(
                MapGridAnchor(20f, 10f),
                MapGridAnchor(20f, 11f)
            )
        ),

        DeliveryBuilding(
            id = 17,
            pointerAnchor = MapGridAnchor(23f, 10f),
            deliveryPoints = listOf(
                MapGridAnchor(21f, 11f),
                MapGridAnchor(25f, 11f),
                MapGridAnchor(23f, 13f)
            )
        )
    )

    // =========================================================
    // PATRONES DE PEDIDOS POR NIVEL
    // Solo hay un punto de entrega activo a la vez.
    // =========================================================

    private val tutorialOrderPattern = listOf(
        GameOrderTarget(buildingId = 1, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 5, deliveryPointIndex = 1),
        GameOrderTarget(buildingId = 7, deliveryPointIndex = 1),
        GameOrderTarget(buildingId = 10, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 12, deliveryPointIndex = 1),
        GameOrderTarget(buildingId = 13, deliveryPointIndex = 1),
        GameOrderTarget(buildingId = 16, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 17, deliveryPointIndex = 2)
    )

    private val levelOneOrderPattern = listOf(
        GameOrderTarget(buildingId = 2, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 4, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 8, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 11, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 14, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 15, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 6, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 9, deliveryPointIndex = 0)
    )

    private val levelTwoOrderPattern = listOf(
        GameOrderTarget(buildingId = 3, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 5, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 7, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 10, deliveryPointIndex = 1),
        GameOrderTarget(buildingId = 12, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 13, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 16, deliveryPointIndex = 1),
        GameOrderTarget(buildingId = 17, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 1, deliveryPointIndex = 1),
        GameOrderTarget(buildingId = 6, deliveryPointIndex = 0)
    )

    private val levelThreeOrderPattern = listOf(
        GameOrderTarget(buildingId = 17, deliveryPointIndex = 1),
        GameOrderTarget(buildingId = 15, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 5, deliveryPointIndex = 1),
        GameOrderTarget(buildingId = 12, deliveryPointIndex = 1),
        GameOrderTarget(buildingId = 10, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 7, deliveryPointIndex = 1),
        GameOrderTarget(buildingId = 13, deliveryPointIndex = 1),
        GameOrderTarget(buildingId = 4, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 11, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 1, deliveryPointIndex = 0)
    )

    private val levelFourOrderPattern = listOf(
        GameOrderTarget(buildingId = 1, deliveryPointIndex = 1),
        GameOrderTarget(buildingId = 5, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 7, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 10, deliveryPointIndex = 1),
        GameOrderTarget(buildingId = 12, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 13, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 16, deliveryPointIndex = 1),
        GameOrderTarget(buildingId = 17, deliveryPointIndex = 2),
        GameOrderTarget(buildingId = 3, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 8, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 15, deliveryPointIndex = 0),
        GameOrderTarget(buildingId = 6, deliveryPointIndex = 0)
    )

    // =========================================================
    // NODOS PARA MOVIMIENTO
    // =========================================================

    val nodes: Map<String, GameMapNode> = buildNodes()

    init {
        require(routeGrid.size == GRID_ROWS) {
            "routeGrid debe tener $GRID_ROWS filas."
        }

        routeGrid.forEachIndexed { rowIndex, row ->
            require(row.length == GRID_COLUMNS) {
                "La fila $rowIndex debe tener $GRID_COLUMNS columnas."
            }
        }
    }

    // =========================================================
    // HELPERS DE PEDIDOS
    // =========================================================

    fun getOrderPatternForLevel(
        levelId: Int
    ): List<GameOrderTarget> {
        return when (levelId) {
            0 -> tutorialOrderPattern
            1 -> levelOneOrderPattern
            2 -> levelTwoOrderPattern
            3 -> levelThreeOrderPattern
            4 -> levelFourOrderPattern
            else -> tutorialOrderPattern
        }
    }

    fun getTotalOrdersForLevel(
        levelId: Int
    ): Int {
        return getOrderPatternForLevel(levelId).size
    }

    fun getOrderTarget(
        levelId: Int,
        orderIndex: Int
    ): GameOrderTarget {
        val pattern = getOrderPatternForLevel(levelId)

        return pattern[orderIndex.coerceIn(0, pattern.lastIndex)]
    }

    fun getActiveBuilding(
        levelId: Int,
        orderIndex: Int
    ): DeliveryBuilding {
        val target = getOrderTarget(
            levelId = levelId,
            orderIndex = orderIndex
        )

        return getBuilding(target.buildingId)
    }

    fun getActiveDeliveryPoint(
        levelId: Int,
        orderIndex: Int
    ): MapGridAnchor {
        val target = getOrderTarget(
            levelId = levelId,
            orderIndex = orderIndex
        )

        val building = getBuilding(target.buildingId)

        return building.deliveryPoints[
            target.deliveryPointIndex.coerceIn(
                minimumValue = 0,
                maximumValue = building.deliveryPoints.lastIndex
            )
        ]
    }

    fun getBuilding(
        buildingId: Int
    ): DeliveryBuilding {
        return buildings.first { building ->
            building.id == buildingId
        }
    }

    // =========================================================
    // HELPERS DE MAPA / MOVIMIENTO
    // =========================================================

    fun getNode(
        nodeId: String
    ): GameMapNode {
        return nodes[nodeId] ?: nodes.getValue(START_NODE_ID)
    }

    fun getStartNode(): GameMapNode {
        return getNode(START_NODE_ID)
    }

    fun getNextNode(
        currentNodeId: String,
        direction: GameMapDirection
    ): GameMapNode? {
        val currentNode = getNode(currentNodeId)
        val nextNodeId = currentNode.connections[direction]

        return nextNodeId?.let { id ->
            nodes[id]
        }
    }

    fun isWalkableCell(
        row: Int,
        column: Int
    ): Boolean {
        if (!isInsideGrid(row, column)) return false

        val symbol = routeGrid[row][column]

        return symbol == 'x' || symbol == 'Z'
    }

    fun isSafeZoneCell(
        row: Int,
        column: Int
    ): Boolean {
        if (!isInsideGrid(row, column)) return false

        return routeGrid[row][column] == 'Z'
    }

    fun isInsideGrid(
        row: Int,
        column: Int
    ): Boolean {
        return row in 0 until GRID_ROWS &&
                column in 0 until GRID_COLUMNS
    }

    fun cellCenterX(
        column: Int
    ): Float {
        return (column + 0.5f) * CELL_SIZE
    }

    fun cellCenterY(
        row: Int
    ): Float {
        return (row + 0.5f) * CELL_SIZE
    }

    fun cellForBasePosition(
        x: Float,
        y: Float
    ): GameMapCell {
        val column = (x / CELL_SIZE)
            .toInt()
            .coerceIn(0, GRID_COLUMNS - 1)

        val row = (y / CELL_SIZE)
            .toInt()
            .coerceIn(0, GRID_ROWS - 1)

        return GameMapCell(
            row = row,
            column = column
        )
    }

    // =========================================================
    // HELPERS DE CONVERSIÓN 1-BASED DISEÑO -> PX BASE
    // =========================================================

    fun designColumnCenterX(
        column: Int
    ): Float {
        return (column - 0.5f) * CELL_SIZE
    }

    fun designRowCenterY(
        row: Int
    ): Float {
        return (row - 0.5f) * CELL_SIZE
    }

    fun designAnchorX(
        anchor: MapGridAnchor
    ): Float {
        return (anchor.column - 0.5f) * CELL_SIZE
    }

    fun designAnchorY(
        anchor: MapGridAnchor
    ): Float {
        return (anchor.row - 0.5f) * CELL_SIZE
    }

    fun designAnchorToBasePosition(
        anchor: MapGridAnchor
    ): MapBasePosition {
        return MapBasePosition(
            x = designAnchorX(anchor),
            y = designAnchorY(anchor)
        )
    }

    fun gridPointToBasePosition(
        point: MapGridPoint
    ): MapBasePosition {
        return MapBasePosition(
            x = designColumnCenterX(point.column),
            y = designRowCenterY(point.row)
        )
    }

    fun getRestaurantPickupBasePosition(): MapBasePosition {
        return designAnchorToBasePosition(RESTAURANT_PICKUP_ANCHOR)
    }

    fun getRestaurantPointerBasePosition(): MapBasePosition {
        return designAnchorToBasePosition(RESTAURANT_POINTER_ANCHOR)
    }

    fun getActiveBuildingPointerBasePosition(
        levelId: Int,
        orderIndex: Int
    ): MapBasePosition {
        val building = getActiveBuilding(
            levelId = levelId,
            orderIndex = orderIndex
        )

        return designAnchorToBasePosition(building.pointerAnchor)
    }

    fun getActiveDeliveryBasePosition(
        levelId: Int,
        orderIndex: Int
    ): MapBasePosition {
        val deliveryPoint = getActiveDeliveryPoint(
            levelId = levelId,
            orderIndex = orderIndex
        )

        return designAnchorToBasePosition(deliveryPoint)
    }

    // =========================================================
    // HELPERS DE CONVERSIÓN BASE -> MAPA MOSTRADO
    // =========================================================

    fun baseToDisplayedX(
        baseX: Float,
        displayedMapWidth: Float
    ): Float {
        if (displayedMapWidth <= 0f) return 0f

        return (baseX / MAP_BASE_WIDTH) * displayedMapWidth
    }

    fun baseToDisplayedY(
        baseY: Float,
        displayedMapHeight: Float
    ): Float {
        if (displayedMapHeight <= 0f) return 0f

        return (baseY / MAP_BASE_HEIGHT) * displayedMapHeight
    }

    fun displayedToBaseX(
        displayedX: Float,
        displayedMapWidth: Float
    ): Float {
        if (displayedMapWidth <= 0f) return 0f

        return (displayedX / displayedMapWidth) * MAP_BASE_WIDTH
    }

    fun displayedToBaseY(
        displayedY: Float,
        displayedMapHeight: Float
    ): Float {
        if (displayedMapHeight <= 0f) return 0f

        return (displayedY / displayedMapHeight) * MAP_BASE_HEIGHT
    }

    // =========================================================
    // BUILD NODES
    // =========================================================

    private fun buildNodes(): Map<String, GameMapNode> {
        val walkableCells = mutableListOf<GameMapCell>()

        routeGrid.forEachIndexed { row, line ->
            line.forEachIndexed { column, _ ->
                if (isWalkableCell(row, column)) {
                    walkableCells.add(
                        GameMapCell(
                            row = row,
                            column = column
                        )
                    )
                }
            }
        }

        val walkableCellSet = walkableCells.toSet()

        return walkableCells.associate { cell ->
            val nodeId = nodeIdForCell(cell)

            nodeId to GameMapNode(
                id = nodeId,
                row = cell.row,
                column = cell.column,
                x = cellCenterX(cell.column),
                y = cellCenterY(cell.row),
                type = if (isSafeZoneCell(cell.row, cell.column)) {
                    GameMapNodeType.SAFE_ZONE
                } else {
                    GameMapNodeType.ROAD
                },
                connections = buildConnectionsForCell(
                    cell = cell,
                    walkableCellSet = walkableCellSet
                )
            )
        }
    }

    private fun buildConnectionsForCell(
        cell: GameMapCell,
        walkableCellSet: Set<GameMapCell>
    ): Map<GameMapDirection, String> {
        val connections = mutableMapOf<GameMapDirection, String>()

        val upCell = GameMapCell(
            row = cell.row - 1,
            column = cell.column
        )

        val downCell = GameMapCell(
            row = cell.row + 1,
            column = cell.column
        )

        val leftCell = GameMapCell(
            row = cell.row,
            column = cell.column - 1
        )

        val rightCell = GameMapCell(
            row = cell.row,
            column = cell.column + 1
        )

        if (upCell in walkableCellSet) {
            connections[GameMapDirection.UP] = nodeIdForCell(upCell)
        }

        if (downCell in walkableCellSet) {
            connections[GameMapDirection.DOWN] = nodeIdForCell(downCell)
        }

        if (leftCell in walkableCellSet) {
            connections[GameMapDirection.LEFT] = nodeIdForCell(leftCell)
        }

        if (rightCell in walkableCellSet) {
            connections[GameMapDirection.RIGHT] = nodeIdForCell(rightCell)
        }

        return connections
    }

    private fun nodeIdForCell(
        cell: GameMapCell
    ): String {
        return if (isSafeZoneCell(cell.row, cell.column)) {
            "safe_${cell.row}_${cell.column}"
        } else {
            "road_${cell.row}_${cell.column}"
        }
    }
}

// =========================================================
// DATA CLASSES DEL MAPA
// =========================================================

data class GameMapNode(
    val id: String,
    val row: Int,
    val column: Int,
    val x: Float,
    val y: Float,
    val type: GameMapNodeType,
    val connections: Map<GameMapDirection, String>
)

data class GameMapCell(
    val row: Int,
    val column: Int
)

data class MapBasePosition(
    val x: Float,
    val y: Float
)

/**
 * Punto entero del grid de diseño.
 *
 * Usa coordenadas 1-based:
 * columna 1..25
 * fila 1..13
 */
data class MapGridPoint(
    val column: Int,
    val row: Int
)

/**
 * Punto flexible del grid de diseño.
 *
 * Sirve para ubicaciones como:
 * columna 10.5, fila 5
 */
data class MapGridAnchor(
    val column: Float,
    val row: Float
)

data class DeliveryBuilding(
    val id: Int,
    val pointerAnchor: MapGridAnchor,
    val deliveryPoints: List<MapGridAnchor>
)

data class GameOrderTarget(
    val buildingId: Int,
    val deliveryPointIndex: Int
)

enum class GameMapDirection {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

enum class GameMapNodeType {
    ROAD,
    SAFE_ZONE
}

enum class MapObjectiveMode {
    GO_TO_PICKUP,
    GO_TO_DELIVERY
}