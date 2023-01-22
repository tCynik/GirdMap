package com.example.lessongirdrecycler.domain.models.cell

class SplittedByCellsTrack() {
    var cellData = mutableListOf<TrackPartInSingleCell>() // todo: make just the list with no models!

    fun addLocation (coordinatesOfCell: CoordinatesOfCell, cellLocation: CellLocation) {
        var currentPart = cellData[cellData.size - 1]
        if (cellData.size == 0) currentPart = 1
        if (coordinatesOfCell != currentPart.cell) { // это новая ячейка, добавляем
            cellData.add(TrackPartInSingleCell(cell = coordinatesOfCell, track = mutableListOf()))
            cellData[cellData.size].addPointToTrack(cellLocation)
        } else {// действуем в рамках старой ячейки, добавляем точку туда
            currentPart.track.add(cellLocation)
        }
    }
}