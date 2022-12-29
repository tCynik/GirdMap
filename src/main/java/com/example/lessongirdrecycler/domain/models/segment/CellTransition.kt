package com.example.lessongirdrecycler.domain.models.segment

import com.example.lessongirdrecycler.domain.models.cell.CellLocation

sealed class CellTransition (val cellSize: Int, val startCellLocation: CellLocation, endSegmentLocation: CellLocation){
    abstract fun getAngleFromStartToCorner(): Int
    class North(cellSize: Int, startCellLocation: CellLocation, endSegmentLocation: CellLocation)
        : CellTransition(cellSize, startCellLocation, endSegmentLocation) {
        override fun getAngleFromStartToCorner(): Int {
            return 0
        }
    }

    class NE(cellSize: Int, startCellLocation: CellLocation, endSegmentLocation: CellLocation)
        : CellTransition(cellSize, startCellLocation, endSegmentLocation) {
        override fun getAngleFromStartToCorner(): Int {
            return (cellSize - startCellLocation.x)/startCellLocation.y
        }
    }

    class East(cellSize: Int,
                startCellLocation: CellLocation,
                endSegmentLocation: CellLocation)
        : CellTransition(cellSize, startCellLocation, endSegmentLocation) {
        override fun getAngleFromStartToCorner(): Int {
            return 0
        }
    }

    class SE(cellSize: Int,
                startCellLocation: CellLocation,
                endSegmentLocation: CellLocation)
        : CellTransition(cellSize, startCellLocation, endSegmentLocation) {
        override fun getAngleFromStartToCorner(): Int {
            return (cellSize - startCellLocation.x)/(cellSize - startCellLocation.y)
        }
    }

    class South(cellSize: Int,
                startCellLocation: CellLocation,
                endSegmentLocation: CellLocation)
        : CellTransition(cellSize, startCellLocation, endSegmentLocation) {
        override fun getAngleFromStartToCorner(): Int {
            return 0
        }
    }

    class SW(cellSize: Int,
                startCellLocation: CellLocation,
                endSegmentLocation: CellLocation)
        : CellTransition(cellSize, startCellLocation, endSegmentLocation) {
        override fun getAngleFromStartToCorner(): Int {
            return startCellLocation.x/(cellSize - startCellLocation.y)
        }
    }

    class West(cellSize: Int,
                startCellLocation: CellLocation,
                endSegmentLocation: CellLocation)
        : CellTransition(cellSize, startCellLocation, endSegmentLocation) {
        override fun getAngleFromStartToCorner(): Int {
            return 0
        }
    }

    class NW(cellSize: Int,
                startCellLocation: CellLocation,
                endSegmentLocation: CellLocation)
        : CellTransition(cellSize, startCellLocation, endSegmentLocation) {
        override fun getAngleFromStartToCorner(): Int {
            return startCellLocation.x/startCellLocation.y
        }
    }

    fun getTransitionLocations(cellTransition: CellTransition, startSegmentCellLocation, endSegmentCellLocation): List<CellLocation> {
        val endSegmentLocation = CellLocation(
            x = endSegmentCellLocation.x - startCellLocation.x,
            y = endSegmentCellLocation.y - startCellLocation.y)
        var endX: Int = 0
        var endY: Int = 0
        var nextStartX: Int = 0
        var nextStartY: Int = 0
        val rateToSegmentEnd = endSegmentLocation.x/endSegmentLocation.y
        when (cellTransition) {
            is North -> {
                endX = endSegmentCellLocation.x
                endY = 0
                nextStartX = endX
                nextStartY = cellSize
            }
            is NE -> {
                val rateToCellCorner = getAngleFromStartToCorner()
                if (rateToSegmentEnd < rateToCellCorner) { // через верхнюю сторону
                    // находим неполную сторону (тут это Х) через пропорцию
                    // пропорция = "сколько всего"/"сколько осталось до границы"
                    // сколько надо пройти = остаток * пропорция
                    // сколько осталось от неполной стороны = сколько уже есть + сколько надо пройти
                    endX = endSegmentLocation.y/startCellLocation.y*(endSegmentLocation.x) + startCellLocation.x
                    endY = 0
                    nextStartX = endX
                    nextStartY = cellSize
                } else if (rateToSegmentEnd > rateToCellCorner) { // через правую сторону
                    endX = cellSize
                    endY = endSegmentLocation.x/startCellLocation.x*(endSegmentLocation.y) + startCellLocation.y
                } else { // через угол
                    endX = cellSize
                    endY = 0
                    nextStartX = 0
                    nextStartY = 0
                }
            }
            is East -> {
                endX = cellSize
                endY = endSegmentCellLocation.y
            }
            is SE -> {
                val rateToCellCorner = getAngleFromStartToCorner()
                if (rateToSegmentEnd < rateToCellCorner) { // calculating y

                } else if (rateToSegmentEnd > rateToCellCorner) {

                }
            }
            is South -> {
                endX = endSegmentCellLocation.x
                endY = cellSize
            }
            is SW -> {
                val rateToCellCorner = getAngleFromStartToCorner()
                if (rateToSegmentEnd < rateToCellCorner) { // calculating y

                } else if (rateToSegmentEnd > rateToCellCorner) {

                }
            }
            is West -> {
                endX = 0
                endY = endSegmentCellLocation.y
            }
            is NW -> {
                val rateToCellCorner = getAngleFromStartToCorner()
                if (rateToSegmentEnd < rateToCellCorner) { // calculating y

                }else if (rateToSegmentEnd > rateToCellCorner) {
                    
                }
            }
        }
        var transitionLocations = mutableListOf<CellLocation>()
        transitionLocations[0] = CellLocation(x = endX, y = endY)
        transitionLocations[1] = CellLocation(x = nextStartX, y = nextStartY)
        return transitionLocations
    }
}