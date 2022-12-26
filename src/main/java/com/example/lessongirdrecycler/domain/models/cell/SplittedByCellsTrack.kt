package com.example.lessongirdrecycler.domain.models.cell

class SplittedByCellsTrack(val trackId: Int) {
    var cellData: MutableMap<CellCoordinates, CellTrack>? = null,
    var currentCell: CellCoordinates? = null

    fun cell (cellCoordinates: CellCoordinates) {
        currentCell = cellCoordinates
    }

    fun addLocation (cellLocation: CellLocation) {
        if (cellData == null) {
            cellData = mutableMapOf()//MutableMap<>(currentCell, CellTrack(id = trackId, turnPoints = mutableListOf()))
        }

        if (cellData != null) {
            if (cellData.containsKey(currentCell))
        }
        cellData[]
    }
}