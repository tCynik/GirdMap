package com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition

import com.example.lessongirdrecycler.domain.models.CellNumber

class TransitionManager {
    //fun whichTransition
}

class TransitionDetector {
    fun execute(currentCell: CellNumber, nextCell: CellNumber) : EnumTransitionTo {
        val deltaX = currentCell.x - nextCell.x
        val deltaY = currentCell.y - nextCell.y

        val noVerticalTransition = (deltaX == 0)
        val noHorizontalTransition = (deltaY == 0)

        return if (noHorizontalTransition && noVerticalTransition) EnumTransitionTo.NONE
        else {
            if (!noHorizontalTransition && !noVerticalTransition) diagonalTransition(deltaX, deltaY)
            else if (noHorizontalTransition) verticalTransition(deltaY)
            else horizontalTransition(deltaX)
        }
    }

    private fun diagonalTransition(deltaX: Int, deltaY: Int) : EnumTransitionTo {
        return if (deltaX > 0) {
            if (deltaY > 0) EnumTransitionTo.SE
            else EnumTransitionTo.NE
        }
        else if (deltaY > 0) EnumTransitionTo.SW
            else EnumTransitionTo.NW
    }

    private fun verticalTransition(deltaY: Int) : EnumTransitionTo {
        return if (deltaY > 0) EnumTransitionTo.SOUTH
        else EnumTransitionTo.NORTH
    }

    private fun horizontalTransition(deltaX: Int) : EnumTransitionTo {
        return if (deltaX > 0) EnumTransitionTo.EAST
        else EnumTransitionTo.WEST
    }
}