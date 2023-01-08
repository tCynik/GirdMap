package com.example.lessongirdrecycler.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lessongirdrecycler.domain.coordinates_calculator.TracksGirdCalculator
import com.example.lessongirdrecycler.models.CellTracks
import com.example.lessongirdrecycler.domain.models.global.GlobalTrack
import com.example.lessongirdrecycler.models.TracksPack

class MainViewModel: ViewModel() {
    val cellSize = 100
    val cells: List<List<CellTracks>> = mutableListOf()
    lateinit var trackGirdCalculator: TracksGirdCalculator

    val tracksAndCellsLive = TracksPack()
    
    fun showTheTrack(trackToShow: GlobalTrack) {
        val splittedTrack = trackGirdCalculator.splitTrackToCells(trackToShow)
        tracksAndCellsLive.addSplittedTrack(splittedTrack)
    }

    fun updateTracks (track: GlobalTrack) {
        val cells = trackGirdCalculator.splitTrackToCells(track)
    }
}