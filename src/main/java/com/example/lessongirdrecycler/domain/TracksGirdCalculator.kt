package com.example.lessongirdrecycler.domain

import com.example.lessongirdrecycler.domain.models.Cell
import com.example.lessongirdrecycler.domain.models.CellNumber
import com.example.lessongirdrecycler.domain.models.GirdTurnPoint
import com.example.lessongirdrecycler.models.CellTracks
import com.example.lessongirdrecycler.models.Track
import com.example.lessongirdrecycler.models.TurnPoint

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

    private fun coordinatesInsideCell (globalCoordinates: TurnPoint, cellNumber: CellNumber): GirdTurnPoint {
        return GirdTurnPoint(
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
            if (cellCoordinates(turnPoints[currentPosition].equals(cellCoordinates(turnPoints[currentPosition+1]))))
        } else return false
    }
}