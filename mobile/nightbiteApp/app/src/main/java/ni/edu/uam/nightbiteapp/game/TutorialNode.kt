package ni.edu.uam.nightbiteapp.game

/**
 * Dirección lógica de movimiento del repartidor.
 *
 * En el tutorial el jugador no se mueve libremente por toda la pantalla,
 * sino entre nodos conectados que representan calles y callejones.
 */
enum class TutorialDirection {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

/**
 * Tipo de nodo dentro del mapa del tutorial.
 *
 * ROAD:
 * Calle normal por donde el jugador puede pasar.
 *
 * RESTAURANT:
 * Punto central donde se recogen los pedidos.
 *
 * DELIVERY:
 * Punto de entrega posible.
 */
enum class TutorialNodeType {
    ROAD,
    RESTAURANT,
    DELIVERY
}

/**
 * Nodo caminable del mapa.
 *
 * Cada nodo representa una posición importante de la calle.
 * Las conexiones indican hacia dónde puede moverse el jugador.
 */
data class TutorialNode(
    val id: String,
    val x: Float,
    val y: Float,
    val type: TutorialNodeType = TutorialNodeType.ROAD,
    val connections: Map<TutorialDirection, String> = emptyMap()
) {
    val isRestaurant: Boolean
        get() = type == TutorialNodeType.RESTAURANT

    val isDeliveryPoint: Boolean
        get() = type == TutorialNodeType.DELIVERY

    fun nextNodeId(direction: TutorialDirection): String? {
        return connections[direction]
    }
}