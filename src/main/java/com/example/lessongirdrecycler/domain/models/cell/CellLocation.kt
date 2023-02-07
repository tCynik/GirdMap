package com.example.lessongirdrecycler.domain.models.cell
// coordinates inside current cell
data class CellLocation(val x: Int, val y: Int) {
    override fun toString(): String {
        return "$x-$y"
    }

    private fun print() {
        print(this.toString())
    }
}