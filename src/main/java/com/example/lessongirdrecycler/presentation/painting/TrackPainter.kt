package com.example.lessongirdrecycler.presentation.painting

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import android.view.View
import com.example.lessongirdrecycler.domain.models.cell.CellLocation
import com.example.lessongirdrecycler.models.TracksPack

open class TrackPainter(context: Context, startPosition: CellLocation): View(context) {
    val paint: Paint = Paint()
    val path: Path = Path()

    init{
        paint.style = Paint.Style.STROKE
        path.moveTo(startPosition.x.toFloat(), startPosition.y.toFloat())
    }

//    fun addTrack (tracks: MutableList<CellLocation>) {
//        for (i in 0 until tracks.size) {
//            drawNextSegment(tracks[i])
//        }
//    }

    fun drawNextSegment(cellLocation: CellLocation) {
        Log.i("bugfix: trackPainter", "painting segment to next point: ${cellLocation.x} : ${cellLocation.y}, color - ${paint.color}")
        val lineToX = cellLocation.x.toFloat()
        val lineToY = cellLocation.y.toFloat()
        path.moveTo(0f, 0f)
        path.lineTo(lineToX, lineToY)
        path.moveTo(lineToX, lineToY)
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        //super.onDraw(canvas)
        Log.i("bugfix: trackPainter", "onDraw")
        canvas!!.drawPath(path, paint)
    }

    protected fun setPaintAttributes (color: Int, width: Int) {
        paint.color = color
        paint.strokeWidth = width.toFloat()
    }
}

class CurrentTrackLine(context: Context, startPosition: CellLocation): TrackPainter(context, startPosition) {
    private val trackColor = Color.RED//Color.WHITE //R.color.current_track_line;
    private val trackWidth = 20

    init{
        setPaintAttributes(color = trackColor, width = trackWidth)
    }
}