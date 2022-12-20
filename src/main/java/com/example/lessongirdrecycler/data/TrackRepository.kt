package com.example.lessongirdrecycler.data

import com.example.lessongirdrecycler.models.Track
import com.example.lessongirdrecycler.models.TurnPoint

class TrackRepository {
    fun loadNextTrack(): Track {
        var myTrack = Track("my track", mutableListOf<TurnPoint>())
        myTrack.addTurnPoint(TurnPoint(0, 0))
        var maxLat = 0
        var maxLon = 0
        while (maxLat<2000) {
            val stepX = (0..100).random()
            maxLon += stepX
            val stepY = (0..100).random()
            maxLat += stepY
            myTrack.addTurnPoint(TurnPoint(latitude = maxLat, longitude =  maxLon))
        }
        return myTrack
    }
}