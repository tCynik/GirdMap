package com.example.lessongirdrecycler.domain.models.cell

data class CellTrack(val id: Int, val firstTurnPoint: CellLocation){//val turnPoints: MutableList<CellLocation>) {
    val turnPoints: MutableList<CellLocation> = mutableListOf()
    init{
        turnPoints.add(firstTurnPoint)
    }

    fun addLocation(cellLocation: CellLocation) {
        turnPoints.add(cellLocation)
    }
}