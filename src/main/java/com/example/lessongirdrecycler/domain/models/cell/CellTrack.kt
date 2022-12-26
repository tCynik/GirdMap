package com.example.lessongirdrecycler.domain.models.cell

data class CellTrack(val id: Int, val turnPoints: MutableList<CellLocation>) {
    fun addLocation(cellLocation: CellLocation) {
        turnPoints.add(cellLocation)
    }
}