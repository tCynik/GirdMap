package com.example.lessongirdrecycler.domain.models.segment

import android.util.Log
import com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition.TransitionTo
import com.example.lessongirdrecycler.domain.models.cell.CellLocation

class CellTransitionByEnum(val cellSize: Int) {
    fun getTransitionPoints(
        transitionTo: TransitionTo,
        startCellLocation: CellLocation,
        endCellLocation: CellLocation): List<CellLocation> {
        val transitionLocations = mutableListOf<CellLocation>() // [0] - current cell, [1] - next cell
        val endSegmentLocation = CellLocation(
            x = endCellLocation.x - startCellLocation.x,
            y = endCellLocation.y - startCellLocation.y)
        val rateToSegmentEnd = endSegmentLocation.x/endSegmentLocation.y

        when (transitionTo) {

            TransitionTo.NORTH -> {
                transitionLocations.add(CellLocation(x = endCellLocation.x, y = 0))
                transitionLocations.add(CellLocation(x = endCellLocation.x, y = cellSize))
            }

            TransitionTo.NE -> {
                val rateToCellCorner = (cellSize - startCellLocation.x)/startCellLocation.y
                if (rateToSegmentEnd < rateToCellCorner) { // через верхнюю сторону -> считаем от Y
                    // находим неполную сторону (тут это Х) через пропорцию подобных треугольников
                    // сколько осталось от неполной стороны = сколько уже есть + сколько надо пройти: Х = Хнач + Хост
                    // сколько надо пройти = остаток * пропорция: Xост = К * Xвсг
                    // пропорция = "сколько всего"/"сколько осталось до границы": К = Yост / Yвсг
                    // итого: Х = Хнач + Хвсг * (Yост / Yвсг)
                    transitionLocations.add(CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = 0))
                    transitionLocations.add(CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = cellSize))
                } else if (rateToSegmentEnd > rateToCellCorner) { // через правую сторону
                    transitionLocations.add(CellLocation(x = cellSize, y = calculateByY(startCellLocation, endSegmentLocation)))
                    transitionLocations.add(CellLocation(x = 0, y = calculateByY(startCellLocation, endSegmentLocation)))
                } else { // через угол
                    transitionLocations.add(CellLocation(x = cellSize, y = 0))
                    transitionLocations.add(CellLocation(x = 0, y = 0))
                }
            }

            TransitionTo.EAST -> {
                transitionLocations.add(CellLocation(x = cellSize, y = endCellLocation.y))
                transitionLocations.add(CellLocation(x = 0, y = endCellLocation.y))
            }

            TransitionTo.SE -> {
                Log.i("bugfix: cellTransition", "cellSize = ${cellSize}, transition to SE. start coord: x = ${startCellLocation.x}, y = ${startCellLocation.y}")
                val rateToCellCorner = (cellSize - startCellLocation.x)/(cellSize - startCellLocation.y)
                if (rateToSegmentEnd < rateToCellCorner) { // через бок -> считаем Y
                    transitionLocations.add(CellLocation(x = cellSize, y = calculateByY(startCellLocation, endSegmentLocation)))
                    transitionLocations.add(CellLocation(x = 0, y = calculateByY(startCellLocation, endSegmentLocation)))
                } else if (rateToSegmentEnd > rateToCellCorner) { // через низ, считаем Х
                    transitionLocations.add(CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = cellSize))
                    transitionLocations.add(CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = 0))
                } else { // углы равны, трек идет через грань
                    transitionLocations.add(CellLocation(x = cellSize, y = cellSize))
                    transitionLocations.add(CellLocation(x = 0, y = 0))
                }

            }

            TransitionTo.SOUTH -> {
                transitionLocations.add(CellLocation(x = endCellLocation.x, y = cellSize))
                transitionLocations.add(CellLocation(x = endCellLocation.x, y = 0))
            }

            TransitionTo.SW -> {
                val rateToCellCorner = startCellLocation.x/(cellSize - startCellLocation.y)
                if (rateToSegmentEnd < rateToCellCorner) { // calculating y
                    transitionLocations.add(CellLocation(x = 0, y = calculateByY(startCellLocation, endSegmentLocation)))
                    transitionLocations.add(CellLocation(x = cellSize, y = calculateByY(startCellLocation, endSegmentLocation)))
                } else if (rateToSegmentEnd > rateToCellCorner) { // calculating X
                    transitionLocations.add(CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = cellSize))
                    transitionLocations.add(CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = 0))
                } else {
                    transitionLocations.add(CellLocation(x = 0, y = cellSize))
                    transitionLocations.add(CellLocation(x = cellSize, y = 0))
                }
            }

            TransitionTo.WEST -> {
                transitionLocations.add(CellLocation(x = 0, y = endCellLocation.y))
                transitionLocations.add(CellLocation(x = cellSize, y = endCellLocation.y))
            }

            TransitionTo.NW -> {
                val rateToCellCorner = startCellLocation.x/startCellLocation.y
                if (rateToSegmentEnd < rateToCellCorner) { // calculating X
                    transitionLocations.add(CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = 0))
                    transitionLocations.add(CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = cellSize))
                }else if (rateToSegmentEnd > rateToCellCorner) { // calculating by X
                    transitionLocations.add(CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = 0))
                    transitionLocations.add(CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = cellSize))
                } else { // transition by corner
                    transitionLocations.add(CellLocation(x = 0, y = 0))
                    transitionLocations.add(CellLocation(x = cellSize, y = cellSize))
                }
            }

            TransitionTo.NONE -> {
                transitionLocations.add(endCellLocation)
                transitionLocations.add(endCellLocation)
            }
        }
        return transitionLocations
    }

    private fun calculateByX(startCellLocation: CellLocation, endSegmentLocation: CellLocation): Int {
        return ((startCellLocation.y/endSegmentLocation.y)*(endSegmentLocation.x)) + startCellLocation.x
    }

    private fun calculateByY(startCellLocation: CellLocation, endSegmentLocation: CellLocation): Int {
        return ((startCellLocation.x/endSegmentLocation.x)*(endSegmentLocation.y)) + startCellLocation.y
    }
}