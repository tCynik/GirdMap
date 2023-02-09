package com.example.lessongirdrecycler.domain.models.global

data class GlobalTurnPoint (val longitude: Int, val latitude: Int) { // longitude = x, latitude = y
    override fun toString(): String {
        return "x=$longitude-y=$latitude"
    }
}