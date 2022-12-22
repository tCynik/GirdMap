package com.example.lessongirdrecycler.domain.coordinates_calculator

import com.example.lessongirdrecycler.domain.models.cell.Cell
import com.example.lessongirdrecycler.domain.models.CellNumber
import com.example.lessongirdrecycler.domain.models.local.LocalLocation
import com.example.lessongirdrecycler.models.Track
import com.example.lessongirdrecycler.models.TurnPoint

/**
 * логика такая:
 * Координаты, с которыми работаем: глобальные, кординаты карты, кординаты квадрата
 * 1. есть начало координат, относительно которого из лобальных координат получаем кординаты карты
 *
 */
class TracksGirdCalculator(
    val cellSize: Int // размер клетки в базовых единицах глобальной системы координат
    ) {

    fun splitTrackToCells(trackId: Int, track: Track): Cell {
        // каждой координате определяем ячейку, в которой точка, и координаты точки внутри ячейки
        // Если по дороге переход в другую ячейку, находим пограничные координаты для обеих ячеек
        // для каждой ячейки находим местный трек (GirdTrack)
        var i = 0
        while (i < track.turnPoints.size) {
            val turnPoint = track.turnPoints[i]
            val currentCellCoordinates = cellCoordinates(turnPoint)
            val coordinatesInsideCell = coordinatesInsideCell(turnPoint, currentCellCoordinates)

        }

        track.turnPoints.forEach{turnPoint -> {

        }}
    }

    private fun cellCoordinates(globalCoordinates: TurnPoint): CellNumber {
        val cellNumber = CellNumber(
            x = globalCoordinates.longitude/cellSize.toInt(),
            y = globalCoordinates.latitude/cellSize.toInt()
        )
        return coordinatesInsideCell(globalCoordinates, cellNumber)
    }

    private fun coordinatesInsideCell (globalCoordinates: TurnPoint, cellNumber: CellNumber): LocalLocation {
        return LocalLocation(
            x = globalCoordinates.longitude - (cellNumber.x*cellSize),
            y = globalCoordinates.latitude - (cellNumber.y*cellSize)
        )
    }

    private fun isCellsHasTransition(currentPosition: Int, turnPoints: List<TurnPoint>): Boolean {
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