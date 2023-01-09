package com.example.lessongirdrecycler.presentation.painting

import android.graphics.Paint
import android.graphics.Path
import android.view.View
import com.example.lessongirdrecycler.domain.models.cell.CellLocation

class TrackPainter: View() {
    val paint: Paint = Paint()
    val path: Path = Path()

    fun drawLine(lineToX: CellLocation, lineToY: CellLocation) {
        path.lineTo(lineToX, lineToY)
        path.moveTo(lineToX, lineToY)
    }
}