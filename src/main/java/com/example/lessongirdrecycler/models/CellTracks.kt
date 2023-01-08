package com.example.lessongirdrecycler.models

import com.example.lessongirdrecycler.domain.models.global.GlobalTrack
import com.example.lessongirdrecycler.domain.models.global.GlobalTurnPoint

data class CellTracks(val localTracksList: Map<String, GlobalTrack>, val topStart: GlobalTurnPoint) {
}