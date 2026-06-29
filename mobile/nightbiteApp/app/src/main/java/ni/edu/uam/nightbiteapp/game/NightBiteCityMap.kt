package ni.edu.uam.nightbiteapp.game

/**
 * Mapa base de la ciudad de NightBite.
 *
 * Este mapa usa una cuadrícula de 40 columnas x 24 filas.
 *
 * En el mundo de LibGDX:
 * - 1 celda = 20 unidades
 * - 40 columnas = 800 unidades
 * - 24 filas = 480 unidades
 *
 * En el diseño:
 * - 1 celda = 40 px
 * - 40 columnas = 1600 px
 * - 24 filas = 960 px
 */
object NightBiteCityMap : NightBiteMap {

    const val WORLD_WIDTH = 1000f
    const val WORLD_HEIGHT = 480f

    const val GRID_COLUMNS = 40
    const val GRID_ROWS = 24

    const val MAP_LEFT = 100f
    const val MAP_BOTTOM = 0f
    const val CELL_SIZE = 20f

    val mapWidth: Float
        get() = GRID_COLUMNS * CELL_SIZE

    val mapHeight: Float
        get() = GRID_ROWS * CELL_SIZE

    const val RESTAURANT_NODE_ID = "restaurant"

    override val worldWidth: Float
        get() = WORLD_WIDTH

    override val worldHeight: Float
        get() = WORLD_HEIGHT

    override val restaurantNodeId: String
        get() = RESTAURANT_NODE_ID

    /**
     * IDs usados por GameLevelConfig.
     *
     * Por ahora conservamos estos nombres para no romper el tutorial.
     */
    override val deliveryNodeIds = listOf(
        "delivery_north_west",
        "delivery_north_east",
        "delivery_west",
        "delivery_east",
        "delivery_south_west",
        "delivery_south_east",
        "delivery_bottom_west",
        "delivery_bottom_east"
    )

    /**
     * Mapa de rutas.
     *
     * x = calle donde puede pasar el repartidor
     * z = zona de recolección donde también puede pasar
     * o = zona no transitable
     */
    val routeGrid = listOf(
        "xxooooooxxooxxoooooooxxxxxxxxxxxxooooooo",
        "xxxxxxxxxxooxxoooooooxxxxxxxxxxxxooooooo",
        "xxxxxxxxxxooxxoooooooxxooooooooxxooooooo",
        "xxooooooooooxxoooooooxxooooooooxxooooooo",
        "xxooooooooooxxoooooooxxooooooooxxooooooo",
        "xxooooooooooxxoooooooxxooooooooxxooooooo",
        "xxooooooooooxxoooooooxxooooooooxxooooooo",
        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
        "xxoooooooxxooooooooxxooooooooooxxooooooo",
        "xxoooooooxxooooooooxxooooooooooxxooooooo",
        "xxoooooooxxzzzzzzooxxooooooooooxxxxxxooo",
        "xxoooooooxxzzzzzzooxxooooooooooxxxxxxooo",
        "xxoooooooxxoooozzooxxooooooooooooooooooo",
        "xxoooooooxxoooozzooxxooooxxxxxxxxooxxxxx",
        "xxoooooooxxoooozzooxxooooxxxxxxxxxxxxxxx",
        "xxoooooooxxoooozzooxxooooxxxxxxxxxxxoooo",
        "xxxxxxxxxxxxxxxxxxxxxoooooooooooooxxoooo",
        "xxxxxxxxxxxxxxxxxxxxxoooooooooooooxxoooo",
        "ooooooxxoooooooooooxxoooooooooooooxxoooo",
        "ooooooxxoooooooooooxxxxxxxxxxxxxxxxxxxxx",
        "ooooooxxoooooooooooxxxxxxxxxxxxxxxxxxxxx",
        "ooooooxxoooooooooooxxooooooooooooooooooo",
        "ooooooxxoooooooooooxxooooooooooooooooooo"
    )

    /**
     * Mapa visual de objetos.
     *
     * R = restaurante NightBite
     * V = ventanilla / zona de recolección
     * E = punto exacto de entrega
     * Q = edificio de entrega
     */
    val objectGrid = listOf(
        "xxooooooxxooxxoooooooxxxxxxxxxxxxooooooo",
        "xxxxxxxxxxooxxoooooooxxxxxxxxxxxxooooooo",
        "xxxxxxxxxxooxxoooooooxxooooooooxxooooooo",
        "xEQQooooooooxxoooooooxxooooooQQxxooooooo",
        "xEQQooooooooxxoooooooxxooooooQQExooooooo",
        "xxQQooooooooxxoooooooxxooooooQQExooooooo",
        "xxQQooooooooxxoooooooxxooooooQQxxooooooo",
        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
        "xxoooooooxxooooooooxxooooooooooxxooooooo",
        "xxoooooooxxooooooooxxooooooooooxxooooQQQ",
        "xxoooooooxxzzzzzzooxxooooooooooxxxxxEQQQ",
        "xxoooooooxxzzVzzzooxxooooooooQQxxxxxEQQQ",
        "xxQQoooooxxRRRRzzooxxooooooooQQooooooQQQ",
        "xEQQoooooxxRRRRVzooxxooooxxxxEExxooxxxxx",
        "xEQQoooooxxRRRRzzooxxooooxxxxxxxxxxxxxxx",
        "xxQQoooooxxRRRRzzooxxooooxxxxxxxxxxxoooo",
        "xxxxxxxxxxxxxxxxxxxxxoooooooooooooxxoooo",
        "xxxxxxxxxxxxxxxxxxxxxoooooooooooooxxQQoo",
        "ooooooxxoooooooooooxxoooooooooooooxxQQoo",
        "ooooooxxoooooooooooxxxxxxxxxxxxxxxxxEExx",
        "ooooooxxoooooooooooxxxxEExxxxxxxEExxxxxx",
        "ooooooxxoooooooooooxxooQQoooooooQQoooooo",
        "ooooooxxoooooooooooxxooQQoooooooQQoooooo"
    )

    /**
     * Posición del nodo principal del restaurante.
     *
     * Está sobre una V, no dentro de las R, para que el repartidor
     * pueda recoger pedidos desde una zona transitable.
     */
    private val restaurantNodeCell = 14 to 15

    /**
     * Posiciones principales de entrega.
     *
     * Todas están sobre celdas E transitables.
     */
    private val deliveryNodeCells = mapOf(
        "delivery_north_west" to (4 to 1),
        "delivery_north_east" to (4 to 31),
        "delivery_west" to (14 to 1),
        "delivery_east" to (11 to 36),
        "delivery_south_west" to (14 to 29),
        "delivery_south_east" to (20 to 36),
        "delivery_bottom_west" to (21 to 23),
        "delivery_bottom_east" to (21 to 32)
    )

    private val specialNodeIdsByCell: Map<Pair<Int, Int>, String> =
        mutableMapOf<Pair<Int, Int>, String>().apply {
            this[restaurantNodeCell] = RESTAURANT_NODE_ID

            deliveryNodeCells.forEach { entry ->
                this[entry.value] = entry.key
            }
        }

    override val nodes: Map<String, TutorialNode> = buildNodes()

    override val streetSegments: List<NightBiteStreetSegment> = buildStreetSegments()

    /**
     * Por ahora las decoraciones visuales se dibujarán desde objectGrid
     * en el renderer, no desde esta lista.
     */
    override val decorations: List<NightBiteMapDecoration> = emptyList()

    override fun getNode(id: String): TutorialNode {
        return nodes[id] ?: getRestaurantNode()
    }

    override fun getNodeOrNull(id: String): TutorialNode? {
        return nodes[id]
    }

    override fun getRestaurantNode(): TutorialNode {
        return nodes.getValue(RESTAURANT_NODE_ID)
    }

    override fun getDeliveryNodes(): List<TutorialNode> {
        return deliveryNodeIds.mapNotNull { nodeId ->
            nodes[nodeId]
        }
    }

    override fun getDeliveryNodeForOrder(orderNumber: Int): TutorialNode {
        val safeIndex = (orderNumber - 1).coerceAtLeast(0) % deliveryNodeIds.size
        return getNode(deliveryNodeIds[safeIndex])
    }

    override fun getInitialPlayerNode(): TutorialNode {
        return getRestaurantNode()
    }

    fun cellCenterX(column: Int): Float {
        return MAP_LEFT + ((column + 0.5f) * CELL_SIZE)
    }

    fun cellCenterY(row: Int): Float {
        return MAP_BOTTOM + ((GRID_ROWS - row - 0.5f) * CELL_SIZE)
    }

    fun isInsideGrid(
        row: Int,
        column: Int
    ): Boolean {
        return row in 0 until GRID_ROWS &&
                column in 0 until GRID_COLUMNS
    }

    fun getRouteSymbol(
        row: Int,
        column: Int
    ): Char {
        if (!isInsideGrid(row, column)) return 'o'
        return routeGrid[row][column]
    }

    fun getObjectSymbol(
        row: Int,
        column: Int
    ): Char {
        if (!isInsideGrid(row, column)) return 'o'
        return objectGrid[row][column]
    }

    fun isWalkableCell(
        row: Int,
        column: Int
    ): Boolean {
        return getRouteSymbol(row, column) == 'x' ||
                getRouteSymbol(row, column) == 'z'
    }

    fun isPickupCell(
        row: Int,
        column: Int
    ): Boolean {
        return getRouteSymbol(row, column) == 'z'
    }

    fun isDeliveryBuildingCell(
        row: Int,
        column: Int
    ): Boolean {
        return getObjectSymbol(row, column) == 'Q'
    }

    fun isRestaurantBuildingCell(
        row: Int,
        column: Int
    ): Boolean {
        return getObjectSymbol(row, column) == 'R'
    }

    fun isDeliveryTargetCell(
        row: Int,
        column: Int
    ): Boolean {
        return getObjectSymbol(row, column) == 'E'
    }

    fun isPickupWindowCell(
        row: Int,
        column: Int
    ): Boolean {
        return getObjectSymbol(row, column) == 'V'
    }

    private fun buildNodes(): Map<String, TutorialNode> {
        val walkableCells = mutableListOf<Pair<Int, Int>>()

        routeGrid.forEachIndexed { row, line ->
            line.forEachIndexed { column, _ ->
                if (isWalkableCell(row, column)) {
                    walkableCells.add(row to column)
                }
            }
        }

        val walkableCellSet = walkableCells.toSet()

        return walkableCells.associate { cell ->
            val row = cell.first
            val column = cell.second
            val nodeId = nodeIdForCell(row, column)

            nodeId to TutorialNode(
                id = nodeId,
                x = cellCenterX(column),
                y = cellCenterY(row),
                type = nodeTypeForCell(row, column, nodeId),
                connections = buildConnectionsForCell(
                    row = row,
                    column = column,
                    walkableCellSet = walkableCellSet
                )
            )
        }
    }

    private fun nodeTypeForCell(
        row: Int,
        column: Int,
        nodeId: String
    ): TutorialNodeType {
        return when {
            nodeId == RESTAURANT_NODE_ID -> TutorialNodeType.RESTAURANT
            nodeId in deliveryNodeIds -> TutorialNodeType.DELIVERY
            isPickupWindowCell(row, column) -> TutorialNodeType.RESTAURANT
            isDeliveryTargetCell(row, column) -> TutorialNodeType.DELIVERY
            else -> TutorialNodeType.ROAD
        }
    }

    private fun buildConnectionsForCell(
        row: Int,
        column: Int,
        walkableCellSet: Set<Pair<Int, Int>>
    ): Map<TutorialDirection, String> {
        val connections = mutableMapOf<TutorialDirection, String>()

        val upCell = (row - 1) to column
        val downCell = (row + 1) to column
        val leftCell = row to (column - 1)
        val rightCell = row to (column + 1)

        if (upCell in walkableCellSet) {
            connections[TutorialDirection.UP] = nodeIdForCell(upCell.first, upCell.second)
        }

        if (downCell in walkableCellSet) {
            connections[TutorialDirection.DOWN] = nodeIdForCell(downCell.first, downCell.second)
        }

        if (leftCell in walkableCellSet) {
            connections[TutorialDirection.LEFT] = nodeIdForCell(leftCell.first, leftCell.second)
        }

        if (rightCell in walkableCellSet) {
            connections[TutorialDirection.RIGHT] = nodeIdForCell(rightCell.first, rightCell.second)
        }

        return connections
    }

    private fun nodeIdForCell(
        row: Int,
        column: Int
    ): String {
        return specialNodeIdsByCell[row to column]
            ?: "road_${row}_${column}"
    }

    private fun buildStreetSegments(): List<NightBiteStreetSegment> {
        val visitedConnections = mutableSetOf<String>()
        val segments = mutableListOf<NightBiteStreetSegment>()

        nodes.values.forEach { node ->
            node.connections.values.forEach { targetNodeId ->
                val targetNode = nodes[targetNodeId] ?: return@forEach

                val connectionKey = listOf(
                    node.id,
                    targetNode.id
                ).sorted().joinToString(separator = "|")

                if (visitedConnections.add(connectionKey)) {
                    segments.add(
                        NightBiteStreetSegment(
                            startX = node.x,
                            startY = node.y,
                            endX = targetNode.x,
                            endY = targetNode.y
                        )
                    )
                }
            }
        }

        return segments
    }
}