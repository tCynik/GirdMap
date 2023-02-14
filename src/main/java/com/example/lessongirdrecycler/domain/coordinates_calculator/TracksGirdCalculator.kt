package com.example.lessongirdrecycler.domain.coordinates_calculator

import android.util.Log
import com.example.lessongirdrecycler.domain.Logger
import com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition.NextCellNumber
import com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition.TransitionManager
import com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition.TransitionTo
import com.example.lessongirdrecycler.domain.models.cell.SplittedByCellsTrack
import com.example.lessongirdrecycler.domain.models.cell.CoordinatesOfCell
import com.example.lessongirdrecycler.domain.models.cell.CellLocation
import com.example.lessongirdrecycler.domain.models.cell.TrackPartInSingleCell
import com.example.lessongirdrecycler.domain.models.global.GlobalTrack
import com.example.lessongirdrecycler.domain.models.global.GlobalTurnPoint
import com.example.lessongirdrecycler.domain.models.segment.CellTransitionByEnum
import com.example.lessongirdrecycler.domain.models.segment.Segment

/**
 * Класс занимается менеджментом реального трека на нашей локальной сетке координат
 * Обеспечивает динамическое расширение границ сетки если это необходимо
 * Содержит и управляет сплиттером, который разбивает ему большой трек по ячейкам
 */
class TracksGirdCalculator(
    val girdStartX: Double,
    val girdStartY: Double,
    val coordinateScale: Int,
    val cellSize: Int, // размер клетки в базовых единицах глобальной системы координат
    ) {
    companion object logger: Logger{
        override fun printLog(logMessage: String) {
            //Log.i("bugfix: Calculator", logMessage)
        }
    }

    var splittedByCellsTrack: SplittedByCellsTrack = SplittedByCellsTrack()

    fun splitGlobalTrackToCells(track: GlobalTrack): SplittedByCellsTrack {
        // каждой координате определяем ячейку, в которой точка, и координаты точки внутри ячейки
        // Если по дороге переход в другую ячейку, находим пограничные координаты для обеих ячеек
        // для каждой ячейки находим местный трек (GirdTrack)
        //val splittedByCellsTrack = SplittedByCellsTrack()
        var currentCell = cellCoordinates(track.turnPoints[0])

        val firstTurnPoint: CellLocation = coordinatesInsideCellByGlobal(
            globalCoordinates = track.turnPoints[0], coordinatesOfCell = currentCell)
        // отсюда новый код
        var resultSplitted = SplittedByCellsTrack()
        val turnPoints = track.turnPoints
        turnPoints.forEach{ point -> point.print(point)}
        var listForResult = mutableListOf<TrackPartInSingleCell>()
        logger.printLog("starting to split the segment, size of track = ${turnPoints.size}")
        var iter = 1
        while (iter < turnPoints.size) { // перебор точек трека
            val currentPoint = turnPoints[iter -1]
            val nextPoint = turnPoints[iter]
            currentCell = cellCoordinates(currentPoint)
            logger.printLog("spliting iter = $iter, cell = $currentCell currentPoint = $currentPoint, next point = $nextPoint ")
            val currentSegment: Segment = makeBigSegment(currentPoint, nextPoint)
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
        resultSplitted.cellData = listForResult
        return resultSplitted
    }

    private fun makeBigSegment( // разбитие трека на сегменты по поворотам
        currentPoint: GlobalTurnPoint,
        nextTurnPoint: GlobalTurnPoint): Segment {
        val cell = cellCoordinatesFromStart(currentPoint)
        val currentCoordinatrs = coordinatesInsideCellByGlobal(currentPoint, cell)
        val nextCoordinates = coordinatesInsideCellByGlobal(nextTurnPoint, cell)
        return Segment (cellSize = cellSize, currentCoordinatrs, nextCoordinates)
    }

    private fun splitSegment(
        startCell: CoordinatesOfCell,
        splittingSegment: Segment,
        listForResult: MutableList<TrackPartInSingleCell>): MutableList<TrackPartInSingleCell> {

        var transitionTo = TransitionManager(cellSize).getTransitionTo( // преверяем на наличие перехода
            splittingSegment.startCellLocation,
            splittingSegment.endCellLocation)

        if (transitionTo == TransitionTo.NONE) listForResult.add(TrackPartInSingleCell(startCell,
            mutableListOf(
                splittingSegment.startCellLocation,
                splittingSegment.endSegmentLocation )))

        else {
            val nextCell = NextCellNumber().get(cellNumber = startCell, transitionTo = transitionTo)

            val transitionPoints = CellTransitionByEnum(cellSize, logger).getTransitionPoints(
                transitionTo = transitionTo,
                startCellLocation = splittingSegment.startCellLocation,
                endCellLocation = splittingSegment.endCellLocation)
            val nextSegment = Segment(cellSize = cellSize,
                startCellLocation = transitionPoints[0],
                endCellLocation = splittingSegment.endCellLocation) // todo: ошибка в логике
            // мы переходим в новую ячейку, и для неё у нас те же самые координаты конца?
            // В таком случае рекурсия никогда не закончится
            // типа выходит, нужно пересчитывать координаты конца под новую ячейку?

            splitSegment(startCell = nextCell, splittingSegment = nextSegment, listForResult = listForResult)
        }

        return listForResult
    }

    private fun distributeSegments(resultSplitted: SplittedByCellsTrack, cellSegments: List<TrackPartInSingleCell>): SplittedByCellsTrack {
        // если предыдущий сегмент закончился и следующий начался внутри одной ячейки
        val resultEndCell = resultSplitted.cellData[resultSplitted.cellData.size].cell
        if (resultEndCell == cellSegments[0].cell) // добавляем в той же ячейке следующую точку
            resultSplitted.addLocation(cellSegments[0].cell, cellSegments[0].track[1])
        // todo: а если не одна точка в той же ячейке? Всё надо перегонять до смены ячейки

        var i = 1
        while (i < cellSegments.size) {
            // перебираем отрезки. Вставляем из каждой ячейки все точки треков в splitted
            i++
        }

        return resultSplitted
    }

    private fun processingTransition(segmentStartCell: CoordinatesOfCell, currentSegment: Segment): TransitionTo {
        val transitionTo = TransitionManager(cellSize).getTransitionTo( // преверяем на наличие перехода
            currentSegment.startCellLocation,
            currentSegment.endCellLocation)

        val nextCellNumber = NextCellNumber().get(
            cellNumber = segmentStartCell,
            transitionTo = transitionTo)
        val transitionPoints = CellTransitionByEnum(cellSize, logger).getTransitionPoints(
            transitionTo = transitionTo,
            startCellLocation = currentSegment.startCellLocation,
            endCellLocation = currentSegment.endCellLocation)
        Log.i("bugfix: calculator", "transition prev ${transitionPoints[0]} to ${transitionPoints[1]}")

        splittedByCellsTrack.addLocation( // end track in current cell
            coordinatesOfCell = segmentStartCell,
            cellLocation = transitionPoints[0])
        splittedByCellsTrack.addLocation( // start track in the next cell
            coordinatesOfCell = nextCellNumber,
            cellLocation = transitionPoints[1])

        Log.i ("bugfix: calculator", "cell #${segmentStartCell}, end: ${splittedByCellsTrack.cellData[0].track} ; next cell: $nextCellNumber, start: ${splittedByCellsTrack.cellData[1]}")
        currentSegment.cutOne(transitionPoints[0], transitionTo) // отрезаем сегмент
        return transitionTo
    }

    private fun cellCoordinatesFromStart(globalCoordinates: GlobalTurnPoint): CoordinatesOfCell {
        checkIsOffTheBoarder(globalCoordinates)
        val xFromStart = (globalCoordinates.latitude - girdStartX) * coordinateScale
        val yFromStart = (globalCoordinates.longitude - girdStartY) * coordinateScale
        return CoordinatesOfCell(x = (xFromStart/cellSize).toInt(), y = (yFromStart/cellSize).toInt())
    }

    private fun checkIsOffTheBoarder(globalCoordinates: GlobalTurnPoint){}

    private fun cellCoordinates(globalCoordinates: GlobalTurnPoint): CoordinatesOfCell {
        val coordinatesOfCell = CoordinatesOfCell(
            // todo: тут будет необходимо учесть, что мы берем координаты ячеек из глобальных координат
            // брать из местных?
            x = globalCoordinates.longitude/cellSize.toInt(),
            y = globalCoordinates.latitude/cellSize.toInt()
        )
        return coordinatesOfCell
    }

    private fun coordinatesInsideCellByGlobal (globalCoordinates: GlobalTurnPoint, coordinatesOfCell: CoordinatesOfCell): CellLocation {
        return CellLocation(
            x = globalCoordinates.longitude - (coordinatesOfCell.x*cellSize),
            y = globalCoordinates.latitude - (coordinatesOfCell.y*cellSize)
        )
    }

//    private fun coordinatesInsideCellByLocal (cellLocation: CellLocation, )
}

private fun GlobalTurnPoint.print(point: GlobalTurnPoint) {
    Log.i("bugfix: Calculator", "next TP is: ${point.longitude}-${point.latitude}")
}
