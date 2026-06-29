package ni.edu.uam.nightbiteapp.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import kotlin.math.abs

/**
 * Renderizador visual del mapa base de NightBite.
 *
 * Dibuja la ciudad desde la cuadrícula definida en NightBiteCityMap:
 * - x: calles transitables
 * - z: zona de recolección
 * - R: restaurante
 * - V: ventanilla de recolección
 * - Q: edificio de entrega
 * - E: punto de entrega
 */
open class NightBiteCityMapRenderer(
    private val shapes: ShapeRenderer,
    private val cityMap: NightBiteMap = NightBiteCityMap
) {

    fun drawMap(
        currentOrder: TutorialOrder
    ) {
        val activeDestinationCell = getActiveDestinationCell(currentOrder)

        enableBlend()

        shapes.begin(ShapeRenderer.ShapeType.Filled)

        drawMapBackground()
        drawRoadCells()
        drawRoadCurbs()
        drawRoadMarks()

        val shouldHighlightPickup =
            currentOrder.isWaitingAtRestaurant || currentOrder.isExpired

        drawPickupZone(shouldHighlightPickup)
        drawDeliveryBuildings(activeDestinationCell)
        drawRestaurantBuilding()
        drawPickupWindows(shouldHighlightPickup)
        drawDeliveryTargetCells(activeDestinationCell)

        shapes.end()

        disableBlend()
    }

    fun drawActiveTarget(
        currentOrder: TutorialOrder
    ) {
        if (!currentOrder.isInProgress) return

        val targetNode = cityMap.getNode(currentOrder.destinationNodeId)

        enableBlend()

        shapes.begin(ShapeRenderer.ShapeType.Filled)

        shapes.color = withAlpha(
            color = TutorialPalette.DeliveryPointer,
            alpha = 0.24f
        )

        shapes.circle(
            targetNode.x,
            targetNode.y,
            32f
        )

        shapes.color = TutorialPalette.DeliveryPointer

        val baseY = targetNode.y + 54f

        shapes.triangle(
            targetNode.x,
            targetNode.y + 24f,
            targetNode.x - 17f,
            baseY,
            targetNode.x + 17f,
            baseY
        )

        shapes.color = TutorialPalette.CheeseYellow

        shapes.circle(
            targetNode.x,
            targetNode.y + 44f,
            5f
        )

        shapes.end()

        disableBlend()
    }

    private fun drawMapBackground() {
        shapes.color = TutorialPalette.NightBackground

        shapes.rect(
            0f,
            0f,
            NightBiteCityMap.WORLD_WIDTH,
            NightBiteCityMap.WORLD_HEIGHT
        )

        shapes.color = TutorialPalette.NightSurface

        shapes.rect(
            NightBiteCityMap.MAP_LEFT,
            NightBiteCityMap.MAP_BOTTOM,
            NightBiteCityMap.mapWidth,
            NightBiteCityMap.mapHeight
        )

        shapes.color = TutorialPalette.PanelBorderBlue

        shapes.rectLine(
            NightBiteCityMap.MAP_LEFT,
            NightBiteCityMap.MAP_BOTTOM,
            NightBiteCityMap.MAP_LEFT + NightBiteCityMap.mapWidth,
            NightBiteCityMap.MAP_BOTTOM,
            3f
        )

        shapes.rectLine(
            NightBiteCityMap.MAP_LEFT,
            NightBiteCityMap.MAP_BOTTOM + NightBiteCityMap.mapHeight,
            NightBiteCityMap.MAP_LEFT + NightBiteCityMap.mapWidth,
            NightBiteCityMap.MAP_BOTTOM + NightBiteCityMap.mapHeight,
            3f
        )

        shapes.rectLine(
            NightBiteCityMap.MAP_LEFT,
            NightBiteCityMap.MAP_BOTTOM,
            NightBiteCityMap.MAP_LEFT,
            NightBiteCityMap.MAP_BOTTOM + NightBiteCityMap.mapHeight,
            3f
        )

        shapes.rectLine(
            NightBiteCityMap.MAP_LEFT + NightBiteCityMap.mapWidth,
            NightBiteCityMap.MAP_BOTTOM,
            NightBiteCityMap.MAP_LEFT + NightBiteCityMap.mapWidth,
            NightBiteCityMap.MAP_BOTTOM + NightBiteCityMap.mapHeight,
            3f
        )
    }

    private fun drawRoadCells() {
        repeat(NightBiteCityMap.GRID_ROWS) { row ->
            repeat(NightBiteCityMap.GRID_COLUMNS) { column ->
                val symbol = NightBiteCityMap.getRouteSymbol(
                    row = row,
                    column = column
                )

                if (symbol == 'x' || symbol == 'z') {
                    val x = cellLeft(column)
                    val y = cellBottom(row)

                    shapes.color = if (symbol == 'z') {
                        withAlpha(
                            color = TutorialPalette.ActionPurple,
                            alpha = 0.75f
                        )
                    } else {
                        TutorialPalette.RoadDark
                    }

                    shapes.rect(
                        x,
                        y,
                        NightBiteCityMap.CELL_SIZE,
                        NightBiteCityMap.CELL_SIZE
                    )

                    shapes.color = if (symbol == 'z') {
                        withAlpha(
                            color = TutorialPalette.OrderPurple,
                            alpha = 0.45f
                        )
                    } else {
                        TutorialPalette.RoadGray
                    }

                    shapes.rect(
                        x,
                        y,
                        NightBiteCityMap.CELL_SIZE,
                        NightBiteCityMap.CELL_SIZE
                    )
                }
            }
        }
    }

    private fun drawRoadCurbs() {
        shapes.color = withAlpha(
            color = TutorialPalette.SmokeWhite,
            alpha = 0.55f
        )

        repeat(NightBiteCityMap.GRID_ROWS) { row ->
            repeat(NightBiteCityMap.GRID_COLUMNS) { column ->
                if (NightBiteCityMap.isWalkableCell(row, column)) {
                    drawCurbsForCell(
                        row = row,
                        column = column
                    )
                }
            }
        }
    }

    private fun drawCurbsForCell(
        row: Int,
        column: Int
    ) {
        val x = cellLeft(column)
        val y = cellBottom(row)
        val size = NightBiteCityMap.CELL_SIZE

        if (!NightBiteCityMap.isWalkableCell(row - 1, column)) {
            shapes.rect(
                x,
                y + size - 2f,
                size,
                2f
            )
        }

        if (!NightBiteCityMap.isWalkableCell(row + 1, column)) {
            shapes.rect(
                x,
                y,
                size,
                2f
            )
        }

        if (!NightBiteCityMap.isWalkableCell(row, column - 1)) {
            shapes.rect(
                x,
                y,
                2f,
                size
            )
        }

        if (!NightBiteCityMap.isWalkableCell(row, column + 1)) {
            shapes.rect(
                x + size - 2f,
                y,
                2f,
                size
            )
        }
    }

    private fun drawRoadMarks() {
        shapes.color = withAlpha(
            color = TutorialPalette.SmokeWhite,
            alpha = 0.42f
        )

        drawHorizontalCenterLines()
        drawVerticalCenterLines()
    }

    private fun drawHorizontalCenterLines() {
        repeat(NightBiteCityMap.GRID_ROWS - 1) { row ->
            repeat(NightBiteCityMap.GRID_COLUMNS) { column ->
                val topWalkable = NightBiteCityMap.isWalkableCell(row, column)
                val bottomWalkable = NightBiteCityMap.isWalkableCell(row + 1, column)

                if (!topWalkable || !bottomWalkable) return@repeat

                val hasLeft =
                    (column > 0) &&
                            NightBiteCityMap.isWalkableCell(row, column - 1) &&
                            NightBiteCityMap.isWalkableCell(row + 1, column - 1)

                val hasRight =
                    (column < NightBiteCityMap.GRID_COLUMNS - 1) &&
                            NightBiteCityMap.isWalkableCell(row, column + 1) &&
                            NightBiteCityMap.isWalkableCell(row + 1, column + 1)

                if (!hasLeft && !hasRight) return@repeat

                if (column % 2 != 0) return@repeat

                val x = cellLeft(column)
                val y = cellBottom(row)

                val centerY = y

                shapes.rect(
                    x + 4f,
                    centerY - 1f,
                    NightBiteCityMap.CELL_SIZE - 8f,
                    2f
                )
            }
        }
    }

    private fun drawVerticalCenterLines() {
        repeat(NightBiteCityMap.GRID_ROWS) { row ->
            repeat(NightBiteCityMap.GRID_COLUMNS - 1) { column ->
                val leftWalkable = NightBiteCityMap.isWalkableCell(row, column)
                val rightWalkable = NightBiteCityMap.isWalkableCell(row, column + 1)

                if (!leftWalkable || !rightWalkable) return@repeat

                val hasUp =
                    (row > 0) &&
                            NightBiteCityMap.isWalkableCell(row - 1, column) &&
                            NightBiteCityMap.isWalkableCell(row - 1, column + 1)

                val hasDown =
                    (row < NightBiteCityMap.GRID_ROWS - 1) &&
                            NightBiteCityMap.isWalkableCell(row + 1, column) &&
                            NightBiteCityMap.isWalkableCell(row + 1, column + 1)

                if (!hasUp && !hasDown) return@repeat

                if (row % 2 != 0) return@repeat

                val x = cellLeft(column)
                val y = cellBottom(row)

                val centerX = x + NightBiteCityMap.CELL_SIZE

                shapes.rect(
                    centerX - 1f,
                    y + 4f,
                    2f,
                    NightBiteCityMap.CELL_SIZE - 8f
                )
            }
        }
    }

    private fun drawRoadMarkForCell(
        row: Int,
        column: Int
    ) {
        val hasLeft = NightBiteCityMap.isWalkableCell(row, column - 1)
        val hasRight = NightBiteCityMap.isWalkableCell(row, column + 1)
        val hasUp = NightBiteCityMap.isWalkableCell(row - 1, column)
        val hasDown = NightBiteCityMap.isWalkableCell(row + 1, column)

        val x = cellLeft(column)
        val y = cellBottom(row)
        val centerX = x + NightBiteCityMap.CELL_SIZE / 2f
        val centerY = y + NightBiteCityMap.CELL_SIZE / 2f

        if (hasLeft && hasRight && column % 2 == 0) {
            shapes.rect(
                x + 5f,
                centerY - 1f,
                10f,
                2f
            )
        }

        if (hasUp && hasDown && row % 2 == 0) {
            shapes.rect(
                centerX - 1f,
                y + 5f,
                2f,
                10f
            )
        }
    }

    private fun drawPickupZone(
        shouldHighlightPickup: Boolean
    ) {
        repeat(NightBiteCityMap.GRID_ROWS) { row ->
            repeat(NightBiteCityMap.GRID_COLUMNS) { column ->
                if (NightBiteCityMap.isPickupCell(row, column)) {
                    val x = cellLeft(column)
                    val y = cellBottom(row)

                    shapes.color = if (shouldHighlightPickup) {
                        withAlpha(
                            color = TutorialPalette.OrderPurple,
                            alpha = 0.45f
                        )
                    } else {
                        withAlpha(
                            color = TutorialPalette.ActionPurple,
                            alpha = 0.18f
                        )
                    }

                    shapes.rect(
                        x + 3f,
                        y + 3f,
                        NightBiteCityMap.CELL_SIZE - 6f,
                        NightBiteCityMap.CELL_SIZE - 6f
                    )

                    if (shouldHighlightPickup) {
                        shapes.color = withAlpha(
                            color = TutorialPalette.CheeseYellow,
                            alpha = 0.45f
                        )

                        shapes.circle(
                            x + NightBiteCityMap.CELL_SIZE / 2f,
                            y + NightBiteCityMap.CELL_SIZE / 2f,
                            3.5f
                        )
                    }

                    shapes.circle(
                        x + NightBiteCityMap.CELL_SIZE / 2f,
                        y + NightBiteCityMap.CELL_SIZE / 2f,
                        3.5f
                    )
                }
            }
        }
    }

    private fun drawDeliveryBuildings(
        activeDestinationCell: Pair<Int, Int>?
    ) {
        drawObjectShadow(symbol = 'Q')

        repeat(NightBiteCityMap.GRID_ROWS) { row ->
            repeat(NightBiteCityMap.GRID_COLUMNS) { column ->
                if (NightBiteCityMap.isDeliveryBuildingCell(row, column)) {
                    val isActiveBuilding = isNearActiveDestination(
                        row = row,
                        column = column,
                        activeDestinationCell = activeDestinationCell,
                        maxDistance = 4
                    )

                    drawDeliveryBuildingCell(
                        row = row,
                        column = column,
                        isActiveBuilding = isActiveBuilding
                    )
                }
            }
        }

        drawObjectOutline(
            symbol = 'Q',
            outlineColor = TutorialPalette.PanelBorderBlue
        )
    }

    private fun drawDeliveryBuildingCell(
        row: Int,
        column: Int,
        isActiveBuilding: Boolean
    ) {
        val x = cellLeft(column)
        val y = cellBottom(row)

        shapes.color = if (isActiveBuilding) {
            TutorialPalette.DeliveryTarget
        } else {
            TutorialPalette.BuildingBlue
        }

        shapes.rect(
            x,
            y,
            NightBiteCityMap.CELL_SIZE,
            NightBiteCityMap.CELL_SIZE
        )
    }

    private fun drawRestaurantBuilding() {
        repeat(NightBiteCityMap.GRID_ROWS) { row ->
            repeat(NightBiteCityMap.GRID_COLUMNS) { column ->
                if (NightBiteCityMap.isRestaurantBuildingCell(row, column)) {
                    val x = cellLeft(column)
                    val y = cellBottom(row)

                    shapes.color = TutorialPalette.RestaurantRed

                    shapes.rect(
                        x,
                        y,
                        NightBiteCityMap.CELL_SIZE,
                        NightBiteCityMap.CELL_SIZE
                    )
                }
            }
        }
    }

    private fun drawPickupWindows(
        shouldHighlightPickup: Boolean
    ) {
        if (!shouldHighlightPickup) return

        repeat(NightBiteCityMap.GRID_ROWS) { row ->
            repeat(NightBiteCityMap.GRID_COLUMNS) { column ->
                if (NightBiteCityMap.isPickupWindowCell(row, column)) {
                    val x = cellLeft(column)
                    val y = cellBottom(row)

                    shapes.color = withAlpha(
                        color = TutorialPalette.CheeseYellow,
                        alpha = 0.35f
                    )

                    shapes.circle(
                        x + NightBiteCityMap.CELL_SIZE / 2f,
                        y + NightBiteCityMap.CELL_SIZE / 2f,
                        13f
                    )

                    shapes.color = TutorialPalette.CheeseYellow

                    shapes.rect(
                        x,
                        y,
                        NightBiteCityMap.CELL_SIZE,
                        NightBiteCityMap.CELL_SIZE
                    )
                }
            }
        }
    }

    private fun drawDeliveryTargetCells(
        activeDestinationCell: Pair<Int, Int>?
    ) {
        if (activeDestinationCell == null) return

        repeat(NightBiteCityMap.GRID_ROWS) { row ->
            repeat(NightBiteCityMap.GRID_COLUMNS) { column ->
                if (NightBiteCityMap.isDeliveryTargetCell(row, column)) {
                    val isActiveTarget = isNearActiveDestination(
                        row = row,
                        column = column,
                        activeDestinationCell = activeDestinationCell,
                        maxDistance = 1
                    )

                    if (isActiveTarget) {
                        drawDeliveryTargetCell(
                            row = row,
                            column = column,
                            isActiveTarget = true
                        )
                    }
                }
            }
        }
    }
    private fun drawDeliveryTargetCell(
        row: Int,
        column: Int,
        isActiveTarget: Boolean
    ) {
        if (!isActiveTarget) return

        val x = cellLeft(column)
        val y = cellBottom(row)

        shapes.color = withAlpha(
            color = TutorialPalette.DeliveryTarget,
            alpha = 0.35f
        )

        shapes.circle(
            x + NightBiteCityMap.CELL_SIZE / 2f,
            y + NightBiteCityMap.CELL_SIZE / 2f,
            17f
        )

        shapes.color = TutorialPalette.CheeseYellow

        shapes.rect(
            x,
            y,
            NightBiteCityMap.CELL_SIZE,
            NightBiteCityMap.CELL_SIZE
        )
    }

    private fun drawObjectShadow(
        symbol: Char
    ) {
        shapes.color = withAlpha(
            color = TutorialPalette.NightBackground,
            alpha = 0.45f
        )

        repeat(NightBiteCityMap.GRID_ROWS) { row ->
            repeat(NightBiteCityMap.GRID_COLUMNS) { column ->
                if (NightBiteCityMap.getObjectSymbol(row, column) == symbol) {
                    shapes.rect(
                        cellLeft(column) + 3f,
                        cellBottom(row) - 3f,
                        NightBiteCityMap.CELL_SIZE,
                        NightBiteCityMap.CELL_SIZE
                    )
                }
            }
        }
    }

    private fun drawObjectOutline(
        symbol: Char,
        outlineColor: Color
    ) {
        shapes.color = outlineColor

        repeat(NightBiteCityMap.GRID_ROWS) { row ->
            repeat(NightBiteCityMap.GRID_COLUMNS) { column ->
                if (NightBiteCityMap.getObjectSymbol(row, column) == symbol) {
                    drawOutlineForObjectCell(
                        row = row,
                        column = column,
                        symbol = symbol
                    )
                }
            }
        }
    }

    private fun drawOutlineForObjectCell(
        row: Int,
        column: Int,
        symbol: Char
    ) {
        val x = cellLeft(column)
        val y = cellBottom(row)
        val size = NightBiteCityMap.CELL_SIZE

        if (NightBiteCityMap.getObjectSymbol(row - 1, column) != symbol) {
            shapes.rect(
                x,
                y + size - 2f,
                size,
                2f
            )
        }

        if (NightBiteCityMap.getObjectSymbol(row + 1, column) != symbol) {
            shapes.rect(
                x,
                y,
                size,
                2f
            )
        }

        if (NightBiteCityMap.getObjectSymbol(row, column - 1) != symbol) {
            shapes.rect(
                x,
                y,
                2f,
                size
            )
        }

        if (NightBiteCityMap.getObjectSymbol(row, column + 1) != symbol) {
            shapes.rect(
                x + size - 2f,
                y,
                2f,
                size
            )
        }
    }

    private fun getActiveDestinationCell(
        currentOrder: TutorialOrder
    ): Pair<Int, Int>? {
        if (!currentOrder.isInProgress) return null

        val targetNode = cityMap.getNode(currentOrder.destinationNodeId)

        return cellForPosition(
            x = targetNode.x,
            y = targetNode.y
        )
    }

    private fun cellForPosition(
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

    private fun isNearActiveDestination(
        row: Int,
        column: Int,
        activeDestinationCell: Pair<Int, Int>?,
        maxDistance: Int
    ): Boolean {
        if (activeDestinationCell == null) return false

        val activeRow = activeDestinationCell.first
        val activeColumn = activeDestinationCell.second

        return abs(row - activeRow) + abs(column - activeColumn) <= maxDistance
    }

    private fun cellLeft(column: Int): Float {
        return NightBiteCityMap.MAP_LEFT + (column * NightBiteCityMap.CELL_SIZE)
    }

    private fun cellBottom(row: Int): Float {
        return NightBiteCityMap.MAP_BOTTOM +
                ((NightBiteCityMap.GRID_ROWS - row - 1) * NightBiteCityMap.CELL_SIZE)
    }

    private fun withAlpha(
        color: Color,
        alpha: Float
    ): Color {
        return Color(
            color.r,
            color.g,
            color.b,
            alpha
        )
    }

    private fun enableBlend() {
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(
            GL20.GL_SRC_ALPHA,
            GL20.GL_ONE_MINUS_SRC_ALPHA
        )
    }

    private fun disableBlend() {
        Gdx.gl.glDisable(GL20.GL_BLEND)
    }
}