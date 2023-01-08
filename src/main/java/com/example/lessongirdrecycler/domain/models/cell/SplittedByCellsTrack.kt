package com.example.lessongirdrecycler.domain.models.cell

class SplittedByCellsTrack() {
    var cellData = mutableListOf<TrackPartInSingleCell>()

    fun addLocation (coordinatesOfCell: CoordinatesOfCell, cellLocation: CellLocation) {
        val currentPart = cellData[cellData.size]
        if (coordinatesOfCell != currentPart.cell) { // это новая ячейка, добавляем
            cellData.add(TrackPartInSingleCell(cell = coordinatesOfCell, track = mutableListOf()))
            cellData[cellData.size].addPointToTrack(cellLocation)
        } else {// действуем в рамках старой ячейки, добавляем точку туда
            currentPart.track.add(cellLocation)
        }
    }
}