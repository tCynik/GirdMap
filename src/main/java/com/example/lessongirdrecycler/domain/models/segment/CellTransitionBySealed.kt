package com.example.lessongirdrecycler.domain.models.segment

import com.example.lessongirdrecycler.domain.models.cell.CellLocation

// TODO: when implement into project need to test maths & logics by jUnit!!!
sealed class CellTransitionBySealed (val cellSize: Int){
    abstract fun getAngleFromStartToCorner(startCellLocation: CellLocation): Int

    private fun calculateByX(startCellLocation: CellLocation, endSegmentLocation: CellLocation): Int {
        return ((startCellLocation.y/endSegmentLocation.y)*(endSegmentLocation.x)) + startCellLocation.x
    }

    private fun calculateByY(startCellLocation: CellLocation, endSegmentLocation: CellLocation): Int {
        return ((startCellLocation.x/endSegmentLocation.x)*(endSegmentLocation.y)) + startCellLocation.y
    }

    class North(cellSize: Int)
        : CellTransitionBySealed(cellSize) {
        override fun getAngleFromStartToCorner(startCellLocation: CellLocation): Int {
            return 0
        }
    }

    class NE(cellSize: Int)
        : CellTransitionBySealed(cellSize) {
        override fun getAngleFromStartToCorner(startCellLocation: CellLocation): Int {
            return (cellSize - startCellLocation.x)/startCellLocation.y
        }
    }

    class East(cellSize: Int)
        : CellTransitionBySealed(cellSize) {
        override fun getAngleFromStartToCorner(startCellLocation: CellLocation): Int {
            return 0
        }
    }

    class SE(cellSize: Int)
        : CellTransitionBySealed(cellSize) {
        override fun getAngleFromStartToCorner(startCellLocation: CellLocation): Int {
            return (cellSize - startCellLocation.x)/(cellSize - startCellLocation.y)
        }
    }

    class South(cellSize: Int)
        : CellTransitionBySealed(cellSize) {
        override fun getAngleFromStartToCorner(startCellLocation: CellLocation): Int {
            return 0
        }
    }

    class SW(cellSize: Int)
        : CellTransitionBySealed(cellSize) {
        override fun getAngleFromStartToCorner(startCellLocation: CellLocation): Int {
            return startCellLocation.x/(cellSize - startCellLocation.y)
        }
    }

    class West(cellSize: Int)
        : CellTransitionBySealed(cellSize) {
        override fun getAngleFromStartToCorner(startCellLocation: CellLocation): Int {
            return 0
        }
    }

    class NW(cellSize: Int)
        : CellTransitionBySealed(cellSize) {
        override fun getAngleFromStartToCorner(startCellLocation: CellLocation): Int {
            return startCellLocation.x/startCellLocation.y
        }
    }

    fun getTransitionLocations(cellTransition: CellTransitionBySealed,
                               startCellLocation: CellLocation,
                               endCellLocation: CellLocation): List<CellLocation> {

        val endSegmentLocation = CellLocation(
            x = endCellLocation.x - startCellLocation.x,
            y = endCellLocation.y - startCellLocation.y)
        val rateToSegmentEnd = endSegmentLocation.x/endSegmentLocation.y
        val transitionLocations = mutableListOf<CellLocation>()
        when (cellTransition) {
            is North -> {
                transitionLocations[0] = CellLocation(x = endCellLocation.x, y = 0)
                transitionLocations[1] = CellLocation(x = endCellLocation.x, y = cellSize)
            }
            is NE -> {
                val rateToCellCorner = getAngleFromStartToCorner(startCellLocation)
                if (rateToSegmentEnd < rateToCellCorner) { // ?????????? ?????????????? ?????????????? -> ?????????????? ???? Y
                    // ?????????????? ???????????????? ?????????????? (?????? ?????? ??) ?????????? ?????????????????? ???????????????? ??????????????????????????
                    // ?????????????? ???????????????? ???? ???????????????? ?????????????? = ?????????????? ?????? ???????? + ?????????????? ???????? ????????????: ?? = ???????? + ????????
                    // ?????????????? ???????? ???????????? = ?????????????? * ??????????????????: X?????? = ?? * X??????
                    // ?????????????????? = "?????????????? ??????????"/"?????????????? ???????????????? ???? ??????????????": ?? = Y?????? / Y??????
                    // ??????????: ?? = ???????? + ???????? * (Y?????? / Y??????)
                    transitionLocations[0] = CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = 0)
                    transitionLocations[1] = CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = cellSize)
                } else if (rateToSegmentEnd > rateToCellCorner) { // ?????????? ???????????? ??????????????
                    transitionLocations[0] = CellLocation(x = cellSize, y = calculateByY(startCellLocation, endSegmentLocation))
                    transitionLocations[1] = CellLocation(x = 0, y = calculateByY(startCellLocation, endSegmentLocation))
                } else { // ?????????? ????????
                    transitionLocations[0] = CellLocation(x = cellSize, y = 0)
                    transitionLocations[1] = CellLocation(x = 0, y = 0)
                }
            }
            is East -> {
                transitionLocations[0] = CellLocation(x = cellSize, y = endCellLocation.y)
                transitionLocations[1] = CellLocation(x = 0, y = endCellLocation.y)
            }
            is SE -> {
                val rateToCellCorner = getAngleFromStartToCorner(startCellLocation)
                if (rateToSegmentEnd < rateToCellCorner) { // ?????????? ?????? -> ?????????????? Y
                    transitionLocations[0] = CellLocation(x = cellSize, y = calculateByY(startCellLocation, endSegmentLocation))
                    transitionLocations[1] = CellLocation(x = 0, y = calculateByY(startCellLocation, endSegmentLocation))
                } else if (rateToSegmentEnd > rateToCellCorner) { // ?????????? ??????, ?????????????? ??
                    transitionLocations[0] = CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = cellSize)
                    transitionLocations[1] = CellLocation(x = calculateByX(startCellLocation, endSegmentLocation), y = 0)
                } else { // ???????? ??????????, ???????? ???????? ?????????? ??????????
                    transitionLocations[0] = CellLocation(x = cellSize, y = cellSize)
                    transitionLocations[1] = CellLocation(x = 0, y = 0)
                }
            }
            is South -> {
                transitionLocations[0] = CellLocation(x = endCellLocation.x, y = cellSize)
                transitionLocations[1] = CellLocation(x = endCellLocation.x, y = 0)
            }
            is SW -> {
                val rateToCellCorner = getAngleFromStartToCorner(startCellLocation)
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
            is West -> {
                transitionLocations[0] = CellLocation(x = 0, y = endCellLocation.y)
                transitionLocations[1] = CellLocation(x = cellSize, y = endCellLocation.y)
            }
            is NW -> {
                val rateToCellCorner = getAngleFromStartToCorner(startCellLocation)
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
        }
        return transitionLocations
    }
}