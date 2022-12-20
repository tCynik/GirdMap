package com.example.lessongirdrecycler.models

data class Track(val name: String, val turnPoints: MutableList<TurnPoint> ) {
    fun addTurnPoint(nextTurnPoint: TurnPoint) {
        turnPoints.add(nextTurnPoint)
    }

    fun endBottomCoordinates(): TurnPoint {
        val maxSizeX = maxX()
        val maxSizeY = maxY()
        return TurnPoint(latitude = maxSizeY, longitude = maxSizeX)
    }
    fun startTopCoordinates(): TurnPoint {
        val minSizeX = maxX()
        val minSizeY = maxY()
        return TurnPoint(latitude = minSizeY, longitude = minSizeX)
    }

    private fun maxX(): Int {
        var max = 0
        turnPoints.forEach{turnPoint -> {if (max<turnPoint.longitude) max = turnPoint.longitude}  }
        return max
    }

    private fun maxY(): Int {
        var max = 0
        turnPoints.forEach{turnPoint -> {if (max<turnPoint.latitude) max = turnPoint.latitude}  }
        return max
    }


}