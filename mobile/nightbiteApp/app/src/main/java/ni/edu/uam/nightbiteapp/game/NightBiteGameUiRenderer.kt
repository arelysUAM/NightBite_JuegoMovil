package ni.edu.uam.nightbiteapp.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Renderizador de interfaz jugable de NightBite.
 *
 * Dibuja elementos reutilizables para el tutorial y futuros niveles:
 * - HUD superior
 * - Botones de movimiento
 * - Botón de acción
 * - Botón de pausa
 * - Estrellas
 * - Menú de pausa
 */
class NightBiteGameUiRenderer(
    private val shapes: ShapeRenderer,
    private val batch: SpriteBatch,
    private val font: BitmapFont,
    private val titleFont: BitmapFont,
    private val glyphLayout: GlyphLayout
) {

    fun drawControls(
        leftButton: Rectangle,
        rightButton: Rectangle,
        upButton: Rectangle,
        downButton: Rectangle,
        actionButton: Rectangle,
        pauseButton: Rectangle
    ) {
        shapes.begin(ShapeRenderer.ShapeType.Filled)

        drawButton(leftButton)
        drawButton(rightButton)
        drawButton(upButton)
        drawButton(downButton)

        drawCircleButton(actionButton)
        drawCircleButton(pauseButton)

        shapes.end()

        batch.begin()

        drawCenteredText("<", leftButton, TutorialPalette.SmokeWhite, titleFont)
        drawCenteredText(">", rightButton, TutorialPalette.SmokeWhite, titleFont)

        drawCenteredText("^", upButton, TutorialPalette.SmokeWhite, titleFont)
        drawCenteredText("A", actionButton, TutorialPalette.SmokeWhite, titleFont)
        drawCenteredText("v", downButton, TutorialPalette.SmokeWhite, titleFont)

        drawCenteredText("II", pauseButton, TutorialPalette.SmokeWhite, titleFont)

        batch.end()
    }

    fun drawHud(
        title: String,
        completedOrders: Int,
        totalOrders: Int,
        score: Int,
        currentOrder: TutorialOrder,
        gameTimeSeconds: Float,
        message: String
    ) {
        batch.begin()

        titleFont.color = TutorialPalette.SmokeWhite
        titleFont.draw(batch, title, 22f, 462f)

        font.color = TutorialPalette.SmokeWhite
        font.draw(
            batch,
            "Pedidos: $completedOrders/$totalOrders",
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
            "Pedido actual: ${currentOrder.number}/$totalOrders",
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

        drawStars(
            completedOrders = completedOrders,
            totalOrders = totalOrders
        )
    }

    fun drawPauseMenu(
        continueButton: Rectangle,
        restartButton: Rectangle,
        homeButton: Rectangle,
        worldWidth: Float = NightBiteCityMap.WORLD_WIDTH,
        worldHeight: Float = NightBiteCityMap.WORLD_HEIGHT
    ) {
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
            worldWidth,
            worldHeight
        )

        shapes.color = TutorialPalette.NightSurface
        shapes.rect(
            PAUSE_PANEL_X,
            PAUSE_PANEL_Y,
            PAUSE_PANEL_WIDTH,
            PAUSE_PANEL_HEIGHT
        )

        shapes.color = TutorialPalette.PanelBorderBlue
        shapes.rectLine(
            PAUSE_PANEL_X,
            PAUSE_PANEL_Y,
            PAUSE_PANEL_X + PAUSE_PANEL_WIDTH,
            PAUSE_PANEL_Y,
            4f
        )
        shapes.rectLine(
            PAUSE_PANEL_X,
            PAUSE_PANEL_Y + PAUSE_PANEL_HEIGHT,
            PAUSE_PANEL_X + PAUSE_PANEL_WIDTH,
            PAUSE_PANEL_Y + PAUSE_PANEL_HEIGHT,
            4f
        )
        shapes.rectLine(
            PAUSE_PANEL_X,
            PAUSE_PANEL_Y,
            PAUSE_PANEL_X,
            PAUSE_PANEL_Y + PAUSE_PANEL_HEIGHT,
            4f
        )
        shapes.rectLine(
            PAUSE_PANEL_X + PAUSE_PANEL_WIDTH,
            PAUSE_PANEL_Y,
            PAUSE_PANEL_X + PAUSE_PANEL_WIDTH,
            PAUSE_PANEL_Y + PAUSE_PANEL_HEIGHT,
            4f
        )

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
            worldWidth / 2f - glyphLayout.width / 2f,
            340f
        )

        font.color = TutorialPalette.LavenderGray
        glyphLayout.setText(font, "Decide como continuar la jornada")

        font.draw(
            batch,
            glyphLayout,
            worldWidth / 2f - glyphLayout.width / 2f,
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

    private fun drawCircleButton(rectangle: Rectangle) {
        val centerX = rectangle.x + rectangle.width / 2f
        val centerY = rectangle.y + rectangle.height / 2f
        val radius = minOf(rectangle.width, rectangle.height) / 2f

        shapes.color = TutorialPalette.ActionPurple
        shapes.circle(
            centerX,
            centerY,
            radius
        )

        shapes.color = TutorialPalette.PanelBorderBlue
        shapes.circle(
            centerX,
            centerY,
            radius
        )

        shapes.color = TutorialPalette.ActionPurple
        shapes.circle(
            centerX,
            centerY,
            radius - 4f
        )
    }

    private fun drawStars(
        completedOrders: Int,
        totalOrders: Int
    ) {
        val earnedStars = TutorialGameResult.calculateStars(
            completedOrders = completedOrders,
            totalOrders = totalOrders
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

    companion object {
        private const val PAUSE_PANEL_X = 250f
        private const val PAUSE_PANEL_Y = 105f
        private const val PAUSE_PANEL_WIDTH = 300f
        private const val PAUSE_PANEL_HEIGHT = 260f
    }
}