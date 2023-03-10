package com.example.lessongirdrecycler.domain.coordinates_calculator

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
        logger.printLog("first time cell = $currentCell")

        val turnPoints = track.turnPoints
        var listForResult = mutableListOf<TrackPartInSingleCell>()
        logger.printLog("starting to split the track, size of track = ${turnPoints.size}. Turnpoints mapped to local coordinates:")
        turnPoints.forEach{point -> point.print(point)}

        var iter = 1
        while (iter < turnPoints.size) { // перебор точек трека
            val currentPoint = turnPoints[iter -1]
            val nextPoint = turnPoints[iter]
            currentCell = cellCoordinates(currentPoint)
            logger.printLog("cell of the current iteration: $currentCell")

            val currentSegment = Segment(
                cellSize = cellSize,
                startCellLocation = coordinatesInsideCellByGlobal(currentPoint, currentCell),
                endCellLocation = coordinatesInsideCellByGlobal(nextPoint, currentCell))//makeBigSegment(currentPoint, nextPoint)
            logger.printLog("next global point = $nextPoint, local = ${coordinatesInsideCellByGlobal(nextPoint, currentCell)}, segment = $currentSegment")
            logger.printLog("spliting iter = $iter, cell = $currentCell currentPoint = $currentPoint, next point = $nextPoint, making the segment= ${currentSegment} ")

            listForResult = distributeSegmentToCells(
                startCell = currentCell,
                splittingSegment = currentSegment,
                listForResult = listForResult)
            //resultSplitted = distributeSegments(resultSplitted, segments)
            iter++
        }
        logger.printLog("splitting ended, splitted size = ${listForResult.size}")

        printList(listForResult)

        val resultSplitted = SplittedByCellsTrack()
        resultSplitted.cellData = listForResult
        return resultSplitted
    }

    private fun printList(listToPrint: List<TrackPartInSingleCell>) {
        listToPrint.forEach { cellTrackPart ->
            logger.printLog("splitting Segment. cell = ${cellTrackPart.cell}, segment coordinates = ${cellTrackPart.track[0]} ${cellTrackPart.track[1]}") }
    }

    private fun distributeSegmentToCells(
        startCell: CoordinatesOfCell,
        splittingSegment: Segment,
        listForResult: MutableList<TrackPartInSingleCell>): MutableList<TrackPartInSingleCell> {
        val transitionTo = TransitionManager(cellSize).getTransitionTo( // преверяем на наличие перехода
            splittingSegment.startCellLocation,
            splittingSegment.endCellLocation)

        if (transitionTo == TransitionTo.NONE) listForResult.add(TrackPartInSingleCell(startCell,
            mutableListOf(
                splittingSegment.startCellLocation,
                splittingSegment.endSegmentLocation )))

        else {
            var splittedList = mutableListOf<TrackPartInSingleCell>()
            splittedList = splitSegment(startCell = startCell, splittingSegment = splittingSegment, listForResult = splittedList, transitionTo = transitionTo)
            listForResult.addAll(splittedList)
        }

        return listForResult
    }

    private fun splitSegment(
        startCell: CoordinatesOfCell,
        splittingSegment: Segment,
        listForResult: MutableList<TrackPartInSingleCell>,
        transitionTo: TransitionTo): MutableList<TrackPartInSingleCell> {

        val nextCell = NextCellNumber().get(cellNumber = startCell, transitionTo = transitionTo)

        val transitionPoints = CellTransitionByEnum(cellSize, TracksGirdCalculator).getTransitionPoints(
            transitionTo = transitionTo,
            startCellLocation = splittingSegment.startCellLocation,
            endCellLocation = splittingSegment.endCellLocation)
        logger.printLog("calc transition points: transTo = $transitionTo, start = ${splittingSegment.startCellLocation}, end = ${splittingSegment.endCellLocation}" +
                ", result = $transitionPoints")

        val nextSegment = splittingSegment.getCutted(transitionPoints[0], transitionPoints[1])
        listForResult.add(TrackPartInSingleCell( cell = startCell, mutableListOf(
            splittingSegment.startCellLocation, transitionPoints[0])))
        
        // в тесте должно получиться новые кооринаты окончания: -100:-100  , получается -50:100
        logger.printLog("making next segment: change start: ${splittingSegment.startCellLocation} to ${transitionPoints[0]}, end: ${splittingSegment.endSegmentLocation} to ${nextSegment.endSegmentLocation}")
        logger.printLog("make recursion slitting. Next segment = $nextSegment \n")
        val nextTransitionTo = TransitionManager(cellSize).getTransitionTo( // преверяем на наличие перехода
            splittingSegment.startCellLocation,
            splittingSegment.endCellLocation)
        if (nextTransitionTo != TransitionTo.NONE)
            splitSegment(
                startCell = nextCell,
                splittingSegment = nextSegment,
                listForResult = listForResult,
                transitionTo = nextTransitionTo)
        return listForResult
    }

    private fun cellCoordinates(globalCoordinates: GlobalTurnPoint): CoordinatesOfCell {
        val localCoordinates = globalCoordinatesToLocal(globalCoordinates)
        val coordinatesOfCell = CoordinatesOfCell(
            x = (localCoordinates.x/(cellSize+1)).toInt(),
            y = (localCoordinates.y/(cellSize+1)).toInt()
        )
        return coordinatesOfCell
    }

    private fun globalCoordinatesToLocal(globalCoordinates: GlobalTurnPoint): CellLocation{ // вынести в отдельный класс (в видеинтерфейса?)
        return CellLocation(globalCoordinates.longitude, globalCoordinates.latitude)
    }
    private fun GlobalTurnPoint.print(point: GlobalTurnPoint) {
        logger.printLog( "mapped to local next TP coordinates is: ${point.longitude}-${point.latitude}")
    }

    private fun coordinatesInsideCellByGlobal (globalCoordinates: GlobalTurnPoint, coordinatesOfCell: CoordinatesOfCell): CellLocation {
        return CellLocation(
            x = globalCoordinates.longitude - (coordinatesOfCell.x*cellSize),
            y = globalCoordinates.latitude - (coordinatesOfCell.y*cellSize)
        )
    }


}