package com.example.lessongirdrecycler.data

import com.example.lessongirdrecycler.domain.models.global.GlobalTrack
import com.example.lessongirdrecycler.domain.models.global.GlobalTurnPoint

class TrackRepository {
    fun loadNextTrack(): GlobalTrack {
        var myTrack = GlobalTrack(id = 0, mutableListOf<GlobalTurnPoint>())
        myTrack.addTurnPoint(GlobalTurnPoint(0, 0))
        var maxLat = 0
        var maxLon = 0
        while (maxLat<2000) {
            val stepX = (0..100).random()
            maxLon += stepX
            val stepY = (0..100).random()
            maxLat += stepY
            myTrack.addTurnPoint(GlobalTurnPoint(latitude = maxLat, longitude =  maxLon))
        }
        return myTrack
    }
}