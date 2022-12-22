package com.example.lessongirdrecycler.domain.models

import com.example.lessongirdrecycler.domain.models.local.LocalLocation

data class GirdTrack(val id: Int, val turnPoints: List<LocalLocation>) {
}