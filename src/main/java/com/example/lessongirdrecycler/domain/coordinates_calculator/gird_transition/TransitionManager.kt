package com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition

import com.example.lessongirdrecycler.domain.models.cell.CellLocation

class TransitionManager(val cellSize: Int) {

    fun getTransitionTo(coordinatesInCell: CellLocation, relativeCoordinatesNext:CellLocation): TransitionTo {
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