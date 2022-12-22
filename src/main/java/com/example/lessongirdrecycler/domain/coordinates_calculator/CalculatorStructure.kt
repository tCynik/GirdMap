package com.example.lessongirdrecycler.domain.coordinates_calculator

import com.example.lessongirdrecycler.domain.models.GirdTrack
import com.example.lessongirdrecycler.domain.models.cell.CellCoordinates
import com.example.lessongirdrecycler.domain.models.local.LocalLocation
import java.util.ArrayList

abstract class CalculatorStructure {
    fun distributeTrackToCells(track: Track): Map<CellCoordinates, GirdTrack> {

    }

    abstract fun pointToCellNumber(localCoordinates: LocalLocation): 
}