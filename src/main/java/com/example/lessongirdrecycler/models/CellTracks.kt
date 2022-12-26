package com.example.lessongirdrecycler.models

import com.example.lessongirdrecycler.domain.models.global.GlobalTurnPoint

data class CellTracks(val localTracksList: Map<String, Track>, val topStart: GlobalTurnPoint) {
}