package ni.edu.uam.nightbiteapp.game

/**
 * Segmento visual de calle.
 *
 * Se usa para dibujar las calles grises entre nodos conectados.
 */
data class TutorialStreetSegment(
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float
)

/**
 * Tipo de decoración del mapa.
 *
 * Estas decoraciones NO son caminables ni puntos de entrega.
 * Solo ayudan a que el tutorial se vea más parecido a una ciudad.
 */
enum class TutorialDecorationType {
    BUILDING,
    PARK,
    CEMETERY
}

/**
 * Rectángulo decorativo del mapa.
 */
data class TutorialMapDecoration(
    val id: String,
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val type: TutorialDecorationType
)

/**
 * Mapa simplificado del tutorial.
 *
 * Por ahora no usamos Tiled. El mapa se dibuja con figuras simples
 * y el jugador solo puede moverse entre nodos conectados.
 */
object TutorialMap {

    const val WORLD_WIDTH = 800f
    const val WORLD_HEIGHT = 480f

    const val RESTAURANT_NODE_ID = "restaurant"

    val deliveryNodeIds = listOf(
        "delivery_north_west",
        "delivery_north_east",
        "delivery_west",
        "delivery_east",
        "delivery_south_west",
        "delivery_south_east",
        "delivery_bottom_west",
        "delivery_bottom_east"
    )

    val nodes: Map<String, TutorialNode> = buildNodes()

    val streetSegments: List<TutorialStreetSegment> = buildStreetSegments()

    val decorations: List<TutorialMapDecoration> = listOf(
        // Cementerio decorativo, no se usa en tutorial
        TutorialMapDecoration(
            id = "cemetery_top_right",
            x = 650f,
            y = 365f,
            width = 105f,
            height = 65f,
            type = TutorialDecorationType.CEMETERY
        ),

        // Parques / plazas decorativas
        TutorialMapDecoration(
            id = "park_top_left",
            x = 70f,
            y = 355f,
            width = 90f,
            height = 55f,
            type = TutorialDecorationType.PARK
        ),
        TutorialMapDecoration(
            id = "park_center_left",
            x = 250f,
            y = 250f,
            width = 75f,
            height = 45f,
            type = TutorialDecorationType.PARK
        ),
        TutorialMapDecoration(
            id = "park_center_right",
            x = 525f,
            y = 250f,
            width = 75f,
            height = 45f,
            type = TutorialDecorationType.PARK
        ),
        TutorialMapDecoration(
            id = "park_bottom_center",
            x = 355f,
            y = 25f,
            width = 90f,
            height = 45f,
            type = TutorialDecorationType.PARK
        ),

        // Bloques azules tipo casas / edificios
        TutorialMapDecoration(
            id = "building_top_center",
            x = 350f,
            y = 385f,
            width = 100f,
            height = 45f,
            type = TutorialDecorationType.BUILDING
        ),
        TutorialMapDecoration(
            id = "building_left_center",
            x = 60f,
            y = 205f,
            width = 75f,
            height = 50f,
            type = TutorialDecorationType.BUILDING
        ),
        TutorialMapDecoration(
            id = "building_right_center",
            x = 665f,
            y = 205f,
            width = 75f,
            height = 50f,
            type = TutorialDecorationType.BUILDING
        ),
        TutorialMapDecoration(
            id = "building_bottom_left",
            x = 80f,
            y = 70f,
            width = 85f,
            height = 50f,
            type = TutorialDecorationType.BUILDING
        ),
        TutorialMapDecoration(
            id = "building_bottom_right",
            x = 635f,
            y = 70f,
            width = 85f,
            height = 50f,
            type = TutorialDecorationType.BUILDING
        )
    )

    fun getNode(id: String): TutorialNode {
        return nodes[id] ?: getRestaurantNode()
    }

    fun getNodeOrNull(id: String): TutorialNode? {
        return nodes[id]
    }

    fun getRestaurantNode(): TutorialNode {
        return nodes.getValue(RESTAURANT_NODE_ID)
    }

    fun getDeliveryNodes(): List<TutorialNode> {
        return deliveryNodeIds.mapNotNull { nodeId ->
            nodes[nodeId]
        }
    }

    fun getDeliveryNodeForOrder(orderNumber: Int): TutorialNode {
        val safeIndex = (orderNumber - 1).coerceAtLeast(0) % deliveryNodeIds.size
        return getNode(deliveryNodeIds[safeIndex])
    }

    fun getInitialPlayerNode(): TutorialNode {
        return getRestaurantNode()
    }

    private fun buildNodes(): Map<String, TutorialNode> {
        return mapOf(
            RESTAURANT_NODE_ID to TutorialNode(
                id = RESTAURANT_NODE_ID,
                x = 400f,
                y = 230f,
                type = TutorialNodeType.RESTAURANT,
                connections = mapOf(
                    TutorialDirection.UP to "north_1",
                    TutorialDirection.DOWN to "south_1",
                    TutorialDirection.LEFT to "west_1",
                    TutorialDirection.RIGHT to "east_1"
                )
            ),

            // Camino vertical superior
            "north_1" to TutorialNode(
                id = "north_1",
                x = 400f,
                y = 300f,
                connections = mapOf(
                    TutorialDirection.DOWN to RESTAURANT_NODE_ID,
                    TutorialDirection.UP to "north_2",
                    TutorialDirection.LEFT to "north_west_1",
                    TutorialDirection.RIGHT to "north_east_1"
                )
            ),
            "north_2" to TutorialNode(
                id = "north_2",
                x = 400f,
                y = 370f,
                connections = mapOf(
                    TutorialDirection.DOWN to "north_1",
                    TutorialDirection.LEFT to "top_west_1",
                    TutorialDirection.RIGHT to "top_east_1"
                )
            ),

            // Camino vertical inferior
            "south_1" to TutorialNode(
                id = "south_1",
                x = 400f,
                y = 160f,
                connections = mapOf(
                    TutorialDirection.UP to RESTAURANT_NODE_ID,
                    TutorialDirection.DOWN to "south_2",
                    TutorialDirection.LEFT to "south_west_1",
                    TutorialDirection.RIGHT to "south_east_1"
                )
            ),
            "south_2" to TutorialNode(
                id = "south_2",
                x = 400f,
                y = 90f,
                connections = mapOf(
                    TutorialDirection.UP to "south_1",
                    TutorialDirection.LEFT to "bottom_west_1",
                    TutorialDirection.RIGHT to "bottom_east_1"
                )
            ),

            // Camino horizontal izquierdo
            "west_1" to TutorialNode(
                id = "west_1",
                x = 310f,
                y = 230f,
                connections = mapOf(
                    TutorialDirection.RIGHT to RESTAURANT_NODE_ID,
                    TutorialDirection.LEFT to "west_2",
                    TutorialDirection.UP to "north_west_1",
                    TutorialDirection.DOWN to "south_west_1"
                )
            ),
            "west_2" to TutorialNode(
                id = "west_2",
                x = 220f,
                y = 230f,
                connections = mapOf(
                    TutorialDirection.RIGHT to "west_1",
                    TutorialDirection.LEFT to "delivery_west",
                    TutorialDirection.UP to "north_west_2",
                    TutorialDirection.DOWN to "south_west_2"
                )
            ),
            "delivery_west" to TutorialNode(
                id = "delivery_west",
                x = 130f,
                y = 230f,
                type = TutorialNodeType.DELIVERY,
                connections = mapOf(
                    TutorialDirection.RIGHT to "west_2"
                )
            ),

            // Camino horizontal derecho
            "east_1" to TutorialNode(
                id = "east_1",
                x = 490f,
                y = 230f,
                connections = mapOf(
                    TutorialDirection.LEFT to RESTAURANT_NODE_ID,
                    TutorialDirection.RIGHT to "east_2",
                    TutorialDirection.UP to "north_east_1",
                    TutorialDirection.DOWN to "south_east_1"
                )
            ),
            "east_2" to TutorialNode(
                id = "east_2",
                x = 580f,
                y = 230f,
                connections = mapOf(
                    TutorialDirection.LEFT to "east_1",
                    TutorialDirection.RIGHT to "delivery_east",
                    TutorialDirection.UP to "north_east_2",
                    TutorialDirection.DOWN to "south_east_2"
                )
            ),
            "delivery_east" to TutorialNode(
                id = "delivery_east",
                x = 670f,
                y = 230f,
                type = TutorialNodeType.DELIVERY,
                connections = mapOf(
                    TutorialDirection.LEFT to "east_2"
                )
            ),

            // Zona superior izquierda
            "north_west_1" to TutorialNode(
                id = "north_west_1",
                x = 310f,
                y = 300f,
                connections = mapOf(
                    TutorialDirection.RIGHT to "north_1",
                    TutorialDirection.DOWN to "west_1",
                    TutorialDirection.LEFT to "north_west_2"
                )
            ),
            "north_west_2" to TutorialNode(
                id = "north_west_2",
                x = 220f,
                y = 300f,
                connections = mapOf(
                    TutorialDirection.RIGHT to "north_west_1",
                    TutorialDirection.DOWN to "west_2",
                    TutorialDirection.LEFT to "delivery_north_west"
                )
            ),
            "delivery_north_west" to TutorialNode(
                id = "delivery_north_west",
                x = 130f,
                y = 300f,
                type = TutorialNodeType.DELIVERY,
                connections = mapOf(
                    TutorialDirection.RIGHT to "north_west_2"
                )
            ),

            // Zona superior derecha
            "north_east_1" to TutorialNode(
                id = "north_east_1",
                x = 490f,
                y = 300f,
                connections = mapOf(
                    TutorialDirection.LEFT to "north_1",
                    TutorialDirection.DOWN to "east_1",
                    TutorialDirection.RIGHT to "north_east_2"
                )
            ),
            "north_east_2" to TutorialNode(
                id = "north_east_2",
                x = 580f,
                y = 300f,
                connections = mapOf(
                    TutorialDirection.LEFT to "north_east_1",
                    TutorialDirection.DOWN to "east_2",
                    TutorialDirection.RIGHT to "delivery_north_east"
                )
            ),
            "delivery_north_east" to TutorialNode(
                id = "delivery_north_east",
                x = 670f,
                y = 300f,
                type = TutorialNodeType.DELIVERY,
                connections = mapOf(
                    TutorialDirection.LEFT to "north_east_2"
                )
            ),

            // Callejón superior izquierdo
            "top_west_1" to TutorialNode(
                id = "top_west_1",
                x = 310f,
                y = 370f,
                connections = mapOf(
                    TutorialDirection.RIGHT to "north_2",
                    TutorialDirection.LEFT to "top_west_2"
                )
            ),
            "top_west_2" to TutorialNode(
                id = "top_west_2",
                x = 220f,
                y = 370f,
                connections = mapOf(
                    TutorialDirection.RIGHT to "top_west_1"
                )
            ),

            // Callejón superior derecho
            "top_east_1" to TutorialNode(
                id = "top_east_1",
                x = 490f,
                y = 370f,
                connections = mapOf(
                    TutorialDirection.LEFT to "north_2",
                    TutorialDirection.RIGHT to "top_east_2"
                )
            ),
            "top_east_2" to TutorialNode(
                id = "top_east_2",
                x = 580f,
                y = 370f,
                connections = mapOf(
                    TutorialDirection.LEFT to "top_east_1"
                )
            ),

            // Zona inferior izquierda
            "south_west_1" to TutorialNode(
                id = "south_west_1",
                x = 310f,
                y = 160f,
                connections = mapOf(
                    TutorialDirection.RIGHT to "south_1",
                    TutorialDirection.UP to "west_1",
                    TutorialDirection.LEFT to "south_west_2"
                )
            ),
            "south_west_2" to TutorialNode(
                id = "south_west_2",
                x = 220f,
                y = 160f,
                connections = mapOf(
                    TutorialDirection.RIGHT to "south_west_1",
                    TutorialDirection.UP to "west_2",
                    TutorialDirection.LEFT to "delivery_south_west"
                )
            ),
            "delivery_south_west" to TutorialNode(
                id = "delivery_south_west",
                x = 130f,
                y = 160f,
                type = TutorialNodeType.DELIVERY,
                connections = mapOf(
                    TutorialDirection.RIGHT to "south_west_2"
                )
            ),

            // Zona inferior derecha
            "south_east_1" to TutorialNode(
                id = "south_east_1",
                x = 490f,
                y = 160f,
                connections = mapOf(
                    TutorialDirection.LEFT to "south_1",
                    TutorialDirection.UP to "east_1",
                    TutorialDirection.RIGHT to "south_east_2"
                )
            ),
            "south_east_2" to TutorialNode(
                id = "south_east_2",
                x = 580f,
                y = 160f,
                connections = mapOf(
                    TutorialDirection.LEFT to "south_east_1",
                    TutorialDirection.UP to "east_2",
                    TutorialDirection.RIGHT to "delivery_south_east"
                )
            ),
            "delivery_south_east" to TutorialNode(
                id = "delivery_south_east",
                x = 670f,
                y = 160f,
                type = TutorialNodeType.DELIVERY,
                connections = mapOf(
                    TutorialDirection.LEFT to "south_east_2"
                )
            ),

            // Callejón inferior izquierdo
            "bottom_west_1" to TutorialNode(
                id = "bottom_west_1",
                x = 310f,
                y = 90f,
                connections = mapOf(
                    TutorialDirection.RIGHT to "south_2",
                    TutorialDirection.LEFT to "delivery_bottom_west"
                )
            ),
            "delivery_bottom_west" to TutorialNode(
                id = "delivery_bottom_west",
                x = 220f,
                y = 90f,
                type = TutorialNodeType.DELIVERY,
                connections = mapOf(
                    TutorialDirection.RIGHT to "bottom_west_1"
                )
            ),

            // Callejón inferior derecho
            "bottom_east_1" to TutorialNode(
                id = "bottom_east_1",
                x = 490f,
                y = 90f,
                connections = mapOf(
                    TutorialDirection.LEFT to "south_2",
                    TutorialDirection.RIGHT to "delivery_bottom_east"
                )
            ),
            "delivery_bottom_east" to TutorialNode(
                id = "delivery_bottom_east",
                x = 580f,
                y = 90f,
                type = TutorialNodeType.DELIVERY,
                connections = mapOf(
                    TutorialDirection.LEFT to "bottom_east_1"
                )
            )
        )
    }

    private fun buildStreetSegments(): List<TutorialStreetSegment> {
        val visitedConnections = mutableSetOf<String>()
        val segments = mutableListOf<TutorialStreetSegment>()

        nodes.values.forEach { node ->
            node.connections.values.forEach { targetNodeId ->
                val targetNode = nodes[targetNodeId] ?: return@forEach

                val connectionKey = listOf(
                    node.id,
                    targetNode.id
                ).sorted().joinToString(separator = "|")

                if (visitedConnections.add(connectionKey)) {
                    segments.add(
                        TutorialStreetSegment(
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