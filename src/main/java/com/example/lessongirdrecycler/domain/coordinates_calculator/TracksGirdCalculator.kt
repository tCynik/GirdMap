package com.example.lessongirdrecycler.domain.coordinates_calculator

import com.example.lessongirdrecycler.domain.models.cell.SplittedByCellsTrack
import com.example.lessongirdrecycler.domain.models.cell.CellCoordinates
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
        val splittedByCellsTrack = SplittedByCellsTrack(mutableMapOf<CellCoordinates, CellTrack>())
        val firstCellTrack = CellTrack(trackId, mutableListOf())
        var currentCell = cellCoordinates(track.turnPoints[0])

        val firstTurnPoint: CellLocation = coordinatesInsideCell(
            globalCoordinates = track.turnPoints[0], cellCoordinates = currentCell)
        firstCellTrack.turnPoints.add(firstTurnPoint)
        splittedByCellsTrack[currentCell] = firstCellTrack

        var i = 1
        while (i < track.turnPoints.size) {
            var currentTurnPoint: CellLocation
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

    private fun cellCoordinates(globalCoordinates: GlobalTurnPoint): CellCoordinates {
        val cellCoordinates = CellCoordinates(
            x = globalCoordinates.longitude/cellSize.toInt(),
            y = globalCoordinates.latitude/cellSize.toInt()
        )
        return cellCoordinates
    }

    private fun coordinatesInsideCell (globalCoordinates: GlobalTurnPoint, cellCoordinates: CellCoordinates): CellLocation {
        return CellLocation(
            x = globalCoordinates.longitude - (cellCoordinates.x*cellSize),
            y = globalCoordinates.latitude - (cellCoordinates.y*cellSize)
        )
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