package ni.edu.uam.nightbiteapp.game

/**
 * Contrato general para el mapa jugable de NightBite.
 *
 * El mapa físico base puede mantenerse igual, pero cada nivel podrá usar
 * configuraciones distintas de pedidos, destinos, tiempos y enemigos.
 */
interface NightBiteMap {

    val worldWidth: Float
    val worldHeight: Float

    val restaurantNodeId: String

    val deliveryNodeIds: List<String>

    val nodes: Map<String, TutorialNode>

    val streetSegments: List<NightBiteStreetSegment>

    val decorations: List<NightBiteMapDecoration>

    fun getNode(id: String): TutorialNode

    fun getNodeOrNull(id: String): TutorialNode?

    fun getRestaurantNode(): TutorialNode

    fun getDeliveryNodes(): List<TutorialNode>

    fun getDeliveryNodeForOrder(orderNumber: Int): TutorialNode

    fun getInitialPlayerNode(): TutorialNode
}