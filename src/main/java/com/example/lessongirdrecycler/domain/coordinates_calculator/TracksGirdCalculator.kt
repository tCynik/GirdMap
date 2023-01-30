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
 * Растаскивание трека глобальных точек на серию треков в рамках отдельных ячеек координатной сетки
 * (т.е. получаем набор мини-треков с отметкой - к какоя ячейке они относятся)
 * логика такая:
 * 1. есть начало координат, относительно которого из лобальных координат получаем кординаты карты
 * 2.
 */
class TracksGirdCalculator(
    val girdStartX: Double,
    val girdStartY: Double,
    val coordinateScale: Int,
    val cellSize: Int, // размер клетки в базовых единицах глобальной системы координат
    ) {
    companion object logger: Logger{
        override fun printLog(logMessage: String) {
            Log.i("bugfix: Calculator", logMessage)
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
        var iter = 1
        while (iter < turnPoints.size) { // перебор точек трека
//            val bigSegment: Segment = makeBigSegment(turnPoints[iter -1], turnPoints[iter])
//            val segments: List<TrackPartInSingleCell> = splitSegment(bigSegment)
//            resultSplitted = distributeSegments(resultSplitted, segments)
//            //resultSplitted.plusSplitted(distributedByCellsSegment)
//            iter++
        }
        return resultSplitted


//        // старый код
//        splittedByCellsTrack.addLocation(coordinatesOfCell = currentCell, cellLocation = firstTurnPoint)
//
//        var i = 1
//        while (i < track.turnPoints.size) {
//            val currentTurnPoint = track.turnPoints[i-1]
//            val nextTurnPoint = track.turnPoints[i]
//
//            val segmentStartCell = cellCoordinates(currentTurnPoint)
//
//            var currentSegment = Segment( // берём текущий сегмент с системе координат стартовой его ячейки
//                cellSize = cellSize,
//                startCellLocation = coordinatesInsideCellByGlobal(currentTurnPoint, segmentStartCell),
//                endSegmentCellLocation = coordinatesInsideCellByGlobal(nextTurnPoint, segmentStartCell))
//
//            if (segmentStartCell.equals(cellCoordinates(nextTurnPoint))) { // если конец сегмента в этой же ячейке
//                splittedByCellsTrack.addLocation(coordinatesOfCell = segmentStartCell, cellLocation = currentSegment.endSegmentCellLocation)
//            } else { // либо если сегмент придется разбивать
//                var transitionTo = TransitionManager(cellSize).getTransitionTo(
//                    currentSegment.startCellLocation,
//                    currentSegment.endSegmentCellLocation)
//                var nextCellNumber = NextCellNumber().get(
//                    cellNumber = segmentStartCell,
//                    transitionTo = transitionTo)
//
//                while (transitionTo != TransitionTo.NONE) {
//                    transitionTo = processingTransition(
//                        segmentStartCell = segmentStartCell,
//                        currentSegment = currentSegment)
//                    Log.i("bugfix: calculator", "next transitionTo = $transitionTo")
//
//                }
//                splittedByCellsTrack.addLocation(coordinatesOfCell = nextCellNumber, cellLocation = currentSegment.endSegmentLocation)
//            }
//            i++
//        }
//        Log.i("bugfix: calculator", "track was splitted:")
//        splittedByCellsTrack.cellData.forEach{trackInCell -> Log.i("bugfix", "cell#: ${trackInCell.cell} , start: ${trackInCell.track[0]}, end: ${trackInCell.track[trackInCell.track.size -1]}")}
//        return splittedByCellsTrack
    }

    private fun makeBigSegment(currentPoint: GlobalTurnPoint, nextTurnPoint: GlobalTurnPoint): Segment {
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
            splittingSegment.endSegmentCellLocation)

        if (transitionTo == TransitionTo.NONE) listForResult.add(TrackPartInSingleCell(startCell,
            mutableListOf(
                splittingSegment.startCellLocation,
                splittingSegment.endSegmentLocation )))

        else {
            val nextCell = NextCellNumber().get(cellNumber = startCell, transitionTo = transitionTo)

            val transitionPoints = CellTransitionByEnum(cellSize, logger).getTransitionPoints(
                transitionTo = transitionTo,
                startCellLocation = splittingSegment.startCellLocation,
                endCellLocation = splittingSegment.endSegmentCellLocation)
            val nextSegment = Segment(cellSize = cellSize,
                startCellLocation = transitionPoints[0],
                endSegmentCellLocation = splittingSegment.endSegmentCellLocation) // todo: ошибка в логике
            // мф переходим в новую ячейку, и для неё у нас те же самые координаты конца?
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
            currentSegment.endSegmentCellLocation)

        val nextCellNumber = NextCellNumber().get(
            cellNumber = segmentStartCell,
            transitionTo = transitionTo)
        val transitionPoints = CellTransitionByEnum(cellSize, logger).getTransitionPoints(
            transitionTo = transitionTo,
            startCellLocation = currentSegment.startCellLocation,
            endCellLocation = currentSegment.endSegmentCellLocation)
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
