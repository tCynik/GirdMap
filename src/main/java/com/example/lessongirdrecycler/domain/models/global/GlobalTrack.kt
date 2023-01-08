package com.example.lessongirdrecycler.domain.models.global

data class GlobalTrack(val id: Int, val turnPoints: MutableList<GlobalTurnPoint> ) {
    fun addTurnPoint(nextTurnPoint: GlobalTurnPoint) {
        turnPoints.add(nextTurnPoint)
    }

    fun endBottomCoordinates(): GlobalTurnPoint {
        val maxSizeX = maxX()
        val maxSizeY = maxY()
        return GlobalTurnPoint(latitude = maxSizeY, longitude = maxSizeX)
    }
    fun startTopCoordinates(): GlobalTurnPoint {
        val minSizeX = maxX()
        val minSizeY = maxY()
        return GlobalTurnPoint(latitude = minSizeY, longitude = minSizeX)
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