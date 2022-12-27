package com.example.lessongirdrecycler.domain.coordinates_calculator

import com.example.lessongirdrecycler.domain.models.cell.CoordinatesOfCell
import com.example.lessongirdrecycler.domain.models.cell.CellTrack
import com.example.lessongirdrecycler.domain.models.cell.CellLocation
import com.example.lessongirdrecycler.models.Track

abstract class CalculatorStructure {
    fun distributeTrackToCells(track: Track): Map<CoordinatesOfCell, CellTrack> {
        for (turnPoint in track.turnPoints) {

        }
    }

    fun pointToCellNumber(localCoordinates: CellLocation):
}