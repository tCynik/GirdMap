package com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition

import com.example.lessongirdrecycler.domain.models.cell.CoordinatesOfCell

class NextCellNumber {
    fun get(cellNumber: CoordinatesOfCell,transitionTo: TransitionTo): CoordinatesOfCell {
        return when (transitionTo) {
            TransitionTo.NORTH -> { CoordinatesOfCell(cellNumber.x + 0, cellNumber.y - 1)}
            TransitionTo.NE ->    { CoordinatesOfCell(cellNumber.x + 1, cellNumber.y - 1) }
            TransitionTo.EAST ->  { CoordinatesOfCell(cellNumber.x + 1, cellNumber.y + 0) }
            TransitionTo.SE ->    { CoordinatesOfCell(cellNumber.x + 1, cellNumber.y + 1) }
            TransitionTo.SOUTH -> { CoordinatesOfCell(cellNumber.x + 0, cellNumber.y + 1) }
            TransitionTo.SW ->    { CoordinatesOfCell(cellNumber.x - 1, cellNumber.y + 1) }
            TransitionTo.WEST ->  { CoordinatesOfCell(cellNumber.x - 1, cellNumber.y + 0) }
            TransitionTo.NW ->    { CoordinatesOfCell(cellNumber.x - 1, cellNumber.y - 1) }
            TransitionTo.NONE ->  { CoordinatesOfCell(cellNumber.x + 0, cellNumber.y + 0) }
        }
    }
}
