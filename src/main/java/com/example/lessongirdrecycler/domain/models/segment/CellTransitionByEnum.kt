package com.example.lessongirdrecycler.domain.models.segment

import com.example.lessongirdrecycler.domain.Logger
import com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition.TransitionTo
import com.example.lessongirdrecycler.domain.models.cell.CellLocation
import kotlin.math.abs

class CellTransitionByEnum(val cellSize: Int, val logger: Logger) {
    fun getTransitionPoints(
        transitionTo: TransitionTo,
        startCellLocation: CellLocation,
        endCellLocation: CellLocation): List<CellLocation> {
        val transitionLocations = mutableListOf<CellLocation>() // [0] - current cell, [1] - next cell
        val endSegmentLocation = CellLocation(
            x = endCellLocation.x - startCellLocation.x,
            y = endCellLocation.y - startCellLocation.y)

        when (transitionTo) {

            TransitionTo.NORTH -> {
                transitionLocations.add(CellLocation(x = endCellLocation.x, y = 0))
                transitionLocations.add(CellLocation(x = endCellLocation.x, y = cellSize))
            }

            TransitionTo.NE -> {
                if (startCellLocation.x == cellSize) {
                    transitionLocations.add(startCellLocation)
                    if (startCellLocation.y == 0)
                        transitionLocations.add(CellLocation(x = cellSize - startCellLocation.x, y = cellSize-startCellLocation.y))
                    else
                        transitionLocations.add(CellLocation(x = cellSize - startCellLocation.x, y = startCellLocation.y))
                } else if(startCellLocation.y == 0) {
                    transitionLocations.add(startCellLocation)
                    transitionLocations.add(CellLocation(x = cellSize-startCellLocation.x, y = cellSize-startCellLocation.y))
                }
                else {
                    logger.printLog("calculating NE. start = $startCellLocation, end = $endSegmentLocation")
                    val rateToSegmentEnd = abs(endSegmentLocation.x*100/endSegmentLocation.y).toDouble()/100
                    val rateToCellCorner = abs(cellSize - startCellLocation.x)*100/startCellLocation.y.toDouble()/100
                    logger.printLog("rateToSegmentEnd = $rateToSegmentEnd, rateToCellCorner = $rateToCellCorner, rate is less = ${rateToSegmentEnd > rateToCellCorner}")
                    if (rateToSegmentEnd < rateToCellCorner) { // через верхнюю сторону -> считаем от Y
                        // находим неполную сторону (тут это Х) через пропорцию подобных треугольников
                        // сколько осталось от неполной стороны = сколько уже есть + сколько надо пройти: Х = Хнач + Хост
                        // сколько надо пройти = остаток * пропорция: Xост = К * Xвсг
                        // пропорция = "сколько всего"/"сколько осталось до границы": К = Yост / Yвсг
                        // итого: Х = Хнач + Хвсг * (Yост / Yвсг)
                        val calculatedX = calculateByX(startCellLocation = startCellLocation, endSegmentLocation = endSegmentLocation) + startCellLocation.x
                        logger.printLog("calc by top. start = $startCellLocation, end = $endSegmentLocation, calcByX  = $calculatedX")
                        transitionLocations.add(CellLocation(x = calculatedX, y = 0))
                        transitionLocations.add(CellLocation(x = calculatedX, y = cellSize))
                    } else if (rateToSegmentEnd > rateToCellCorner) { // через правую сторону
                        val calculatedY = calculateByY(startCellLocation, endSegmentLocation)
                        logger.printLog("calc by right. start = $startCellLocation, end = $endSegmentLocation, calcByY = $calculatedY")
                        transitionLocations.add(CellLocation(x = cellSize, y = calculatedY))
                        transitionLocations.add(CellLocation(x = 0, y = calculatedY))
                    } else { // через угол
                        transitionLocations.add(CellLocation(x = cellSize, y = 0))
                        transitionLocations.add(CellLocation(x = 0, y = 0))
                    }
                }

            }

            TransitionTo.EAST -> {
                transitionLocations.add(CellLocation(x = cellSize, y = endCellLocation.y))
                transitionLocations.add(CellLocation(x = 0, y = endCellLocation.y))
            }

            TransitionTo.SE -> {
                val currentTransitionLocations = fromSideCase(startLocation = startCellLocation, sideX = cellSize, sideY = cellSize)
                if (currentTransitionLocations.isNotEmpty()) {
                    currentTransitionLocations.forEach { location -> transitionLocations.add(location)}
                } else {
                    val rateToSegmentEnd = endSegmentLocation.x/endSegmentLocation.y
                    val rateToCellCorner = (cellSize - startCellLocation.x)/(cellSize - startCellLocation.y)
                    if (rateToSegmentEnd > rateToCellCorner) { // через бок -> считаем Y
                        transitionLocations.add(CellLocation(x = cellSize, y = calculateByY(startCellLocation, endSegmentLocation)))
                        transitionLocations.add(CellLocation(x = 0, y = calculateByY(startCellLocation, endSegmentLocation)))
                    } else if (rateToSegmentEnd < rateToCellCorner) { // через низ, считаем Х
                        val calculatedX = calculateByX(startCellLocation, endSegmentLocation)
                        transitionLocations.add(CellLocation(x = calculatedX, y = cellSize))
                        transitionLocations.add(CellLocation(x = calculatedX, y = 0))
                    } else { // углы равны, трек идет через грань
                        transitionLocations.add(CellLocation(x = cellSize, y = cellSize))
                        transitionLocations.add(CellLocation(x = 0, y = 0))
                    }
                }
            }

            TransitionTo.SOUTH -> {
                transitionLocations.add(CellLocation(x = endCellLocation.x, y = cellSize))
                transitionLocations.add(CellLocation(x = endCellLocation.x, y = 0))
            }

            TransitionTo.SW -> {
                    val currentTransitionLocations = fromSideCase(startLocation = startCellLocation, sideX = 0, sideY = cellSize)
                    if (currentTransitionLocations.isNotEmpty()) {
                        currentTransitionLocations.forEach { location -> transitionLocations.add(location)}

                    } else {
                    val rateToSegmentEnd = abs(endSegmentLocation.x*100/endSegmentLocation.y/100)
                    val rateToCellCorner = abs(startCellLocation.x*100/(cellSize - startCellLocation.y)/100)

                    if (rateToSegmentEnd > rateToCellCorner) { // calculating y
                        val calculatedY = calculateByY(startCellLocation, endSegmentLocation) + startCellLocation.y
                        transitionLocations.add(CellLocation(x = 0, y = calculatedY))
                        transitionLocations.add(CellLocation(x = cellSize, y = calculatedY))
                    } else if (rateToSegmentEnd < rateToCellCorner) { // calculating X
                        val calculatedX = calculateByX(startCellLocation, endSegmentLocation)
                        transitionLocations.add(CellLocation(x = calculatedX, y = cellSize))
                        transitionLocations.add(CellLocation(x = calculatedX, y = 0))
                    } else {
                        transitionLocations.add(CellLocation(x = 0, y = cellSize))
                        transitionLocations.add(CellLocation(x = cellSize, y = 0))
                    }
                }
            }

            TransitionTo.WEST -> {
                transitionLocations.add(CellLocation(x = 0, y = endCellLocation.y))
                transitionLocations.add(CellLocation(x = cellSize, y = endCellLocation.y))
            }

            TransitionTo.NW -> {
                val currentTransitionLocations = fromSideCase(startLocation = startCellLocation, sideX = 0, sideY = 0)
                if (currentTransitionLocations.isNotEmpty()) {
                    currentTransitionLocations.forEach { location -> transitionLocations.add(location)}
                } else {
                    val rateToSegmentEnd = endSegmentLocation.x/endSegmentLocation.y
                    val rateToCellCorner = startCellLocation.x/startCellLocation.y
                    if (rateToSegmentEnd < rateToCellCorner) { // calculating X
                        val calculatedX = calculateNWByY(startCellLocation, endSegmentLocation) //+ startCellLocation.y
                        transitionLocations.add(CellLocation(x = calculatedX, y = 0))
                        transitionLocations.add(CellLocation(x = calculatedX, y = cellSize))
                    }else if (rateToSegmentEnd > rateToCellCorner) { // calculating by X
                        val calculatedY = calculateNWByX(startCellLocation, endSegmentLocation) // + startCellLocation.x
                        transitionLocations.add(CellLocation(x = 0, y = calculatedY))
                        transitionLocations.add(CellLocation(x = cellSize, y = calculatedY))
                    } else { // transition by corner
                        transitionLocations.add(CellLocation(x = 0, y = 0))
                        transitionLocations.add(CellLocation(x = cellSize, y = cellSize))
                    }
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
        val result = (startCellLocation.y*endSegmentLocation.x/endSegmentLocation.y) + startCellLocation.x
        //logger.printLog("calc by X = (start.y'${startCellLocation.y}' * endX'${endSegmentLocation.x}' / endY'${endSegmentLocation.y}') + startX'${startCellLocation.x}' = $result")
        return (startCellLocation.y*endSegmentLocation.x/endSegmentLocation.y) + startCellLocation.x
    }

    private fun calculateByY(startCellLocation: CellLocation, endSegmentLocation: CellLocation): Int {
        val result = abs(startCellLocation.x*endSegmentLocation.y/endSegmentLocation.x) + startCellLocation.y
        //logger.printLog("calc by Y = (startX'${startCellLocation.x}' * endY'${endSegmentLocation.y}' / endX'${endSegmentLocation.x}') + startY'${startCellLocation.y}' = $result")
        return (startCellLocation.x*endSegmentLocation.y/endSegmentLocation.x) + startCellLocation.y
    }

    private fun calculateNWByX(startCellLocation: CellLocation, endSegmentLocation: CellLocation): Int {
        val result = (startCellLocation.y*endSegmentLocation.y/endSegmentLocation.x)
        //logger.printLog("calc by X = (start.y'${startCellLocation.y}' * endX'${endSegmentLocation.x}' / endY'${endSegmentLocation.y}') + startX'${startCellLocation.x}' = $result")
        return result
    }

    private fun calculateNWByY(startCellLocation: CellLocation, endSegmentLocation: CellLocation): Int {
        val result = abs(startCellLocation.x*startCellLocation.y/endSegmentLocation.y)
        //logger.printLog("calcNW by Y = (startX'${startCellLocation.x}' * startY'${startCellLocation.y}' / endY'${endSegmentLocation.y}') + startY'${startCellLocation.y}' = $result")
        return result
    }

    private fun fromSideCase(startLocation: CellLocation, sideX: Int, sideY: Int): MutableList<CellLocation> {
        val transitionLocations = mutableListOf<CellLocation>()
        if (startLocation.x == sideX) {
            transitionLocations.add(startLocation)
            if (startLocation.y == sideY)
                transitionLocations.add(CellLocation(x = cellSize - startLocation.x, y = cellSize - startLocation.y))
            else
                transitionLocations.add(CellLocation(x = cellSize - startLocation.x, y = startLocation.y))
        } else if (startLocation.y == sideY) {
            transitionLocations.add(startLocation)
            transitionLocations.add(CellLocation(x = startLocation.x, y = cellSize - startLocation.y))
        }
        //logger.printLog("result x = ${transitionLocations[0]}, result y = ${transitionLocations[1]}")
        return transitionLocations
    }
}