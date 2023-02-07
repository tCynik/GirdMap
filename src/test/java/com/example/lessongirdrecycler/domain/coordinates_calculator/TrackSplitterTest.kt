package com.example.lessongirdrecycler.domain.coordinates_calculator

import com.example.lessongirdrecycler.domain.Logger
import com.example.lessongirdrecycler.domain.models.cell.CellLocation
import com.example.lessongirdrecycler.domain.models.cell.CoordinatesOfCell
import com.example.lessongirdrecycler.domain.models.cell.SplittedByCellsTrack
import com.example.lessongirdrecycler.domain.models.cell.TrackPartInSingleCell
import com.example.lessongirdrecycler.domain.models.global.GlobalTrack
import com.example.lessongirdrecycler.domain.models.global.GlobalTurnPoint
import junit.framework.Assert
import org.junit.Test


internal class TrackSplitterTest {
    companion object logger: Logger {
        override fun printLog(logMessage: String) {
            println(logMessage)
        }
    }
    val trackSplitter = TrackSplitter(cellSize = 100,
        girdStartX = 0.0,
        girdStartY = 0.0,
        coordinateScale = 1,
        logger = logger)
    val result = mutableListOf<TrackPartInSingleCell>()

    @Test
    fun splitStraightToEast() {
        val testTrack = GlobalTrack(id = 1, turnPoints = mutableListOf(
            GlobalTurnPoint(0, 50),
            GlobalTurnPoint(500, 50) ))

        result.add(TrackPartInSingleCell(
            cell = CoordinatesOfCell(0, 0), mutableListOf(
                CellLocation(0, 50),
                CellLocation(100, 50),)))
        result.add(TrackPartInSingleCell(
            cell = CoordinatesOfCell(1, 0), mutableListOf(
                CellLocation(0, 50),
                CellLocation(100, 50),)))
        result.add(TrackPartInSingleCell(
            cell = CoordinatesOfCell(2, 0), mutableListOf(
                CellLocation(0, 50),
                CellLocation(100, 50),)))
        result.add(TrackPartInSingleCell(
            cell = CoordinatesOfCell(3, 0), mutableListOf(
                CellLocation(0, 50),
                CellLocation(100, 50),)))

        Assert.assertEquals(result, trackSplitter.splitGlobalTrackToCells(testTrack))
    }
}