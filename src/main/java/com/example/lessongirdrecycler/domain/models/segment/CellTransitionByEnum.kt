package com.example.lessongirdrecycler.domain.models.segment

import com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition.TransitionTo
import com.example.lessongirdrecycler.domain.models.cell.CellLocation

class CellTransitionByEnum(val cellSize: Int) {
    fun getTransitionPoints(
        transitionTo: TransitionTo,
        startCellLocation: CellLocation,
        endCellLocation: CellLocation): List<CellLocation> {
        val transitionLocations = mutableListOf<CellLocation>()
        val endSegmentLocation = CellLocation(
            x = endCellLocation.x - startCellLocation.x,
            y = endCellLocation.y - startCellLocation.y)
        val rateToSegmentEnd = endSegmentLocation.x/endSegmentLocation.y

        when (transitionTo) {

            TransitionTo.NORTH -> {
                transitionLocations[0] = CellLocation(x = endCellLocation.x, y = 0)
                transitionLocations[1] = CellLocation(x = endCellLocation.x, y = cellSize)
            }

            TransitionTo.NE -> {
                val rateToCellCorner = (cellSize - startCellLocation.x)/startCellLocation.y
                if (rateToSegmentEnd < rateToCellCorner) { // через верхнюю сторону -> считаем от Y
                    // находим неполную сторону (тут это Х) через пропорцию подобных треугольников
                    // сколько осталось от неполной стороны = сколько уже есть + сколько надо пройти: Х = Хнач + Хост
                    // сколько надо пройти = остаток * пропорция: Xост = К * Xвсг
                    // пропорция = "сколько всего"/"сколько осталось до границы": К = Yост / Yвсг
                    // итого: Х = Хнач + Хвсг * (Yост / Yвсг)
                    transitionLocations[0] = CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = 0)
                    transitionLocations[1] = CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = cellSize)
                } else if (rateToSegmentEnd > rateToCellCorner) { // через правую сторону
                    transitionLocations[0] = CellLocation(x = cellSize, y = calculateByY(startCellLocation, endSegmentLocation))
                    transitionLocations[1] = CellLocation(x = 0, y = calculateByY(startCellLocation, endSegmentLocation))
                } else { // через угол
                    transitionLocations[0] = CellLocation(x = cellSize, y = 0)
                    transitionLocations[1] = CellLocation(x = 0, y = 0)
                }
            }

            TransitionTo.EAST -> {
                transitionLocations[0] = CellLocation(x = cellSize, y = endCellLocation.y)
                transitionLocations[1] = CellLocation(x = 0, y = endCellLocation.y)
            }

            TransitionTo.SE -> {
                val rateToCellCorner = (cellSize - startCellLocation.x)/(cellSize - startCellLocation.y)
                if (rateToSegmentEnd < rateToCellCorner) { // через бок -> считаем Y
                    transitionLocations[0] = CellLocation(x = cellSize, y = calculateByY(startCellLocation, endSegmentLocation))
                    transitionLocations[1] = CellLocation(x = 0, y = calculateByY(startCellLocation, endSegmentLocation))
                } else if (rateToSegmentEnd > rateToCellCorner) { // через низ, считаем Х
                    transitionLocations[0] = CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = cellSize)
                    transitionLocations[1] = CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = 0)
                } else { // углы равны, трек идет через грань
                    transitionLocations[0] = CellLocation(x = cellSize, y = cellSize)
                    transitionLocations[1] = CellLocation(x = 0, y = 0)
                }

            }

            TransitionTo.SOUTH -> {
                transitionLocations[0] = CellLocation(x = endCellLocation.x, y = cellSize)
                transitionLocations[1] = CellLocation(x = endCellLocation.x, y = 0)
            }

            TransitionTo.SW -> {
                val rateToCellCorner = startCellLocation.x/(cellSize - startCellLocation.y)
                if (rateToSegmentEnd < rateToCellCorner) { // calculating y
                    transitionLocations[0] = CellLocation(x = 0, y = calculateByY(startCellLocation, endSegmentLocation))
                    transitionLocations[1] = CellLocation(x = cellSize, y = calculateByY(startCellLocation, endSegmentLocation))
                } else if (rateToSegmentEnd > rateToCellCorner) { // calculating X
                    transitionLocations[0] = CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = cellSize)
                    transitionLocations[1] = CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = 0)
                } else {
                    transitionLocations[0] = CellLocation(x = 0, y = cellSize)
                    transitionLocations[1] = CellLocation(x = cellSize, y = 0)
                }
            }

            TransitionTo.WEST -> {
                transitionLocations[0] = CellLocation(x = 0, y = endCellLocation.y)
                transitionLocations[1] = CellLocation(x = cellSize, y = endCellLocation.y)
            }

            TransitionTo.NW -> {
                val rateToCellCorner = startCellLocation.x/startCellLocation.y
                if (rateToSegmentEnd < rateToCellCorner) { // calculating X
                    transitionLocations[0] = CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = 0)
                    transitionLocations[1] = CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = cellSize)
                }else if (rateToSegmentEnd > rateToCellCorner) { // calculating by X
                    transitionLocations[0] = CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = 0)
                    transitionLocations[1] = CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = cellSize)
                } else { // transition by corner
                    transitionLocations[0] = CellLocation(x = 0, y = 0)
                    transitionLocations[1] = CellLocation(x = cellSize, y = cellSize)
                }
            }

            TransitionTo.NONE -> {
                transitionLocations[0] = endCellLocation
                transitionLocations[1] = endCellLocation
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