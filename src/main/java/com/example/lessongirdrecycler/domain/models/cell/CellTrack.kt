package com.example.lessongirdrecycler.domain.models.cell

data class CellTrack(val id: Int, val turnPoints: MutableList<CellLocation>){//val turnPoints: MutableList<CellLocation>) {
    //val turnPoints: MutableList<CellLocation> = mutableListOf()

    fun addLocation(cellLocation: CellLocation) {
        turnPoints.add(cellLocation)
    }
}