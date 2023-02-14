package com.example.lessongirdrecycler.domain.models.segment

import com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition.TransitionTo
import com.example.lessongirdrecycler.domain.models.cell.CellLocation

class Segment (val cellSize: Int,
               var startCellLocation: CellLocation,
               val endCellLocation: CellLocation,) {
    var endSegmentLocation: CellLocation = CellLocation(0, 0)
    init{
        endSegmentLocation = CellLocation(
            x = endCellLocation.x - startCellLocation.x,
            y = endCellLocation.y - startCellLocation.y)
    }

    override fun toString(): String {
        return "start: $startCellLocation, end: $endCellLocation"
    }

    fun getCutted(currentEndLocation: CellLocation, nextStartLocation: CellLocation): Segment{
        val deltaX = startCellLocation.x - currentEndLocation.x
        val deltay = startCellLocation.y - currentEndLocation.y
        val nextEnd = CellLocation(
            x = endCellLocation.x + deltaX,
            y= endCellLocation.y + deltay)
        return Segment(cellSize = cellSize, startCellLocation = nextStartLocation, endCellLocation = nextEnd)
    }

    fun cutOne(nextStartLocation: CellLocation, transitionTo: TransitionTo) { // todo: delete?

        when(transitionTo) {
            TransitionTo.NORTH -> {
                endSegmentLocation = CellLocation(
                    x = endSegmentLocation.x,
                    y = endSegmentLocation.y - startCellLocation.y)
            }
            TransitionTo.NE -> {
                endSegmentLocation = CellLocation(
                    x = endSegmentLocation.x - (cellSize - startCellLocation.x),
                    y = endSegmentLocation.y - startCellLocation.y)
            }
            TransitionTo.EAST -> {
                endSegmentLocation = CellLocation(
                    x = endSegmentLocation.x - (cellSize - startCellLocation.x),
                    y = endSegmentLocation.y)
            }
            TransitionTo.SE -> {
                endSegmentLocation = CellLocation(
                    x = endSegmentLocation.x - (cellSize - startCellLocation.x),
                    y = endSegmentLocation.y - (cellSize - startCellLocation.y))
            }
            TransitionTo.SOUTH -> {
                endSegmentLocation = CellLocation(
                    x = endSegmentLocation.x,
                    y = endSegmentLocation.y - (cellSize - startCellLocation.y))
            }
            TransitionTo.SW -> {
                endSegmentLocation = CellLocation(
                    x = endSegmentLocation.x - (startCellLocation.x),
                    y = endSegmentLocation.y - (cellSize - startCellLocation.y))
            }
            TransitionTo.WEST -> {
                endSegmentLocation = CellLocation(
                    x = endSegmentLocation.x - (startCellLocation.x),
                    y = endSegmentLocation.y)
            }
            TransitionTo.NW -> {
                endSegmentLocation = CellLocation(
                    x = endSegmentLocation.x - startCellLocation.x,
                    y = endSegmentLocation.y - startCellLocation.y)
            }
            TransitionTo.NONE -> {}
        }

        startCellLocation = nextStartLocation
        return
    }

    fun makeCutSegment(nextStartLocation: CellLocation, transitionTo: TransitionTo): Segment {
        var endLocation = CellLocation(0, 0)
        when(transitionTo) {
            TransitionTo.NORTH -> {
                endLocation = CellLocation(
                    x = endSegmentLocation.x,
                    y = endSegmentLocation.y - startCellLocation.y)
            }
            TransitionTo.NE -> {
                endLocation = CellLocation(
                    x = endSegmentLocation.x - (cellSize - startCellLocation.x),
                    y = endSegmentLocation.y - startCellLocation.y)
            }
            TransitionTo.EAST -> {
                endLocation = CellLocation(
                    x = endSegmentLocation.x - (cellSize - startCellLocation.x),
                    y = endSegmentLocation.y)
            }
            TransitionTo.SE -> {
                endLocation = CellLocation(
                    x = endSegmentLocation.x - (cellSize - startCellLocation.x),
                    y = endSegmentLocation.y - (cellSize - startCellLocation.y))
            }
            TransitionTo.SOUTH -> {
                endLocation = CellLocation(
                    x = endSegmentLocation.x,
                    y = endSegmentLocation.y - (cellSize - startCellLocation.y))
            }
            TransitionTo.SW -> {
                endLocation = CellLocation(
                    x = endSegmentLocation.x - (startCellLocation.x),
                    y = endSegmentLocation.y - (cellSize - startCellLocation.y))
            }
            TransitionTo.WEST -> {
                endLocation = CellLocation(
                    x = endSegmentLocation.x - (startCellLocation.x),
                    y = endSegmentLocation.y)
            }
            TransitionTo.NW -> {
                endLocation = CellLocation(
                    x = endSegmentLocation.x - startCellLocation.x,
                    y = endSegmentLocation.y - startCellLocation.y)
            }
            TransitionTo.NONE -> {}
        }

        startCellLocation = nextStartLocation
        return Segment(
            cellSize = cellSize,
            startCellLocation = startCellLocation,
            endCellLocation = endLocation)
    }


    fun isCellTransfer(): Boolean {
        return (endCellLocation.x > cellSize || endCellLocation.y > cellSize)
    }

//    private fun currentCellEnd(): CellLocation{
//        val transition = SegmentTransitionManager().getTransition(endCellLocation = endSegmentCellLocation)
//        val end: CellLocation
//        var endX: Int
//        var endY: Int
//        when (transition) {
//            SegmentTransitionTo.NORTH -> {
//                endX = endSegmentCellLocation.x
//                endY = 0}
//            SegmentTransitionTo.SOUTH -> {
//                endX = endSegmentCellLocation.x
//                endY = cellSize }
//            SegmentTransitionTo.EAST -> {
//                endX = cellSize
//                endY = endSegmentCellLocation.y }
//            SegmentTransitionTo.WEST -> {
//                endX = 0
//                endY = endSegmentCellLocation.y }
//            SegmentTransitionTo.NE -> {
//                val rateToCellCorner = (cellSize - startCellLocation.x)/(cellSize - startCellLocation.y)
//                val rateToSegmentEnd = endSegmentLocation.x/endSegmentLocation.y
//                if (rateToSegmentEnd < rateToCellCorner) { // через верхнюю сторону
//                    // находим неполную сторону (тут это Х) через пропорцию
//                    // пропорция = "сколько всего"/"сколько осталось до границы"
//                    // сколько надо пройти = остаток * пропорция
//                    // сколько осталось от неполной стороны = сколько уже есть + сколько надо пройти
//                    endX = endSegmentLocation.y/startCellLocation.y*(endSegmentLocation.x) + startCellLocation.x
//                    endY = 0
//                } else if (rateToSegmentEnd > rateToCellCorner) { // через правую сторону
//                    endX = cellSize
//                    endY = endSegmentLocation.x/startCellLocation.x*(endSegmentLocation.y) + startCellLocation.y
//                } else { // через угол
//                    endX = cellSize
//                    endY = 0
//                }
//            }
//        }
//        return CellLocation(x = endX, y = endY)
//    }

}