package ni.edu.uam.nightbiteapp.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.FitViewport
import kotlin.math.sqrt

/**
 * Juego base de una jornada de NightBite usando LibGDX.
 *
 * Usa el mapa base de la ciudad y una configuración de nivel
 * para definir cantidad de pedidos, destinos y tiempo límite.
 */
class NightBiteLevelGame(
    private val onFinished: (TutorialGameResult) -> Unit,
    private val onBackToHome: () -> Unit,
    private val levelConfig: GameLevelConfig = NightBiteLevelConfigs.tutorial
) : ApplicationAdapter() {

    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: FitViewport

    private lateinit var shapes: ShapeRenderer
    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont
    private lateinit var titleFont: BitmapFont
    private lateinit var glyphLayout: GlyphLayout

    private lateinit var mapRenderer: NightBiteCityMapRenderer
    private lateinit var uiRenderer: NightBiteGameUiRenderer

    private var playerNodeId: String = NightBiteCityMap.RESTAURANT_NODE_ID

    private var playerX: Float = NightBiteCityMap.getRestaurantNode().x
    private var playerY: Float = NightBiteCityMap.getRestaurantNode().y

    private var movementStartNodeId: String = NightBiteCityMap.RESTAURANT_NODE_ID
    private var movementTargetNodeId: String? = null
    private var movementDirection: TutorialDirection? = null

    private var heldTouchDirection: TutorialDirection? = null
    private var heldTouchPointer: Int? = null
    private var heldKeyboardDirection: TutorialDirection? = null

    private var currentOrder: TutorialOrder = createOrder(orderNumber = 1)

    private var completedOrders = 0
    private var totalDeliveryTimeSeconds = 0f
    private var score = 0
    private var gameTimeSeconds = 0f
    private var resultSent = false
    private var homeNavigationRequested = false

    private var message = "Presiona ACCION para recoger el primer pedido."

    private val touchPoint = Vector3()

    private val upButton = Rectangle(26f, 258f, 64f, 58f)
    private val downButton = Rectangle(26f, 164f, 64f, 58f)

    private val leftButton = Rectangle(918f, 258f, 58f, 54f)
    private val actionButton = Rectangle(914f, 176f, 66f, 66f)
    private val rightButton = Rectangle(918f, 98f, 58f, 54f)

    private val pauseButton = Rectangle(930f, 398f, 46f, 46f)

    private val continueButton = Rectangle(400f, 270f, 200f, 48f)
    private val restartButton = Rectangle(400f, 205f, 200f, 48f)
    private val homeButton = Rectangle(400f, 140f, 200f, 48f)

    private var isPauseMenuOpen = false
    private var hasRenderedAtLeastOnce = false

    override fun create() {
        camera = OrthographicCamera()

        viewport = FitViewport(
            NightBiteCityMap.WORLD_WIDTH,
            NightBiteCityMap.WORLD_HEIGHT,
            camera
        )

        shapes = ShapeRenderer()
        batch = SpriteBatch()
        font = BitmapFont()
        titleFont = BitmapFont()
        glyphLayout = GlyphLayout()

        mapRenderer = NightBiteCityMapRenderer(
            shapes = shapes,
            cityMap = NightBiteCityMap
        )

        uiRenderer = NightBiteGameUiRenderer(
            shapes = shapes,
            batch = batch,
            font = font,
            titleFont = titleFont,
            glyphLayout = glyphLayout
        )

        font.data.setScale(1.05f)
        titleFont.data.setScale(1.25f)

        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun touchDown(
                screenX: Int,
                screenY: Int,
                pointer: Int,
                button: Int
            ): Boolean {
                handleTouchDown(
                    screenX = screenX,
                    screenY = screenY,
                    pointer = pointer
                )
                return true
            }

            override fun touchUp(
                screenX: Int,
                screenY: Int,
                pointer: Int,
                button: Int
            ): Boolean {
                if (pointer == heldTouchPointer) {
                    heldTouchDirection = null
                    heldTouchPointer = null
                }

                return true
            }

            override fun keyDown(keycode: Int): Boolean {
                when (keycode) {
                    Input.Keys.LEFT -> heldKeyboardDirection = TutorialDirection.LEFT
                    Input.Keys.RIGHT -> heldKeyboardDirection = TutorialDirection.RIGHT
                    Input.Keys.UP -> heldKeyboardDirection = TutorialDirection.UP
                    Input.Keys.DOWN -> heldKeyboardDirection = TutorialDirection.DOWN

                    Input.Keys.SPACE,
                    Input.Keys.ENTER -> handleAction()

                    Input.Keys.ESCAPE -> openPauseMenu()
                }

                return true
            }

            override fun keyUp(keycode: Int): Boolean {
                val releasedDirection = when (keycode) {
                    Input.Keys.LEFT -> TutorialDirection.LEFT
                    Input.Keys.RIGHT -> TutorialDirection.RIGHT
                    Input.Keys.UP -> TutorialDirection.UP
                    Input.Keys.DOWN -> TutorialDirection.DOWN
                    else -> null
                }

                if (releasedDirection == heldKeyboardDirection) {
                    heldKeyboardDirection = null
                }

                return true
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun resume() {
        if (hasRenderedAtLeastOnce && !resultSent) {
            openPauseMenu()
        }
    }

    override fun render() {
        val deltaTime = Gdx.graphics.deltaTime.coerceAtMost(0.05f)

        if (!isPauseMenuOpen) {
            update(deltaTime)
        }

        clearScreen()
        drawGame()

        if (isPauseMenuOpen) {
            uiRenderer.drawPauseMenu(
                continueButton = continueButton,
                restartButton = restartButton,
                homeButton = homeButton,
                worldWidth = NightBiteCityMap.WORLD_WIDTH,
                worldHeight = NightBiteCityMap.WORLD_HEIGHT
            )
        }

        hasRenderedAtLeastOnce = true
    }

    private fun update(deltaTime: Float) {
        if (resultSent) return

        gameTimeSeconds += deltaTime

        updatePlayerMovement(deltaTime)
        updateOrderTimer()
    }

    private fun updateOrderTimer() {
        if (currentOrder.hasTimeExpired(gameTimeSeconds)) {
            currentOrder = currentOrder.expire()

            message = if (isLastOrder(currentOrder.number)) {
                "El ultimo pedido vencio. Finalizando jornada..."
            } else {
                "El pedido vencio. Regresa al restaurante por el siguiente."
            }

            if (isLastOrder(currentOrder.number)) {
                finishLevel()
            }
        }
    }

    private fun handleTouchDown(
        screenX: Int,
        screenY: Int,
        pointer: Int
    ) {
        if (resultSent) return

        touchPoint.set(screenX.toFloat(), screenY.toFloat(), 0f)
        viewport.unproject(touchPoint)

        val x = touchPoint.x
        val y = touchPoint.y

        if (isPauseMenuOpen) {
            handlePauseMenuTouch(x, y)
            return
        }

        when {
            pauseButton.contains(x, y) -> openPauseMenu()

            leftButton.contains(x, y) -> holdMovementButton(
                direction = TutorialDirection.LEFT,
                pointer = pointer
            )

            rightButton.contains(x, y) -> holdMovementButton(
                direction = TutorialDirection.RIGHT,
                pointer = pointer
            )

            upButton.contains(x, y) -> holdMovementButton(
                direction = TutorialDirection.UP,
                pointer = pointer
            )

            downButton.contains(x, y) -> holdMovementButton(
                direction = TutorialDirection.DOWN,
                pointer = pointer
            )

            actionButton.contains(x, y) -> handleAction()
        }
    }

    private fun holdMovementButton(
        direction: TutorialDirection,
        pointer: Int
    ) {
        heldTouchDirection = direction
        heldTouchPointer = pointer
    }

    private fun updatePlayerMovement(deltaTime: Float) {
        val desiredDirection = getDesiredMovementDirection() ?: return

        if (movementTargetNodeId == null) {
            startMovementFromCurrentNode(desiredDirection)
            return
        }

        val activeMovementDirection = movementDirection ?: return

        val targetNodeId = when {
            desiredDirection == activeMovementDirection -> movementTargetNodeId
            desiredDirection == oppositeDirection(activeMovementDirection) -> movementStartNodeId
            else -> findNearestTurnNodeForDirection(desiredDirection)
        } ?: return

        moveTowardsNode(
            targetNodeId = targetNodeId,
            deltaTime = deltaTime
        )
    }

    private fun getDesiredMovementDirection(): TutorialDirection? {
        return heldKeyboardDirection ?: heldTouchDirection
    }

    private fun startMovementFromCurrentNode(direction: TutorialDirection) {
        val currentNode = NightBiteCityMap.getNode(playerNodeId)
        val nextNodeId = currentNode.nextNodeId(direction)

        if (nextNodeId == null) {
            message = "No hay calle en esa direccion."
            return
        }

        movementStartNodeId = playerNodeId
        movementTargetNodeId = nextNodeId
        movementDirection = direction
    }

    private fun moveTowardsNode(
        targetNodeId: String,
        deltaTime: Float
    ) {
        val targetNode = NightBiteCityMap.getNode(targetNodeId)

        val deltaX = targetNode.x - playerX
        val deltaY = targetNode.y - playerY
        val distance = sqrt(deltaX * deltaX + deltaY * deltaY)

        val movementStep = PLAYER_SPEED * deltaTime

        if (distance <= movementStep || distance <= NODE_REACH_TOLERANCE) {
            playerX = targetNode.x
            playerY = targetNode.y
            playerNodeId = targetNode.id

            movementStartNodeId = playerNodeId
            movementTargetNodeId = null
            movementDirection = null

            updateMovementMessage()
            return
        }

        playerX += (deltaX / distance) * movementStep
        playerY += (deltaY / distance) * movementStep
    }

    private fun findNearestTurnNodeForDirection(
        desiredDirection: TutorialDirection
    ): String? {
        val targetNodeId = movementTargetNodeId ?: return null

        val startNode = NightBiteCityMap.getNode(movementStartNodeId)
        val targetNode = NightBiteCityMap.getNode(targetNodeId)

        val startCanTurn = startNode.nextNodeId(desiredDirection) != null
        val targetCanTurn = targetNode.nextNodeId(desiredDirection) != null

        return when {
            startCanTurn && targetCanTurn -> {
                val distanceToStart = distanceFromPlayerToNode(startNode)
                val distanceToTarget = distanceFromPlayerToNode(targetNode)

                if (distanceToStart <= distanceToTarget) {
                    startNode.id
                } else {
                    targetNode.id
                }
            }

            startCanTurn -> startNode.id

            targetCanTurn -> targetNode.id

            else -> null
        }
    }

    private fun distanceFromPlayerToNode(node: TutorialNode): Float {
        val deltaX = node.x - playerX
        val deltaY = node.y - playerY

        return sqrt(deltaX * deltaX + deltaY * deltaY)
    }

    private fun oppositeDirection(direction: TutorialDirection): TutorialDirection {
        return when (direction) {
            TutorialDirection.LEFT -> TutorialDirection.RIGHT
            TutorialDirection.RIGHT -> TutorialDirection.LEFT
            TutorialDirection.UP -> TutorialDirection.DOWN
            TutorialDirection.DOWN -> TutorialDirection.UP
        }
    }

    private fun updateMovementMessage() {
        val isInsideRestaurant = isPlayerInsideRestaurantArea()

        message = when {
            currentOrder.isWaitingAtRestaurant && isInsideRestaurant ->
                "Presiona ACCION para recoger el pedido ${currentOrder.number}."

            currentOrder.isWaitingAtRestaurant ->
                "Regresa al restaurante para recoger el pedido."

            currentOrder.isInProgress && isPlayerAtCurrentDestination() ->
                "Llegaste al destino. Presiona ACCION para entregar."

            currentOrder.isInProgress ->
                "Sigue el apuntador amarillo hasta la casa marcada."

            currentOrder.isExpired && isInsideRestaurant ->
                "Presiona ACCION para tomar el siguiente pedido."

            currentOrder.isExpired ->
                "Pedido vencido. Vuelve al restaurante."

            else ->
                "Regresa al restaurante para continuar."
        }
    }

    private fun handleAction() {
        val isInsideRestaurant = isPlayerInsideRestaurantArea()

        when {
            currentOrder.isWaitingAtRestaurant && isInsideRestaurant -> {
                currentOrder = currentOrder.start(gameTimeSeconds)
                message = "Pedido recogido. Llevalo al destino marcado."
            }

            currentOrder.isWaitingAtRestaurant && !isInsideRestaurant -> {
                message = "Debes estar en el restaurante para recoger pedidos."
            }

            currentOrder.isInProgress && isPlayerAtCurrentDestination() -> {
                deliverCurrentOrder()
            }

            currentOrder.isInProgress && !isPlayerAtCurrentDestination() -> {
                message = "Ese no es el punto de entrega."
            }

            currentOrder.isExpired && isInsideRestaurant -> {
                goToNextOrderAfterExpired()
            }

            currentOrder.isExpired && !isInsideRestaurant -> {
                message = "Vuelve al restaurante por el siguiente pedido."
            }

            else -> {
                message = "Regresa al restaurante para continuar."
            }
        }
    }

    private fun isPlayerInsideRestaurantArea(): Boolean {
        val playerCell = getCellForPosition(
            x = playerX,
            y = playerY
        )

        val row = playerCell.first
        val column = playerCell.second

        return playerNodeId == NightBiteCityMap.RESTAURANT_NODE_ID ||
                NightBiteCityMap.isPickupCell(row, column) ||
                NightBiteCityMap.isPickupWindowCell(row, column)
    }

    private fun deliverCurrentOrder() {
        val remainingTime = currentOrder.remainingTimeSeconds(gameTimeSeconds)
        val timeBonus = (remainingTime * 5).toInt().coerceAtLeast(0)

        val startedAt = currentOrder.startedAtSeconds ?: gameTimeSeconds
        val deliveryDuration = (gameTimeSeconds - startedAt).coerceAtLeast(0f)

        totalDeliveryTimeSeconds += deliveryDuration

        completedOrders += 1
        score += 100 + timeBonus

        currentOrder = currentOrder.deliver(gameTimeSeconds)

        if (isLastOrder(currentOrder.number)) {
            message = "Todos los pedidos fueron procesados."
            finishLevel()
            return
        }

        currentOrder = createOrder(orderNumber = currentOrder.number + 1)
        message = "Entrega completada. Regresa al restaurante."
    }

    private fun goToNextOrderAfterExpired() {
        if (isLastOrder(currentOrder.number)) {
            finishLevel()
            return
        }

        currentOrder = createOrder(orderNumber = currentOrder.number + 1)
        currentOrder = currentOrder.start(gameTimeSeconds)
        message = "Nuevo pedido recogido. Llevalo al destino marcado."
    }

    private fun requestBackToHomeFromPause() {
        if (homeNavigationRequested) return

        homeNavigationRequested = true
        isPauseMenuOpen = true
        message = "Volviendo al Home..."

        Gdx.input.inputProcessor = null

        Gdx.app.postRunnable {
            onBackToHome()
        }
    }

    private fun finishLevel() {
        if (resultSent) return

        resultSent = true

        val finalResult = TutorialGameResult(
            completedOrders = completedOrders,
            totalOrders = levelConfig.totalOrders,
            score = score,
            elapsedTimeSeconds = gameTimeSeconds,
            totalDeliveryTimeSeconds = totalDeliveryTimeSeconds
        )

        Gdx.input.inputProcessor = null

        Gdx.app.postRunnable {
            onFinished(finalResult)
        }
    }

    private fun isPlayerAtCurrentDestination(): Boolean {
        if (playerNodeId == currentOrder.destinationNodeId) {
            return true
        }

        val destination = NightBiteCityMap.getNode(currentOrder.destinationNodeId)

        val deltaX = destination.x - playerX
        val deltaY = destination.y - playerY
        val distance = sqrt(deltaX * deltaX + deltaY * deltaY)

        return distance <= DELIVERY_ACTION_RADIUS
    }

    private fun getCellForPosition(
        x: Float,
        y: Float
    ): Pair<Int, Int> {
        val column = ((x - NightBiteCityMap.MAP_LEFT) / NightBiteCityMap.CELL_SIZE)
            .toInt()
            .coerceIn(0, NightBiteCityMap.GRID_COLUMNS - 1)

        val row = (NightBiteCityMap.GRID_ROWS - 1) -
                ((y - NightBiteCityMap.MAP_BOTTOM) / NightBiteCityMap.CELL_SIZE)
                    .toInt()
                    .coerceIn(0, NightBiteCityMap.GRID_ROWS - 1)

        return row to column
    }

    private fun createOrder(orderNumber: Int): TutorialOrder {
        val availableDestinations = levelConfig.deliveryNodeIds.ifEmpty {
            NightBiteCityMap.deliveryNodeIds
        }

        val safeIndex = (orderNumber - 1).coerceAtLeast(0) % availableDestinations.size
        val destinationNodeId = availableDestinations[safeIndex]
        val destination = NightBiteCityMap.getNode(destinationNodeId)

        return TutorialOrder(
            number = orderNumber,
            destinationNodeId = destination.id,
            timeLimitSeconds = levelConfig.timeLimitSeconds
        )
    }

    private fun isLastOrder(orderNumber: Int): Boolean {
        return orderNumber >= levelConfig.totalOrders
    }

    private fun openPauseMenu() {
        heldTouchDirection = null
        heldTouchPointer = null
        heldKeyboardDirection = null
        isPauseMenuOpen = true
        message = "Jornada pausada."
    }

    private fun closePauseMenu() {
        isPauseMenuOpen = false
        updateMovementMessage()
    }

    private fun restartLevel() {
        playerNodeId = NightBiteCityMap.RESTAURANT_NODE_ID

        val restaurant = NightBiteCityMap.getRestaurantNode()

        playerX = restaurant.x
        playerY = restaurant.y

        movementStartNodeId = NightBiteCityMap.RESTAURANT_NODE_ID
        movementTargetNodeId = null
        movementDirection = null
        heldTouchDirection = null
        heldTouchPointer = null
        heldKeyboardDirection = null

        currentOrder = createOrder(orderNumber = 1)
        completedOrders = 0
        score = 0
        gameTimeSeconds = 0f
        totalDeliveryTimeSeconds = 0f
        resultSent = false
        homeNavigationRequested = false
        isPauseMenuOpen = false
        message = "Presiona ACCION para recoger el primer pedido."
    }

    private fun handlePauseMenuTouch(
        x: Float,
        y: Float
    ) {
        when {
            continueButton.contains(x, y) -> {
                closePauseMenu()
            }

            restartButton.contains(x, y) -> {
                restartLevel()
            }

            homeButton.contains(x, y) -> {
                requestBackToHomeFromPause()
            }
        }
    }

    private fun clearScreen() {
        Gdx.gl.glClearColor(
            TutorialPalette.NightBackground.r,
            TutorialPalette.NightBackground.g,
            TutorialPalette.NightBackground.b,
            TutorialPalette.NightBackground.a
        )
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    private fun drawGame() {
        camera.update()

        shapes.projectionMatrix = camera.combined
        batch.projectionMatrix = camera.combined

        mapRenderer.drawMap(currentOrder)
        mapRenderer.drawActiveTarget(currentOrder)

        drawPlayer()

        uiRenderer.drawControls(
            leftButton = leftButton,
            rightButton = rightButton,
            upButton = upButton,
            downButton = downButton,
            actionButton = actionButton,
            pauseButton = pauseButton
        )

        uiRenderer.drawHud(
            title = levelConfig.title,
            completedOrders = completedOrders,
            totalOrders = levelConfig.totalOrders,
            score = score,
            currentOrder = currentOrder,
            gameTimeSeconds = gameTimeSeconds,
            message = message
        )
    }

    private fun drawPlayer() {
        shapes.begin(ShapeRenderer.ShapeType.Filled)

        shapes.color = TutorialPalette.Player
        shapes.circle(
            playerX,
            playerY,
            PLAYER_RADIUS
        )

        if (currentOrder.isInProgress) {
            shapes.color = TutorialPalette.OrderPurple
            shapes.rect(
                playerX + 7f,
                playerY + 6f,
                11f,
                11f
            )
        }

        shapes.end()
    }

    override fun dispose() {
        shapes.dispose()
        batch.dispose()
        font.dispose()
        titleFont.dispose()
    }

    companion object {
        private const val PLAYER_SPEED = 175f
        private const val PLAYER_RADIUS = 10f
        private const val NODE_REACH_TOLERANCE = 10f

        private const val DELIVERY_ACTION_RADIUS = 18f
    }
}