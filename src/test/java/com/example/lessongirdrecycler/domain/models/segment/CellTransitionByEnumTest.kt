package com.example.lessongirdrecycler.domain.models.segment

import com.example.lessongirdrecycler.domain.Logger
import com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition.TransitionTo
import com.example.lessongirdrecycler.domain.models.cell.CellLocation
import org.junit.Assert
import org.junit.Test

internal class CellTransitionByEnumTest {
    companion object logger: Logger {
        override fun printLog(logMessage: String) {
            println(logMessage)
        }
    }

    val cellTransitionCalculator = CellTransitionByEnum(100, logger)
    val startCellLocation = CellLocation(50, 50)

    // directly one axis transitions
    @Test
    fun transitionNorth () {
        val transitionLocations = calculateTransition(TransitionTo.NORTH,50, -50)
        val currentExpected = CellLocation(50, 0)
        val nextExpected = CellLocation(50, 100)
        Assert.assertEquals(listOf(currentExpected, nextExpected), transitionLocations)
    }

    @Test
    fun transitionEast () {
        val transitionLocations = calculateTransition(TransitionTo.EAST,150, 50)
        val currentExpected = CellLocation(100, 50)
        val nextExpected = CellLocation(0, 50)
        Assert.assertEquals(listOf(currentExpected, nextExpected), transitionLocations)
    }

    @Test
    fun transitionSouth () {
        val transitionLocations = calculateTransition(TransitionTo.SOUTH,50, 150)
        val currentExpected = CellLocation(50, 100)
        val nextExpected = CellLocation(50, 0)
        Assert.assertEquals(listOf(currentExpected, nextExpected), transitionLocations)
    }

    @Test
    fun transitionWest () {
        val transitionLocations = calculateTransition(TransitionTo.WEST,-50, 50)
        val currentExpected = CellLocation(0, 50)
        val nextExpected = CellLocation(100, 50)
        Assert.assertEquals(listOf(currentExpected, nextExpected), transitionLocations)
    }

    // SE
    @Test
    fun transitionSEMore45() {
        val transitionLocations = calculateTransition(transitionTo = TransitionTo.SE, x = 100, y = 150)
        val currentExpected = CellLocation(75, 100)
        val nextExpected = CellLocation(75, 0)
        Assert.assertEquals(listOf(currentExpected, nextExpected), transitionLocations)
    }

    @Test
    fun transitionSE45() {
        val transitionLocations = calculateTransition(transitionTo = TransitionTo.SE, x = 150, y = 150)
        val currentExpected = CellLocation(100, 100)
        val nextExpected = CellLocation(0, 0)
        Assert.assertEquals(listOf(currentExpected, nextExpected), transitionLocations)
    }

    @Test
    fun transitionSELess45() {
        val transitionLocations = calculateTransition(transitionTo = TransitionTo.SE, x = 150, y = 100)
        val currentExpected = CellLocation(100, 75)
        val nextExpected = CellLocation(0, 75)
        Assert.assertEquals(listOf(currentExpected, nextExpected), transitionLocations)
    }

    //NE
    @Test
    fun transitionNEMore45() { // calc by right
        val transitionLocations = calculateTransition(transitionTo = TransitionTo.NE, x = 150, y = 0)
        val currentExpected = CellLocation(100, 25)
        val nextExpected = CellLocation(0, 25)
        Assert.assertEquals(listOf(currentExpected, nextExpected), transitionLocations)
    }

    @Test
    fun transitionNE45() {
        val transitionLocations = calculateTransition(transitionTo = TransitionTo.NE, x = 150, y = -50)
        val currentExpected = CellLocation(100, 0)
        val nextExpected = CellLocation(0, 0)
        Assert.assertEquals(listOf(currentExpected, nextExpected), transitionLocations)
    }

    @Test
    fun transitionNELess45() { // calc by top
        val transitionLocations = calculateTransition(transitionTo = TransitionTo.NE, x = 100, y = -50)
        val currentExpected = CellLocation(75, 0)
        val nextExpected = CellLocation(75, 100)
        Assert.assertEquals(listOf(currentExpected, nextExpected), transitionLocations)
    }

    // SW
    @Test
    fun transitionSWMore45() { // calc by side
        val transitionLocations = calculateTransition(transitionTo = TransitionTo.SW, x = -50, y = 100)
        val currentExpected = CellLocation(0, 75)
        val nextExpected = CellLocation(100, 75)
        Assert.assertEquals(listOf(currentExpected, nextExpected), transitionLocations)
    }

    @Test
    fun transitionSW45() {
        val transitionLocations = calculateTransition(transitionTo = TransitionTo.SW, x = -50, y = 150)
        val currentExpected = CellLocation(0, 100)
        val nextExpected = CellLocation(100, 0)
        Assert.assertEquals(listOf(currentExpected, nextExpected), transitionLocations)
    }

    @Test
    fun transitionSWLess45() { // calc by bottom
        val transitionLocations = calculateTransition(transitionTo = TransitionTo.SW, x = 0, y = 150)
        val currentExpected = CellLocation(25, 100)
        val nextExpected = CellLocation(25, 0)
        Assert.assertEquals(listOf(currentExpected, nextExpected), transitionLocations)
    }

    //NW
    @Test
    fun transitionNWMore45() { // calc by top
        val transitionLocations = calculateTransition(transitionTo = TransitionTo.NW, x = 0, y = -50)
        val currentExpected = CellLocation(25, 0)
        val nextExpected = CellLocation(25, 100)
        Assert.assertEquals(listOf(currentExpected, nextExpected), transitionLocations)
    }

    @Test
    fun transitionNW45() {
        val transitionLocations = calculateTransition(transitionTo = TransitionTo.NW, x = -50, y = -50)
        val currentExpected = CellLocation(0, 0)
        val nextExpected = CellLocation(100, 100)
        Assert.assertEquals(listOf(currentExpected, nextExpected), transitionLocations)
    }

    @Test
    fun transitionNWLess45() { // calc by side
        val transitionLocations = calculateTransition(transitionTo = TransitionTo.NW, x = -50, y = 0)
        val currentExpected = CellLocation(0, 25)
        val nextExpected = CellLocation(100, 25)
        Assert.assertEquals(listOf(currentExpected, nextExpected), transitionLocations)
    }

    @Test
    fun startByZero() {
        val transitionLocations = cellTransitionCalculator.getTransitionPoints(
            transitionTo = TransitionTo.NW,
            startCellLocation = CellLocation(0, 0),
            endCellLocation = CellLocation(-50, -50))
        val currentExpected = CellLocation(0, 0)
        val nextExpected = CellLocation(100, 100)
        Assert.assertEquals(listOf(currentExpected, nextExpected), transitionLocations)
    }

    // todo: need to test transition from current_zero



    private fun calculateTransition(transitionTo: TransitionTo, x: Int, y: Int): List<CellLocation> {
        val endCellLocation = CellLocation(x, y)
        return cellTransitionCalculator.getTransitionPoints(
            transitionTo = transitionTo,
            startCellLocation = startCellLocation,
            endCellLocation = endCellLocation)
    }
}