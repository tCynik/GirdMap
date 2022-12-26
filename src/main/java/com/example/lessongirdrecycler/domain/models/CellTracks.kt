package com.example.lessongirdrecycler.domain.models

import com.example.lessongirdrecycler.domain.models.cell.CellTrack

data class CellTracks (private var tracks: MutableList<CellTrack>) {
    fun addTrack(trackId: Int, track: CellTrack) {
        tracks[trackId] = track
    }

    fun getTracks(): List<CellTrack> {
        return tracks
    }
}