package com.example.lessongirdrecycler.domain.coordinates_calculator

import com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition.TransitionManager
import com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition.TransitionTo
import com.example.lessongirdrecycler.domain.models.cell.SplittedByCellsTrack
import com.example.lessongirdrecycler.domain.models.cell.CoordinatesOfCell
import com.example.lessongirdrecycler.domain.models.cell.CellTrack
import com.example.lessongirdrecycler.domain.models.cell.CellLocation
import com.example.lessongirdrecycler.models.Track
import com.example.lessongirdrecycler.domain.models.global.GlobalTurnPoint

/**
 * логика такая:
 * Координаты, с которыми работаем: глобальные, кординаты карты, кординаты квадрата
 * 1. есть начало координат, относительно которого из лобальных координат получаем кординаты карты
 *
 */
class TracksGirdCalculator(
    val girdStartX: Double,
    val girdStartY: Double,
    val cellSize: Int // размер клетки в базовых единицах глобальной системы координат
    ) {

    fun splitTrackToCells(trackId: Int, track: Track): SplittedByCellsTrack {
        // каждой координате определяем ячейку, в которой точка, и координаты точки внутри ячейки
        // Если по дороге переход в другую ячейку, находим пограничные координаты для обеих ячеек
        // для каждой ячейки находим местный трек (GirdTrack)
        val splittedByCellsTrack = SplittedByCellsTrack(trackId)
        var currentCell = cellCoordinates(track.turnPoints[0])

        val currentTurnPoint: CellLocation = coordinatesInsideCell(
            globalCoordinates = track.turnPoints[0], coordinatesOfCell = currentCell)
        splittedByCellsTrack.addLocation(coordinatesOfCell = currentCell, cellLocation = currentTurnPoint)

        val transitionManager = TransitionManager(cellSize)

        var i = 1
        while (i < track.turnPoints.size) {
            val relativeNextCoordinates: CellLocation = getRelativeForNext(
                nextTurnPoint = track.turnPoints[i],
                currentTurnPoint = track.turnPoints[i-1])

            var nextTurnPoint: CellLocation
            // todo: need 2 provide: 1) next turnpoint in current cell,
            //  2) next cell (if it is),
            //  3) turnpoint in the next cell (if it is)
            val transition = transitionManager.getTransition(
                coordinatesInCell = currentTurnPoint,
                relativeCoordinatesNext = relativeNextCoordinates)

            when (transition) {
                TransitionTo.NORTH -> {
                    nextTurnPoint = CellLocation(
                        x = ,
                        y = 0)
                }
                TransitionTo.NE -> {
                    nextTurnPoint = CellLocation( x = cellSize, y = 0)
                }
                TransitionTo.EAST -> {}
                TransitionTo.SE -> {
                    nextTurnPoint = CellLocation(x = cellSize, y = cellSize)
                }
                TransitionTo.SOUTH -> {}
                TransitionTo.SW -> {
                    nextTurnPoint = CellLocation(x = 0, y = cellSize)
                }
                TransitionTo.WEST -> {}
                TransitionTo.NW -> {
                    nextTurnPoint = CellLocation(x = 0, y = 0)}
                TransitionTo.NONE -> {
                    nextTurnPoint = CellLocation( // todo: go one to the next point
                        x = currentTurnPoint.x + relativeNextCoordinates.x,
                        y = currentTurnPoint.y + relativeNextCoordinates.y)
                }
            }


            val nextCell = cellCoordinates(track.turnPoints[i])
            if (nextCell != currentCell) {
                currentCell =

            }
            else {
                splittedByCellsTrack[currentCell].addLocation()
            }

            var cellTrack = CellTrack(id = track.id, turnPoints = mutableListOf())
            val turnPoint = track.turnPoints[i]
            val globalPoint = track.turnPoints[i]
            val currentCellCoordinates = cellCoordinates(turnPoint)
            val coordinatesInsideCell = coordinatesInsideCell(turnPoint, currentCellCoordinates)
            i++
        }

        track.turnPoints.forEach{turnPoint -> {

        }}

        return SplittedByCellsTrack(cellData = splittedByCellsTrack)
    }

    private fun cellCoordinates(globalCoordinates: GlobalTurnPoint): CoordinatesOfCell {
        val coordinatesOfCell = CoordinatesOfCell(
            // todo: тут будет необходимо учесть, что мы берем координаты ячеек из глобальных координат
            // брать из местных?
            x = globalCoordinates.longitude/cellSize.toInt(),
            y = globalCoordinates.latitude/cellSize.toInt()
        )
        return coordinatesOfCell
    }

    private fun coordinatesInsideCell (globalCoordinates: GlobalTurnPoint, coordinatesOfCell: CoordinatesOfCell): CellLocation {
        return CellLocation(
            x = globalCoordinates.longitude - (coordinatesOfCell.x*cellSize),
            y = globalCoordinates.latitude - (coordinatesOfCell.y*cellSize)
        )
    }

    private fun getRelativeForNext(
        nextTurnPoint: GlobalTurnPoint,
        currentTurnPoint: GlobalTurnPoint
        ): CellLocation {
        return CellLocation(
            x = nextTurnPoint.longitude - currentTurnPoint.longitude,
            y = nextTurnPoint.latitude - currentTurnPoint.latitude )
    }

    private fun isCellsHasTransition(currentPosition: Int, turnPoints: List<GlobalTurnPoint>): Boolean {
        if (currentPosition < turnPoints.size) {
            val currentCellCoordinates = cellCoordinates(turnPoints[currentPosition])
            val nextCellCoordinates = cellCoordinates(turnPoints[currentPosition])
            val differentX = currentCellCoordinates.x - nextCellCoordinates.x
            val differentY = currentCellCoordinates.y - nextCellCoordinates.y
            // далее определяем тип перехода: вверх-вниз-вправо-влево
            if (cellCoordinates(turnPoints[currentPosition].
                equals(cellCoordinates(turnPoints[currentPosition+1])
        } else return false
    }
}