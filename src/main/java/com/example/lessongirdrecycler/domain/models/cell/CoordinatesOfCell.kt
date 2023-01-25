package com.example.lessongirdrecycler.domain.models.cell

data class CoordinatesOfCell(val x: Int, val y: Int) {
    override fun toString(): String {
        return "$x-$y"
    }
}
