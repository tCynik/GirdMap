package com.example.lessongirdrecycler.domain.coordinates_calculator

import com.example.lessongirdrecycler.domain.Logger
import com.example.lessongirdrecycler.domain.models.cell.CellLocation
import com.example.lessongirdrecycler.domain.models.cell.CoordinatesOfCell
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
    val expectedResult = mutableListOf<TrackPartInSingleCell>()

    @Test
    fun splitStraightToEast() {
        val testTrack = GlobalTrack(id = 1, turnPoints = mutableListOf(
            GlobalTurnPoint(0, 50),
            GlobalTurnPoint(500, 50) ))

        expectedResult.add(TrackPartInSingleCell(
            cell = CoordinatesOfCell(0, 0), mutableListOf(
                CellLocation(0, 50),
                CellLocation(100, 50),)))
        expectedResult.add(TrackPartInSingleCell(
            cell = CoordinatesOfCell(1, 0), mutableListOf(
                CellLocation(0, 50),
                CellLocation(100, 50),)))
        expectedResult.add(TrackPartInSingleCell(
            cell = CoordinatesOfCell(2, 0), mutableListOf(
                CellLocation(0, 50),
                CellLocation(100, 50),)))
        expectedResult.add(TrackPartInSingleCell(
            cell = CoordinatesOfCell(3, 0), mutableListOf(
                CellLocation(0, 50),
                CellLocation(100, 50),)))
        expectedResult.add(TrackPartInSingleCell(
            cell = CoordinatesOfCell(4, 0), mutableListOf(
                CellLocation(0, 50),
                CellLocation(100, 50),)))

        val actualResult: List<TrackPartInSingleCell> = trackSplitter.splitGlobalTrackToCells(testTrack).cellData
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun split45tToSW() {
        val testTrack = GlobalTrack(id = 1, turnPoints = mutableListOf(
            GlobalTurnPoint(100, 0),
            GlobalTurnPoint(-400, 500) ))

        expectedResult.add(TrackPartInSingleCell(
            cell = CoordinatesOfCell(0, 0), mutableListOf(
                CellLocation(100, 0),
                CellLocation(0, 100),)))
        expectedResult.add(TrackPartInSingleCell(
            cell = CoordinatesOfCell(-1, 1), mutableListOf(
                CellLocation(100, 0),
                CellLocation(0, 100),)))
        expectedResult.add(TrackPartInSingleCell(
            cell = CoordinatesOfCell(-2, 2), mutableListOf(
                CellLocation(100, 0),
                CellLocation(0, 100),)))
        expectedResult.add(TrackPartInSingleCell(
            cell = CoordinatesOfCell(-3, 3), mutableListOf(
                CellLocation(100, 0),
                CellLocation(0, 100),)))
        expectedResult.add(TrackPartInSingleCell(
            cell = CoordinatesOfCell(-4, 4), mutableListOf(
                CellLocation(100, 0),
                CellLocation(0, 100),)))

        val actualResult: List<TrackPartInSingleCell> = trackSplitter.splitGlobalTrackToCells(testTrack).cellData
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun split45tToSWCrossEdge() {
        val testTrack = GlobalTrack(id = 1, turnPoints = mutableListOf(
            GlobalTurnPoint(100, 50),
            GlobalTurnPoint(-50, 200) ))

        expectedResult.add(TrackPartInSingleCell(
            cell = CoordinatesOfCell(0, 0), mutableListOf(
                CellLocation(100, 50),
                CellLocation(50, 100),)))
        expectedResult.add(TrackPartInSingleCell(
            cell = CoordinatesOfCell(0, 1), mutableListOf(
                CellLocation(50, 0),
                CellLocation(0, 50),)))
        expectedResult.add(TrackPartInSingleCell(
            cell = CoordinatesOfCell(-1, 1), mutableListOf(
                CellLocation(100, 50),
                CellLocation(50, 100),)))

        val actualResult: List<TrackPartInSingleCell> = trackSplitter.splitGlobalTrackToCells(testTrack).cellData
        Assert.assertEquals(expectedResult, actualResult)
    }

    // next is to test cross-edge calculating
}