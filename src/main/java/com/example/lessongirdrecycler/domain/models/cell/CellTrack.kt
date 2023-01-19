package com.example.lessongirdrecycler.domain.models.cell

data class CellTrack(val id: Int, val turnPoints: MutableList<CellLocation>){
    // todo: add parameter - CoordinateCell

    fun addLocation(cellLocation: CellLocation) {
        turnPoints.add(cellLocation)
    }
}