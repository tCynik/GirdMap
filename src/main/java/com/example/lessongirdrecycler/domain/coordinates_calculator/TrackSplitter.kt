package com.example.lessongirdrecycler.domain.coordinates_calculator

import android.util.Log
import com.example.lessongirdrecycler.domain.Logger
import com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition.NextCellNumber
import com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition.TransitionManager
import com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition.TransitionTo
import com.example.lessongirdrecycler.domain.models.cell.CellLocation
import com.example.lessongirdrecycler.domain.models.cell.CoordinatesOfCell
import com.example.lessongirdrecycler.domain.models.cell.SplittedByCellsTrack
import com.example.lessongirdrecycler.domain.models.cell.TrackPartInSingleCell
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

class TrackSplitter (
    val girdStartX: Double,
    val girdStartY: Double,
    val coordinateScale: Int,
    val cellSize: Int, // размер клетки в базовых единицах глобальной системы координат
    val logger: Logger
    ){
    val transitionManager = TransitionManager(cellSize)

    fun splitGlobalTrackToCells(track: GlobalTrack): SplittedByCellsTrack {
        // каждой координате определяем ячейку, в которой точка, и координаты точки внутри ячейки
        // Если по дороге переход в другую ячейку, находим пограничные координаты для обеих ячеек
        // для каждой ячейки находим местный трек (GirdTrack)
        var currentCell = cellCoordinates(track.turnPoints[0])

        val turnPoints = track.turnPoints
        turnPoints.forEach{point -> point.print(point)}
        var listForResult = mutableListOf<TrackPartInSingleCell>()
        logger.printLog("starting to split the track, size of track = ${turnPoints.size}")

        var iter = 1
        while (iter < turnPoints.size) { // перебор точек трека
            val currentPoint = turnPoints[iter -1]
            val nextPoint = turnPoints[iter]
            currentCell = cellCoordinates(currentPoint)
            val currentSegment = Segment(
                cellSize = cellSize,
                startCellLocation = coordinatesInsideCellByGlobal(currentPoint, currentCell),
                endSegmentCellLocation = coordinatesInsideCellByGlobal(nextPoint, currentCell))//makeBigSegment(currentPoint, nextPoint)
            logger.printLog("next global point = $nextPoint, local = ${coordinatesInsideCellByGlobal(nextPoint, currentCell)}, segment = $currentSegment")
            logger.printLog("spliting iter = $iter, cell = $currentCell currentPoint = $currentPoint, next point = $nextPoint, making the segment= ${currentSegment} ")

            listForResult = splitSegment(
                startCell = currentCell,
                splittingSegment = currentSegment,
                listForResult = listForResult)
            //resultSplitted = distributeSegments(resultSplitted, segments)
            iter++
        }
        logger.printLog("splitting ended, splitted size = ${listForResult.size}")

        listForResult.forEach { cellTrackPart ->
            logger.printLog("splitting Segment. cell = ${cellTrackPart.cell}, segment coordinates = ${cellTrackPart.track[0]}-${cellTrackPart.track[1]}") }

        var resultSplitted = SplittedByCellsTrack()
        resultSplitted.cellData = listForResult
        return resultSplitted
    }

    private fun splitSegment(
        startCell: CoordinatesOfCell,
        splittingSegment: Segment,
        listForResult: MutableList<TrackPartInSingleCell>): MutableList<TrackPartInSingleCell> {
        val transitionTo = TransitionManager(cellSize).getTransitionTo( // преверяем на наличие перехода
            splittingSegment.startCellLocation,
            splittingSegment.endSegmentCellLocation)

        if (transitionTo == TransitionTo.NONE) listForResult.add(TrackPartInSingleCell(startCell,
            mutableListOf(
                splittingSegment.startCellLocation,
                splittingSegment.endSegmentLocation )))

        else {
            val nextCell = NextCellNumber().get(cellNumber = startCell, transitionTo = transitionTo)

            val transitionPoints = CellTransitionByEnum(cellSize, TracksGirdCalculator).getTransitionPoints(
                transitionTo = transitionTo,
                startCellLocation = splittingSegment.startCellLocation,
                endCellLocation = splittingSegment.endSegmentCellLocation)
            logger.printLog("calc transition points: transTo = $transitionTo, start = ${splittingSegment.startCellLocation}, end = ${splittingSegment.endSegmentCellLocation}" +
                    ", result = $transitionPoints")

            val nextSegment = splittingSegment.getCutted(transitionPoints[1])
            listForResult.add(TrackPartInSingleCell( cell = nextCell, mutableListOf(
                splittingSegment.startCellLocation, transitionPoints[0])))
            logger.printLog("making next segment: change start: ${splittingSegment.startCellLocation} to ${transitionPoints[0]}, end: ${splittingSegment.endSegmentLocation} to ${nextSegment.endSegmentLocation}")
            logger.printLog("make recursion slitting. Next segment = $nextSegment \n")
            splitSegment(startCell = nextCell, splittingSegment = nextSegment, listForResult = listForResult)
        }

        return listForResult
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
    private fun GlobalTurnPoint.print(point: GlobalTurnPoint) {
        logger.printLog( "next TP is: ${point.longitude}-${point.latitude}")
    }

    private fun coordinatesInsideCellByGlobal (globalCoordinates: GlobalTurnPoint, coordinatesOfCell: CoordinatesOfCell): CellLocation {
        return CellLocation(
            x = globalCoordinates.longitude - (coordinatesOfCell.x*cellSize),
            y = globalCoordinates.latitude - (coordinatesOfCell.y*cellSize)
        )
    }


}