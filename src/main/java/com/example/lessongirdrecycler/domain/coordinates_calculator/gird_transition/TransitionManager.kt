package com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition

import com.example.lessongirdrecycler.domain.models.cell.CellLocation

class TransitionManager(private val cellSize: Int) {

    fun getTransitionTo(coordinatesInCell: CellLocation, relativeCoordinatesNext:CellLocation): TransitionTo {
        val relativeXFromTopStart = coordinatesInCell.x + relativeCoordinatesNext.x
        val relativeYFromTopStart = coordinatesInCell.y + relativeCoordinatesNext.y
        var transitionTo = TransitionTo.NONE

        if (relativeXFromTopStart < 0) transitionTo = TransitionTo.WEST
        else if (relativeXFromTopStart > cellSize) transitionTo = TransitionTo.EAST

        if (relativeYFromTopStart < 0)
            transitionTo = when (transitionTo) {
                TransitionTo.WEST -> TransitionTo.NW
                TransitionTo.EAST -> TransitionTo.NE
                else -> TransitionTo.NORTH
            }
        else if (relativeYFromTopStart > cellSize)
            transitionTo = when (transitionTo) {
                TransitionTo.WEST -> TransitionTo.SW
                TransitionTo.EAST -> TransitionTo.SE
                else -> TransitionTo.SOUTH
            }
        return transitionTo
    }
}