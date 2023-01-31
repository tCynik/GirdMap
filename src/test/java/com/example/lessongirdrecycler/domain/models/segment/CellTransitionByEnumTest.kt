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

    // directly
    @Test
    fun transitionNorthCurrent () {
        val transitionLocations = calculateTransitionNorth(50, -50)
        Assert.assertEquals(CellLocation(50, 0), transitionLocations[0])
    }

    @Test
    fun transitionNorthNext () {
        val transitionLocations = calculateTransitionNorth(50, -50)
        Assert.assertEquals(CellLocation(50, 100), transitionLocations[1])
    }

    @Test
    fun transitionEastCurrent () {
        val transitionLocations = calculateTransitionEast(150, 50)
        Assert.assertEquals(CellLocation(100, 50), transitionLocations[0])
    }

    @Test
    fun transitionEastNext () {
        val transitionLocations = calculateTransitionEast(150, 50)
        Assert.assertEquals(CellLocation(0, 50), transitionLocations[1])
    }

    @Test
    fun transitionSouthCurrent () {
        val transitionLocations = calculateTransitionSouth(50, 150)
        Assert.assertEquals(CellLocation(50, 100), transitionLocations[0])
    }

    @Test
    fun transitionSouthNext () {
        val transitionLocations = calculateTransitionSouth(50, 150)
        Assert.assertEquals(CellLocation(50, 0), transitionLocations[1])
    }

    @Test
    fun transitionWestCurrent () {
        val transitionLocations = calculateTransitionWest(-50, 50)
        Assert.assertEquals(CellLocation(0, 50), transitionLocations[0])
    }

    @Test
    fun transitionWestNext () {
        val transitionLocations = calculateTransitionWest(-50, 50)
        Assert.assertEquals(CellLocation(100, 50), transitionLocations[1])
    }

    // SE
    @Test
    fun transitionSE45Current () {
        val transitionLocations = calculateTransitionSE(x = 150, y = 150)
        Assert.assertEquals(CellLocation(100, 100), transitionLocations[0])
    }

    @Test
    fun transitionSE45Next () {
        val transitionLocations = calculateTransitionSE(x = 150, y = 150)
        Assert.assertEquals(CellLocation(0, 0), transitionLocations[1])
    }

    @Test
    fun transitionSELess45Current () {
        val transitionLocations = calculateTransitionSE(x = 150, y = 100)
        Assert.assertEquals(CellLocation(100, 75), transitionLocations[0])
    }

    @Test
    fun transitionSELess45Next () {
        val transitionLocations = calculateTransitionSE(x = 150, y = 100)
        Assert.assertEquals(CellLocation(0, 75), transitionLocations[1])
    }

    @Test
    fun transitionSEMore45Current () {
        val transitionLocations = calculateTransitionSE(x = 100, y = 150)
        Assert.assertEquals(CellLocation(75, 100), transitionLocations[0])
    }

    @Test
    fun transitionSEMore45Next () {
        val transitionLocations = calculateTransitionSE(x = 100, y = 150)
        Assert.assertEquals(CellLocation(75, 0), transitionLocations[1])
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



//____________________________________________________________________________________

    private fun calculateTransitionSE(x: Int, y: Int): List<CellLocation> {
        val transitionTo = TransitionTo.SE
        val endCellLocation = CellLocation(x, y)
        return cellTransitionCalculator.getTransitionPoints(
            transitionTo = transitionTo,
            startCellLocation = startCellLocation,
            endCellLocation = endCellLocation)
    }
    
    private fun calculateTransitionNorth(x: Int, y: Int): List<CellLocation> {
        val transitionTo = TransitionTo.NORTH
        val endCellLocation = CellLocation(x, y)
        return cellTransitionCalculator.getTransitionPoints(
            transitionTo = transitionTo,
            startCellLocation = startCellLocation,
            endCellLocation = endCellLocation)
    }

    private fun calculateTransitionEast(x: Int, y: Int): List<CellLocation> {
        val transitionTo = TransitionTo.EAST
        val endCellLocation = CellLocation(x, y)
        return cellTransitionCalculator.getTransitionPoints(
            transitionTo = transitionTo,
            startCellLocation = startCellLocation,
            endCellLocation = endCellLocation)
    }

    private fun calculateTransitionSouth(x: Int, y: Int): List<CellLocation> {
        val transitionTo = TransitionTo.SOUTH
        val endCellLocation = CellLocation(x, y)
        return cellTransitionCalculator.getTransitionPoints(
            transitionTo = transitionTo,
            startCellLocation = startCellLocation,
            endCellLocation = endCellLocation)
    }

    private fun calculateTransitionWest(x: Int, y: Int): List<CellLocation> {
        val transitionTo = TransitionTo.WEST
        val endCellLocation = CellLocation(x, y)
        return cellTransitionCalculator.getTransitionPoints(
            transitionTo = transitionTo,
            startCellLocation = startCellLocation,
            endCellLocation = endCellLocation)
    }

    private fun calculateTransition(transitionTo: TransitionTo, x: Int, y: Int): List<CellLocation> {
        val endCellLocation = CellLocation(x, y)
        return cellTransitionCalculator.getTransitionPoints(
            transitionTo = transitionTo,
            startCellLocation = startCellLocation,
            endCellLocation = endCellLocation)
    }

}