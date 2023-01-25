package com.example.lessongirdrecycler.domain.models.cell

class SplittedByCellsTrack() {
    var cellData = mutableListOf<TrackPartInSingleCell>() // todo: make just the list with no models!

    fun addLocation (coordinatesOfCell: CoordinatesOfCell, cellLocation: CellLocation) {
        if (cellData.size == 0) { // обработка создания первого трека в последовательности ячеек
            var firstPart = TrackPartInSingleCell(
                cell = coordinatesOfCell,
                track = mutableListOf(cellLocation))
            cellData.add(firstPart)
        }
        else {
            var currentPart = cellData[cellData.size - 1]

            if (coordinatesOfCell != currentPart.cell) { // это новая ячейка, добавляем элемент списка
                cellData.add(TrackPartInSingleCell(cell = coordinatesOfCell, track = mutableListOf()))
                cellData[cellData.size-1].addPointToTrack(cellLocation)
            } else {// действуем в рамках старой ячейки, добавляем точку в имеющийся трек
                cellData[cellData.size-1].addPointToTrack(cellLocation)
            }
        }
    }

    fun getCell(index: Int): CoordinatesOfCell {
        return cellData[index].cell
    }

    fun getTrack(index: Int): MutableList<CellLocation> {
        return  cellData[index].track
    }
}