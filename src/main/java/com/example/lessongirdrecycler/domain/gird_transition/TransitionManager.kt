package com.example.lessongirdrecycler.domain.gird_transition

import com.example.lessongirdrecycler.domain.models.CellNumber

class TransitionManager {
    fun whichTransition
}

class TransitionDetector {
    fun execute(currentCell: CellNumber, nextCell: CellNumber) : TransitionTo {
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