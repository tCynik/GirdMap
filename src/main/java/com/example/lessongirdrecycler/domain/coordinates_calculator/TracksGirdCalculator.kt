package com.example.lessongirdrecycler.domain.coordinates_calculator

import com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition.NextCellNumber
import com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition.TransitionManager
import com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition.TransitionTo
import com.example.lessongirdrecycler.domain.models.cell.SplittedByCellsTrack
import com.example.lessongirdrecycler.domain.models.cell.CoordinatesOfCell
import com.example.lessongirdrecycler.domain.models.cell.CellLocation
import com.example.lessongirdrecycler.domain.models.global.GlobalTrack
import com.example.lessongirdrecycler.domain.models.global.GlobalTurnPoint
import com.example.lessongirdrecycler.domain.models.segment.CellTransitionByEnum
import com.example.lessongirdrecycler.domain.models.segment.Segment

/**
 * Растаскивание трека глобальных точек на серию треков в рамках отдельных ячеек координатной сетки
 * (т.е. получаем набор мини-треков с отметкой - к какоя ячейке они относятся)
 * логика такая:
 * 1. есть начало координат, относительно которого из лобальных координат получаем кординаты карты
 * 2.
 */
class TracksGirdCalculator(
    val girdStartX: Double,
    val girdStartY: Double,
    val cellSize: Int // размер клетки в базовых единицах глобальной системы координат
    ) {

    fun splitTrackToCells(track: GlobalTrack): SplittedByCellsTrack {
        // каждой координате определяем ячейку, в которой точка, и координаты точки внутри ячейки
        // Если по дороге переход в другую ячейку, находим пограничные координаты для обеих ячеек
        // для каждой ячейки находим местный трек (GirdTrack)
        val splittedByCellsTrack = SplittedByCellsTrack()
        var currentCell = cellCoordinates(track.turnPoints[0])

        val firstTurnPoint: CellLocation = coordinatesInsideCell(
            globalCoordinates = track.turnPoints[0], coordinatesOfCell = currentCell)
        splittedByCellsTrack.addLocation(coordinatesOfCell = currentCell, cellLocation = firstTurnPoint)

        var i = 1
        while (i < track.turnPoints.size) {
            val segmentStartCell = cellCoordinates(track.turnPoints[i-1])
            val segmentEndCell = cellCoordinates(track.turnPoints[i])

            var currentSegment = Segment(
                cellSize = cellSize,
                startCellLocation = coordinatesInsideCell(track.turnPoints[i-1], segmentStartCell),
                endSegmentCellLocation = coordinatesInsideCell(track.turnPoints[i], segmentStartCell))

            if (segmentStartCell.equals(segmentEndCell)) { // если конец сегмента в этой же ячейке
                splittedByCellsTrack.addLocation(coordinatesOfCell = segmentStartCell, cellLocation = currentSegment.endSegmentCellLocation)
            } else { // либо если сегмент придется разбивать
                var transitionTo = TransitionManager(cellSize).getTransitionTo(
                    currentSegment.startCellLocation,
                    currentSegment.endSegmentCellLocation)
                var nextCellNumber = NextCellNumber().get(
                    cellNumber = segmentStartCell,
                    transitionTo = transitionTo)

                while (transitionTo != TransitionTo.NONE) {
                    transitionTo = TransitionManager(cellSize).getTransitionTo( // преверяем на наличие перехода
                        currentSegment.startCellLocation,
                        currentSegment.endSegmentCellLocation)

                    nextCellNumber = NextCellNumber().get(
                        cellNumber = segmentStartCell,
                        transitionTo = transitionTo)
                    val transitionPoints = CellTransitionByEnum(cellSize).getTransitionPoints(
                        transitionTo = transitionTo,
                        startCellLocation = currentSegment.startCellLocation,
                        endCellLocation = currentSegment.endSegmentCellLocation)

                    splittedByCellsTrack.addLocation(coordinatesOfCell = segmentStartCell, cellLocation = transitionPoints[0])
                    splittedByCellsTrack.addLocation(coordinatesOfCell = nextCellNumber, cellLocation = transitionPoints[1])

                    currentSegment.cutOne(transitionPoints[0], transitionTo) // отрезаем сегмент
                }
                splittedByCellsTrack.addLocation(coordinatesOfCell = nextCellNumber, cellLocation = currentSegment.endSegmentLocation)
            }
            i++
        }
        return splittedByCellsTrack
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
}