package com.example.lessongirdrecycler.domain.models.segment

import com.example.lessongirdrecycler.domain.models.cell.CellLocation

class SegmentTransitionManager {

    fun getTransition(endCellLocation: CellLocation): SegmentTransitionTo {
        val segmentX = endCellLocation.x
        val segmentY = endCellLocation.y

        return if (segmentY < 0) {
            if (segmentX > 0) SegmentTransitionTo.NE
            else if (segmentX < 0 ) SegmentTransitionTo.NW
            else SegmentTransitionTo.NORTH
        } else if (segmentY > 0) {
            if (segmentX > 0) SegmentTransitionTo.SE
            else if (segmentX < 0 ) SegmentTransitionTo.SW
            else SegmentTransitionTo.SOUTH
        } else {
            if (segmentX > 0) SegmentTransitionTo.EAST
            else if (segmentX < 0 ) SegmentTransitionTo.WEST
            else SegmentTransitionTo.NONE
        }
    }
}