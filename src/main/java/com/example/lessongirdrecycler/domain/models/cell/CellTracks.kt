package com.example.lessongirdrecycler.domain.models.cell

import com.example.lessongirdrecycler.domain.models.GirdTrack

data class CellTracks (private var tracks: MutableList<GirdTrack>) {
    fun addTrack(trackId: Int, track: GirdTrack) {
        tracks[trackId] = track
    }

    fun getTracks(): List<GirdTrack> {
        return tracks
    }
}