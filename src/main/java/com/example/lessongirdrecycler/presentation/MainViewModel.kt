package com.example.lessongirdrecycler.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lessongirdrecycler.domain.coordinates_calculator.TracksGirdCalculator
import com.example.lessongirdrecycler.domain.models.cell.CellTrack
import com.example.lessongirdrecycler.domain.models.cell.CoordinatesOfCell
import com.example.lessongirdrecycler.domain.models.cell.SplittedByCellsTrack
import com.example.lessongirdrecycler.models.CellTracks
import com.example.lessongirdrecycler.models.Track

class MainViewModel: ViewModel() {
    val cellSize = 100
    val cells: List<List<CellTracks>> = mutableListOf()
    lateinit var trackGirdCalculator: TracksGirdCalculator

    val tracksAndCellsLive = MutableLiveData<Map<CoordinatesOfCell, List<CellTrack>>>()

    fun updateTracks (track: Track) {
        val cells = trackGirdCalculator.splitTrackToCells(track)
    }
}