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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Tutorial jugable de NightBite usando LibGDX.
 *
 * Objetivo del tutorial:
 * - Ir al restaurante.
 * - Recoger pedido.
 * - Ir a la casa señalada.
 * - Entregar a tiempo.
 * - Volver al restaurante.
 * - Repetir hasta completar 8 pedidos.
 *
 * En esta primera versión:
 * - No hay enemigos.
 * - No hay vidas.
 * - No hay zona segura.
 * - El jugador se mueve por nodos que representan calles.
 */
class TutorialGame(
    private val onFinished: (TutorialGameResult) -> Unit,
    private val onBackToHome: () -> Unit
) : ApplicationAdapter() {

    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: FitViewport
    private lateinit var shapes: ShapeRenderer
    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont
    private lateinit var titleFont: BitmapFont
    private lateinit var glyphLayout: GlyphLayout

    private var playerNodeId: String = TutorialMap.RESTAURANT_NODE_ID
    private var currentOrder: TutorialOrder = createOrder(orderNumber = 1)

    private var completedOrders = 0
    private var totalDeliveryTimeSeconds = 0f
    private var score = 0
    private var gameTimeSeconds = 0f
    private var resultSent = false
    private var homeNavigationRequested = false

    private var message = "Presiona ACCION para recoger el primer pedido."

    private val touchPoint = Vector3()

    private val leftButton = Rectangle(24f, 24f, 72f, 54f)
    private val rightButton = Rectangle(114f, 24f, 72f, 54f)
    private val upButton = Rectangle(204f, 24f, 72f, 54f)
    private val downButton = Rectangle(294f, 24f, 72f, 54f)
    private val actionButton = Rectangle(610f, 24f, 160f, 54f)

    private val pauseButton = Rectangle(720f, 392f, 58f, 34f)

    private val continueButton = Rectangle(300f, 270f, 200f, 48f)
    private val restartButton = Rectangle(300f, 205f, 200f, 48f)
    private val homeButton = Rectangle(300f, 140f, 200f, 48f)

    private var isPauseMenuOpen = false
    private var hasRenderedAtLeastOnce = false

    override fun create() {
        camera = OrthographicCamera()
        viewport = FitViewport(
            TutorialMap.WORLD_WIDTH,
            TutorialMap.WORLD_HEIGHT,
            camera
        )

        shapes = ShapeRenderer()
        batch = SpriteBatch()
        font = BitmapFont()
        titleFont = BitmapFont()
        glyphLayout = GlyphLayout()

        font.data.setScale(1.05f)
        titleFont.data.setScale(1.25f)

        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun touchDown(
                screenX: Int,
                screenY: Int,
                pointer: Int,
                button: Int
            ): Boolean {
                handleTouch(screenX, screenY)
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
            drawPauseMenu()
        }

        hasRenderedAtLeastOnce = true
    }

    private fun update(deltaTime: Float) {
        if (resultSent) return

        gameTimeSeconds += deltaTime

        handleKeyboardInput()
        updateOrderTimer()
    }

    private fun handleKeyboardInput() {
        when {
            Gdx.input.isKeyJustPressed(Input.Keys.LEFT) -> movePlayer(TutorialDirection.LEFT)
            Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) -> movePlayer(TutorialDirection.RIGHT)
            Gdx.input.isKeyJustPressed(Input.Keys.UP) -> movePlayer(TutorialDirection.UP)
            Gdx.input.isKeyJustPressed(Input.Keys.DOWN) -> movePlayer(TutorialDirection.DOWN)
            Gdx.input.isKeyJustPressed(Input.Keys.SPACE) -> handleAction()
            Gdx.input.isKeyJustPressed(Input.Keys.ENTER) -> handleAction()
        }
    }

    private fun updateOrderTimer() {
        if (currentOrder.hasTimeExpired(gameTimeSeconds)) {
            currentOrder = currentOrder.expire()

            message = if (currentOrder.number >= TutorialGameResult.TOTAL_TUTORIAL_ORDERS) {
                "El ultimo pedido vencio. Finalizando tutorial..."
            } else {
                "El pedido vencio. Regresa al restaurante por el siguiente."
            }

            if (currentOrder.number >= TutorialGameResult.TOTAL_TUTORIAL_ORDERS) {
                finishTutorial()
            }
        }
    }

    private fun handleTouch(
        screenX: Int,
        screenY: Int
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
            leftButton.contains(x, y) -> movePlayer(TutorialDirection.LEFT)
            rightButton.contains(x, y) -> movePlayer(TutorialDirection.RIGHT)
            upButton.contains(x, y) -> movePlayer(TutorialDirection.UP)
            downButton.contains(x, y) -> movePlayer(TutorialDirection.DOWN)
            actionButton.contains(x, y) -> handleAction()
        }
    }

    private fun movePlayer(direction: TutorialDirection) {
        val currentNode = TutorialMap.getNode(playerNodeId)
        val nextNodeId = currentNode.nextNodeId(direction)

        if (nextNodeId == null) {
            message = "No hay calle en esa direccion."
            return
        }

        playerNodeId = nextNodeId

        updateMovementMessage()
    }

    private fun updateMovementMessage() {
        val playerNode = TutorialMap.getNode(playerNodeId)

        message = when {
            currentOrder.isWaitingAtRestaurant && playerNode.isRestaurant ->
                "Presiona ACCION para recoger el pedido ${currentOrder.number}."

            currentOrder.isWaitingAtRestaurant ->
                "Regresa al restaurante para recoger el pedido."

            currentOrder.isInProgress && isPlayerAtCurrentDestination() ->
                "Llegaste al destino. Presiona ACCION para entregar."

            currentOrder.isInProgress ->
                "Sigue el apuntador amarillo hasta la casa marcada."

            currentOrder.isExpired && playerNode.isRestaurant ->
                "Presiona ACCION para tomar el siguiente pedido."

            currentOrder.isExpired ->
                "Pedido vencido. Vuelve al restaurante."

            else ->
                "Regresa al restaurante para continuar."
        }
    }

    private fun handleAction() {
        val playerNode = TutorialMap.getNode(playerNodeId)

        when {
            currentOrder.isWaitingAtRestaurant && playerNode.isRestaurant -> {
                currentOrder = currentOrder.start(gameTimeSeconds)
                message = "Pedido recogido. Llevalo al destino marcado."
            }

            currentOrder.isWaitingAtRestaurant && !playerNode.isRestaurant -> {
                message = "Debes estar en el restaurante para recoger pedidos."
            }

            currentOrder.isInProgress && isPlayerAtCurrentDestination() -> {
                deliverCurrentOrder()
            }

            currentOrder.isInProgress && !isPlayerAtCurrentDestination() -> {
                message = "Ese no es el punto de entrega."
            }

            currentOrder.isExpired && playerNode.isRestaurant -> {
                goToNextOrderAfterExpired()
            }

            currentOrder.isExpired && !playerNode.isRestaurant -> {
                message = "Vuelve al restaurante por el siguiente pedido."
            }

            else -> {
                message = "Regresa al restaurante para continuar."
            }
        }
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

        if (currentOrder.number >= TutorialGameResult.TOTAL_TUTORIAL_ORDERS) {
            message = "Todos los pedidos fueron procesados."
            finishTutorial()
            return
        }

        currentOrder = createOrder(orderNumber = currentOrder.number + 1)
        message = "Entrega completada. Regresa al restaurante."
    }

    private fun goToNextOrderAfterExpired() {
        if (currentOrder.number >= TutorialGameResult.TOTAL_TUTORIAL_ORDERS) {
            finishTutorial()
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

    private fun finishTutorial() {
        if (resultSent) return

        resultSent = true

        val finalResult = TutorialGameResult(
            completedOrders = completedOrders,
            totalOrders = TutorialGameResult.TOTAL_TUTORIAL_ORDERS,
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
        return playerNodeId == currentOrder.destinationNodeId
    }

    private fun createOrder(orderNumber: Int): TutorialOrder {
        val destination = TutorialMap.getDeliveryNodeForOrder(orderNumber)

        return TutorialOrder(
            number = orderNumber,
            destinationNodeId = destination.id
        )
    }

    private fun openPauseMenu() {
        isPauseMenuOpen = true
        message = "Jornada pausada."
    }

    private fun closePauseMenu() {
        isPauseMenuOpen = false
        updateMovementMessage()
    }

    private fun restartTutorial() {
        playerNodeId = TutorialMap.RESTAURANT_NODE_ID
        currentOrder = createOrder(orderNumber = 1)
        completedOrders = 0
        score = 0
        gameTimeSeconds = 0f
        totalDeliveryTimeSeconds = 0f
        resultSent = false
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
                restartTutorial()
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

        drawMap()
        drawActiveTarget()
        drawPlayer()
        drawControls()
        drawHud()
    }

    private fun drawMap() {
        shapes.begin(ShapeRenderer.ShapeType.Filled)

        drawMapBoundary()
        drawDecorations()
        drawStreets()
        drawDeliveryPoints()
        drawRestaurant()

        shapes.end()
    }

    private fun drawMapBoundary() {
        shapes.color = TutorialPalette.NightSurface
        shapes.rect(
            10f,
            88f,
            TutorialMap.WORLD_WIDTH - 20f,
            TutorialMap.WORLD_HEIGHT - 146f
        )
    }

    private fun drawDecorations() {
        TutorialMap.decorations.forEach { decoration ->
            shapes.color = when (decoration.type) {
                TutorialDecorationType.BUILDING -> TutorialPalette.BuildingBlue
                TutorialDecorationType.PARK -> TutorialPalette.ParkGreen
                TutorialDecorationType.CEMETERY -> TutorialPalette.CemeteryBlue
            }

            shapes.rect(
                decoration.x,
                decoration.y,
                decoration.width,
                decoration.height
            )
        }
    }

    private fun drawStreets() {
        shapes.color = TutorialPalette.RoadDark

        TutorialMap.streetSegments.forEach { street ->
            shapes.rectLine(
                street.startX,
                street.startY,
                street.endX,
                street.endY,
                34f
            )
        }

        shapes.color = TutorialPalette.RoadGray

        TutorialMap.streetSegments.forEach { street ->
            shapes.rectLine(
                street.startX,
                street.startY,
                street.endX,
                street.endY,
                22f
            )
        }
    }

    private fun drawDeliveryPoints() {
        TutorialMap.getDeliveryNodes().forEach { node ->
            val isActiveDestination =
                currentOrder.isInProgress &&
                        node.id == currentOrder.destinationNodeId

            shapes.color = if (isActiveDestination) {
                TutorialPalette.DeliveryTarget
            } else {
                TutorialPalette.BuildingLightBlue
            }

            shapes.rect(
                node.x - 20f,
                node.y - 20f,
                40f,
                40f
            )

            shapes.color = TutorialPalette.BuildingBlue
            shapes.rect(
                node.x - 14f,
                node.y - 14f,
                28f,
                28f
            )
        }
    }

    private fun drawRestaurant() {
        val restaurant = TutorialMap.getRestaurantNode()

        shapes.color = TutorialPalette.RestaurantRed
        shapes.rect(
            restaurant.x - 34f,
            restaurant.y - 28f,
            68f,
            56f
        )

        shapes.color = TutorialPalette.CheeseYellow
        shapes.rect(
            restaurant.x - 22f,
            restaurant.y + 4f,
            44f,
            10f
        )
    }

    private fun drawActiveTarget() {
        if (!currentOrder.isInProgress) return

        val targetNode = TutorialMap.getNode(currentOrder.destinationNodeId)

        shapes.begin(ShapeRenderer.ShapeType.Filled)

        shapes.color = TutorialPalette.DeliveryPointer

        val baseY = targetNode.y + 52f

        shapes.triangle(
            targetNode.x,
            targetNode.y + 24f,
            targetNode.x - 18f,
            baseY,
            targetNode.x + 18f,
            baseY
        )

        shapes.end()
    }

    private fun drawPlayer() {
        val playerNode = TutorialMap.getNode(playerNodeId)

        shapes.begin(ShapeRenderer.ShapeType.Filled)

        shapes.color = TutorialPalette.Player
        shapes.circle(
            playerNode.x,
            playerNode.y,
            15f
        )

        if (currentOrder.isInProgress) {
            shapes.color = TutorialPalette.OrderPurple
            shapes.rect(
                playerNode.x + 10f,
                playerNode.y + 8f,
                14f,
                14f
            )
        }

        shapes.end()
    }

    private fun drawControls() {
        shapes.begin(ShapeRenderer.ShapeType.Filled)

        drawButton(leftButton)
        drawButton(rightButton)
        drawButton(upButton)
        drawButton(downButton)
        drawButton(actionButton)
        drawButton(pauseButton)

        shapes.end()

        batch.begin()

        drawCenteredText("IZQ", leftButton, TutorialPalette.SmokeWhite, font)
        drawCenteredText("DER", rightButton, TutorialPalette.SmokeWhite, font)
        drawCenteredText("ARR", upButton, TutorialPalette.SmokeWhite, font)
        drawCenteredText("ABA", downButton, TutorialPalette.SmokeWhite, font)
        drawCenteredText("ACCION", actionButton, TutorialPalette.SmokeWhite, titleFont)
        drawCenteredText("II", pauseButton, TutorialPalette.SmokeWhite, titleFont)

        batch.end()
    }

    private fun drawButton(rectangle: Rectangle) {
        shapes.color = TutorialPalette.ActionPurple
        shapes.rect(
            rectangle.x,
            rectangle.y,
            rectangle.width,
            rectangle.height
        )

        shapes.color = TutorialPalette.PanelBorderBlue
        shapes.rectLine(
            rectangle.x,
            rectangle.y,
            rectangle.x + rectangle.width,
            rectangle.y,
            3f
        )
        shapes.rectLine(
            rectangle.x,
            rectangle.y + rectangle.height,
            rectangle.x + rectangle.width,
            rectangle.y + rectangle.height,
            3f
        )
        shapes.rectLine(
            rectangle.x,
            rectangle.y,
            rectangle.x,
            rectangle.y + rectangle.height,
            3f
        )
        shapes.rectLine(
            rectangle.x + rectangle.width,
            rectangle.y,
            rectangle.x + rectangle.width,
            rectangle.y + rectangle.height,
            3f
        )
    }

    private fun drawHud() {
        batch.begin()

        titleFont.color = TutorialPalette.SmokeWhite
        titleFont.draw(batch, "NightBite - Tutorial", 22f, 462f)

        font.color = TutorialPalette.SmokeWhite
        font.draw(
            batch,
            "Pedidos: $completedOrders/${TutorialGameResult.TOTAL_TUTORIAL_ORDERS}",
            22f,
            435f
        )

        font.draw(
            batch,
            "Puntaje: $score",
            190f,
            435f
        )

        font.draw(
            batch,
            "Pedido actual: ${currentOrder.number}/${TutorialGameResult.TOTAL_TUTORIAL_ORDERS}",
            325f,
            435f
        )

        val timeText = if (currentOrder.isInProgress) {
            "Tiempo: ${currentOrder.remainingTimeSeconds(gameTimeSeconds).toInt()}s"
        } else {
            "Tiempo: --"
        }

        font.draw(
            batch,
            timeText,
            570f,
            435f
        )

        font.color = TutorialPalette.LavenderGray
        font.draw(
            batch,
            message,
            22f,
            405f
        )

        batch.end()

        drawStars()
    }

    private fun drawStars() {
        val earnedStars = TutorialGameResult.calculateStars(
            completedOrders = completedOrders,
            totalOrders = TutorialGameResult.TOTAL_TUTORIAL_ORDERS
        )

        shapes.begin(ShapeRenderer.ShapeType.Filled)

        repeat(3) { index ->
            val centerX = 710f + (index * 28f)
            val centerY = 454f

            shapes.color = if (index < earnedStars) {
                TutorialPalette.CheeseYellow
            } else {
                TutorialPalette.RoadDark
            }

            drawStar(
                centerX = centerX,
                centerY = centerY,
                outerRadius = 12f,
                innerRadius = 5.5f
            )
        }

        shapes.end()
    }

    private fun drawStar(
        centerX: Float,
        centerY: Float,
        outerRadius: Float,
        innerRadius: Float
    ) {
        val points = mutableListOf<Pair<Float, Float>>()

        for (index in 0 until 10) {
            val angle = -PI / 2.0 + index * PI / 5.0
            val radius = if (index % 2 == 0) outerRadius else innerRadius

            points.add(
                Pair(
                    centerX + (cos(angle) * radius).toFloat(),
                    centerY + (sin(angle) * radius).toFloat()
                )
            )
        }

        for (index in points.indices) {
            val current = points[index]
            val next = points[(index + 1) % points.size]

            shapes.triangle(
                centerX,
                centerY,
                current.first,
                current.second,
                next.first,
                next.second
            )
        }
    }

    private fun drawPauseMenu() {
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(
            GL20.GL_SRC_ALPHA,
            GL20.GL_ONE_MINUS_SRC_ALPHA
        )

        shapes.begin(ShapeRenderer.ShapeType.Filled)

        shapes.color = com.badlogic.gdx.graphics.Color(0f, 0f, 0f, 0.65f)
        shapes.rect(
            0f,
            0f,
            TutorialMap.WORLD_WIDTH,
            TutorialMap.WORLD_HEIGHT
        )

        shapes.color = TutorialPalette.NightSurface
        shapes.rect(
            250f,
            105f,
            300f,
            260f
        )

        shapes.color = TutorialPalette.PanelBorderBlue
        shapes.rectLine(250f, 105f, 550f, 105f, 4f)
        shapes.rectLine(250f, 365f, 550f, 365f, 4f)
        shapes.rectLine(250f, 105f, 250f, 365f, 4f)
        shapes.rectLine(550f, 105f, 550f, 365f, 4f)

        drawButton(continueButton)
        drawButton(restartButton)
        drawButton(homeButton)

        shapes.end()

        batch.begin()

        titleFont.color = TutorialPalette.SmokeWhite
        glyphLayout.setText(titleFont, "Pausa")

        titleFont.draw(
            batch,
            glyphLayout,
            400f - glyphLayout.width / 2f,
            340f
        )

        font.color = TutorialPalette.LavenderGray
        glyphLayout.setText(font, "Decide como continuar la jornada")

        font.draw(
            batch,
            glyphLayout,
            400f - glyphLayout.width / 2f,
            315f
        )

        drawCenteredText(
            text = "CONTINUAR",
            rectangle = continueButton,
            color = TutorialPalette.SmokeWhite,
            textFont = titleFont
        )

        drawCenteredText(
            text = "REINICIAR NIVEL",
            rectangle = restartButton,
            color = TutorialPalette.SmokeWhite,
            textFont = titleFont
        )

        drawCenteredText(
            text = "VOLVER AL HOME",
            rectangle = homeButton,
            color = TutorialPalette.SmokeWhite,
            textFont = titleFont
        )

        batch.end()

        Gdx.gl.glDisable(GL20.GL_BLEND)
    }

    private fun drawCenteredText(
        text: String,
        rectangle: Rectangle,
        color: com.badlogic.gdx.graphics.Color,
        textFont: BitmapFont
    ) {
        textFont.color = color
        glyphLayout.setText(textFont, text)

        val x = rectangle.x + (rectangle.width - glyphLayout.width) / 2f
        val y = rectangle.y + (rectangle.height + glyphLayout.height) / 2f

        textFont.draw(
            batch,
            glyphLayout,
            x,
            y
        )
    }

    override fun dispose() {
        shapes.dispose()
        batch.dispose()
        font.dispose()
        titleFont.dispose()
    }
}