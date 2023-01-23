package com.example.lessongirdrecycler.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lessongirdrecycler.domain.coordinates_calculator.TracksGirdCalculator
import com.example.lessongirdrecycler.models.CellTracks
import com.example.lessongirdrecycler.domain.models.global.GlobalTrack
import com.example.lessongirdrecycler.domain.models.global.GlobalTurnPoint
import com.example.lessongirdrecycler.models.TracksPack

class MainViewModel: ViewModel() {
    val cellSize = 100
    val cells: List<List<CellTracks>> = mutableListOf()
    var trackGirdCalculator: TracksGirdCalculator? = null // not initilasing until get any first location

    val tracksAndCellsLive = MutableLiveData<TracksPack>()

    fun showTheTrack(trackToShow: GlobalTrack) {
//        val splittedTrack = trackGirdCalculator.splitTrackToCells(trackToShow)
//        tracksAndCellsLive.value!!.addSplittedTrack(splittedTrack)
    }

    fun updateTracks (track: GlobalTrack) {
        val splittedTrack = calculatorWithInitChecking(startPoint = track.turnPoints[0]).splitGlobalTrackToCells(track)
        tracksAndCellsLive.value!!.addSplittedTrack(splittedTrack)
    }

    private fun calculatorWithInitChecking(startPoint: GlobalTurnPoint): TracksGirdCalculator {
        if (trackGirdCalculator != null) return trackGirdCalculator!!
        else {
            trackGirdCalculator = TracksGirdCalculator(
                girdStartX = startPoint.latitude.toDouble(),  // todo: when migrating to location remove toDouble() cast
                girdStartY = startPoint.longitude.toDouble(),
                cellSize = cellSize)
            return trackGirdCalculator!!
        }
    }
}