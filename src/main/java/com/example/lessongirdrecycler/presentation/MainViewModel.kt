package com.example.lessongirdrecycler.presentation

import com.example.lessongirdrecycler.domain.coordinates_calculator.TracksGirdCalculator
import com.example.lessongirdrecycler.models.CellTracks
import com.example.lessongirdrecycler.models.Track

class MainViewModel {
    val cellSize = 100
    val cells: List<List<CellTracks>> = mutableListOf()
    val trackGirdCalculator = TracksGirdCalculator(cellSize)

    fun updateTracks (track: Track) {
        val cells = trackGirdCalculator.splitTrackToCells(track)
    }
}