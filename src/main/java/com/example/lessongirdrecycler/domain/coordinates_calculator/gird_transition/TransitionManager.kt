package com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition

import com.example.lessongirdrecycler.domain.models.cell.CoordinatesOfCell
import com.example.lessongirdrecycler.domain.models.cell.CellLocation

class TransitionManager(val cellSize: Int) {
    fun getTransition(coordinatesInCell: CellLocation, relativeCoordinatesNext:CellLocation): TransitionTo {
        val relativeXFromTopStart = coordinatesInCell.x + relativeCoordinatesNext.x
        val relativeYFromTopStart = coordinatesInCell.y + relativeCoordinatesNext.y

        if (relativeXFromTopStart < 0) { // ГОРИЗОНТАЛЬНОЕ смещение влево...
            if (relativeYFromTopStart < 0) return TransitionTo.NW // и ВЕРТИКАЛЬНОЕ вверх
            else if (relativeYFromTopStart > cellSize) return TransitionTo.SW // и ВЕРТИКАЛЬНОЕ вниз
            else return TransitionTo.WEST // только влево, и всё
        }
        else if (relativeXFromTopStart > cellSize) {// ГОРИЗОНТАЛЬНОЕ смещение вправо...
            if (relativeYFromTopStart < 0) return TransitionTo.NE // и ВЕРТИКАЛЬНОЕ вверх
            else if (relativeYFromTopStart > cellSize) return TransitionTo.SE // и ВЕРТИКАЛЬНОЕ вниз
            else return TransitionTo.EAST // либо только вправо
        }
        else { // тибо ГОРИЗОНТАЛЬНОГО смещения нет, и
            if (relativeYFromTopStart < 0) return TransitionTo.NORTH // ВЕРТИКАЛЬНОЕ вверх
            else if (relativeYFromTopStart > cellSize) return TransitionTo.SOUTH // ВЕРТИКАЛЬНОЕ вниз
            else return TransitionTo.NONE // вертикального тоже нет
        }
    }
}
// todo: cleanup
class TransitionDetector {
    fun execute(currentCell: CoordinatesOfCell, nextCell: CoordinatesOfCell) : TransitionTo {
        val deltaX = currentCell.x - nextCell.x
        val deltaY = currentCell.y - nextCell.y

        val noVerticalTransition = (deltaX == 0)
        val noHorizontalTransition = (deltaY == 0)

        return if (noHorizontalTransition && noVerticalTransition) TransitionTo.NONE
        else {
            if (!noHorizontalTransition && !noVerticalTransition) diagonalTransition(deltaX, deltaY)
            else if (noHorizontalTransition) verticalTransition(deltaY)
            else horizontalTransition(deltaX)
        }
    }

    private fun diagonalTransition(deltaX: Int, deltaY: Int) : TransitionTo {
        return if (deltaX > 0) {
            if (deltaY > 0) TransitionTo.SE
            else TransitionTo.NE
        }
        else if (deltaY > 0) TransitionTo.SW
            else TransitionTo.NW
    }

    private fun verticalTransition(deltaY: Int) : TransitionTo {
        return if (deltaY > 0) TransitionTo.SOUTH
        else TransitionTo.NORTH
    }

    private fun horizontalTransition(deltaX: Int) : TransitionTo {
        return if (deltaX > 0) TransitionTo.EAST
        else TransitionTo.WEST
    }
}