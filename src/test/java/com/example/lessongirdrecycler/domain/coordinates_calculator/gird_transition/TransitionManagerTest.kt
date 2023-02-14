package com.example.lessongirdrecycler.domain.coordinates_calculator.gird_transition

import com.example.lessongirdrecycler.domain.models.cell.CellLocation
import junit.framework.Assert
import org.junit.Test
import kotlin.math.exp


internal class TransitionManagerTest {
    val cellSize = 100
    val transitionManager = TransitionManager(cellSize)

    @Test
    fun fromTopLeftNStraight() {
        val start = CellLocation(0, 0)
        val end = CellLocation(0, -200)

        val expected = TransitionTo.NORTH
        val actual = transitionManager.getTransitionTo(start, end)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromTopLeftNE45() {
        val start = CellLocation(0, 0)
        val end = CellLocation(200, -200)

        val expected = TransitionTo.NE
        val actual = transitionManager.getTransitionTo(start, end)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromTopLeftNW45() {
        val start = CellLocation(0, 0)
        val end = CellLocation(-200, -200)

        val expected = TransitionTo.NW
        val actual = transitionManager.getTransitionTo(start, end)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromTopCenterNorth() {
        val start = CellLocation(50, 0)
        val end = CellLocation(50, -200)

        val expected = TransitionTo.NORTH
        val actual = transitionManager.getTransitionTo(start, end)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromTopCenterNW() {
        val start = CellLocation(50, 0)
        val end = CellLocation(-1, -200)

        val expected = TransitionTo.NW
        val actual = transitionManager.getTransitionTo(start, end)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromTopCenterNE() {
        val start = CellLocation(50, 0)
        val end = CellLocation(101, -200)

        val expected = TransitionTo.NE
        val actual = transitionManager.getTransitionTo(start, end)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromLeftCenterSW() { // todo: Transition to left cell (must be WEST?), BUT because its on left it is SW
        val start = CellLocation(1, 50)
        val end = CellLocation(-100, 101)

        val expected = TransitionTo.SW
        val actual = transitionManager.getTransitionTo(start, end)
        Assert.assertEquals(expected, actual)
    }





    @Test
    fun fromBottomLeftSW45() {
        val start = CellLocation(0, 100)
        val end = CellLocation(-100, 200)

        val expected = TransitionTo.SW
        val actual = transitionManager.getTransitionTo(start, end)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromBottomCenterSW45() {
        val start = CellLocation(50, 100)
        val end = CellLocation(-50, 200)

        val expected = TransitionTo.SW
        val actual = transitionManager.getTransitionTo(start, end)
        Assert.assertEquals(expected, actual)
    }


    @Test
    fun fromRightCenterSW45() {
        val start = CellLocation(100, 50)
        val end = CellLocation(-50, 200)

        val expected = TransitionTo.SW
        val actual = transitionManager.getTransitionTo(start, end)
        Assert.assertEquals(expected, actual)
    }




}