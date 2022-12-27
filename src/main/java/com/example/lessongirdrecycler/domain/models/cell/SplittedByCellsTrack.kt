package com.example.lessongirdrecycler.domain.models.cell

class SplittedByCellsTrack(val trackId: Int) {
    private var cellData: MutableMap<CoordinatesOfCell, CellTrack> = mutableMapOf()

    fun addLocation (coordinatesOfCell: CoordinatesOfCell, cellLocation: CellLocation) {
        if (!cellData.containsKey(coordinatesOfCell)) {
            cellData.put(coordinatesOfCell, CellTrack(trackId, cellLocation))
            cellData[coordinatesOfCell]!!.addLocation(cellLocation)
        }
    }

    fun getCellData(): MutableMap<CoordinatesOfCell, CellTrack> {
        return cellData
    }
}