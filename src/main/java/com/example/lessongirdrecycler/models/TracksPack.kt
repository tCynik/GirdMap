package com.example.lessongirdrecycler.models

import com.example.lessongirdrecycler.domain.models.cell.CellTrack
import com.example.lessongirdrecycler.domain.models.cell.CoordinatesOfCell
import com.example.lessongirdrecycler.domain.models.cell.SplittedByCellsTrack
import com.example.lessongirdrecycler.domain.models.global.GlobalTrack
import java.util.*

class TracksPack {
    val tracksByCells = mutableMapOf<CoordinatesOfCell, LinkedList<CellTrack>>()
    val trackNumbers = mutableListOf<Int>()

    fun addSplittedTrack(splittedTrack: SplittedByCellsTrack) {
        for (partOfTrack in splittedTrack.cellData) {
            val currentCell = partOfTrack.cell
            val id = unuseNumber()
            val track = CellTrack(id = id, turnPoints = partOfTrack.track)
            if (!tracksByCells.containsKey(currentCell)) {
                tracksByCells.put(currentCell, LinkedList<CellTrack>())
            }
            tracksByCells[currentCell]!!.add(track)
        }
    }

    fun removeTrackById(id: Int) {
        tracksByCells.forEach {item -> val cellList = item.value
            var i = 0
            while (i < cellList.size) {
                val currentTrack = cellList[i]
                if (currentTrack.id == id) cellList.remove(currentTrack)
                i++
            }
            trackNumbers.remove(id)
        }
    }

    private fun unuseNumber(): Int {
        var numberFounded = false
        var number = 0
        while (!numberFounded) {
            numberFounded = true
            if (trackNumbers.contains(number)) numberFounded = false
            number++
        }
        return number
    }

}