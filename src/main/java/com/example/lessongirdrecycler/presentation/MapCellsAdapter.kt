package com.example.lessongirdrecycler.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lessongirdrecycler.R
import com.example.lessongirdrecycler.domain.models.CellTracks
import com.example.lessongirdrecycler.domain.models.cell.*
import com.example.lessongirdrecycler.presentation.painting.CurrentTrackLine

// в полях имеем мапу "координаты ячейки - набор CellTrack'ов"
// при байдинге во вью холдерах рисуем все треки относящиеся к конкретной ячейке


class MapCellsAdapter(var numberItems: Int, var columnsNumber: Int): RecyclerView.Adapter<MapCellsAdapter.MapViewHolder>() {
    private var nubmerViewHolders = 0
    private var cellData = mutableListOf<TrackPartInSingleCell>()
    private var tracksDataByCells = mutableMapOf<CoordinatesOfCell, MutableList<CellTrack>>()

    init{tempInitTestTrack()}

    private fun tempInitTestTrack() { // todo: just for test, to be deleted
        val cell = CoordinatesOfCell(1, 1)
        val turnPoints = mutableListOf<CellLocation>()
        turnPoints.add(CellLocation(10, 10))
        turnPoints.add(CellLocation(150, 120))
        turnPoints.add(CellLocation(200, 180))
        turnPoints.add(CellLocation(230, 250))
        turnPoints.add(CellLocation(300, 400))

        val track = CellTrack(id = 1, turnPoints)

        val list = mutableListOf<CellTrack>(track)
        tracksDataByCells[cell] = list
    }

    class MapViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val listItemNumberView: TextView = itemView.findViewById(R.id.tv_number_item)
        val viewHolderNumber: TextView = itemView.findViewById(R.id.tv_holder_number)
        val mapPlace: LinearLayout = itemView.findViewById(R.id.cell_map)
        val trackLine = CurrentTrackLine(itemView.context, CellLocation(0, 0)) // todo: delete

        init{
            mapPlace.addView(trackLine)
        }

        fun setText(text: String) {
            listItemNumberView.text = text // todo: delete
        }

        fun paintTracks(tracks: List<CellTrack>) {
            for (currentTrack in tracks) {
                drawTheTrack(currentTrack)
            }
        }

        private fun drawTheTrack(track: CellTrack) {
            val turnPoints = track.turnPoints
            val firstPoint = turnPoints[0]
            val line = CurrentTrackLine(itemView.context, CellLocation(x = firstPoint.x, y = firstPoint.y))
            for (i in 1 until turnPoints.size) {
                line.drawNextSegment(turnPoints[i])
            }

            mapPlace.addView(line)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val view = inflator.inflate(R.layout.item_layout, parent, false)
        val viewHolder = MapViewHolder(view)

        // значение на всю жизнь для каждого вью холдера (повторяющееся при переиспользовании)
        viewHolder.viewHolderNumber.text = "VH index = $nubmerViewHolders"
        nubmerViewHolders++
        return viewHolder
    }

    override fun onBindViewHolder(holder: MapViewHolder, position: Int) {
        val cellCoordinates = coordinatesCellByPosition(position)
        val coordinatesString = "${cellCoordinates.x} : ${cellCoordinates.y}"
        holder.setText(coordinatesString)

        val currentCellCoordinate = coordinatesCellByPosition(position)
        if (tracksDataByCells.containsKey(currentCellCoordinate)) {
            Log.i("bugfix: adapter", "data contains track for cell ${currentCellCoordinate.toString()}")
            val cellTracks = tracksDataByCells[currentCellCoordinate]
            holder.paintTracks(cellTracks!!)
        }

        //holder.trackLine.drawNextSegment(CellLocation(200, 200))
    }

    override fun getItemCount(): Int {
        return  numberItems
    }

    fun drawSingleTrack(cell: CoordinatesOfCell, track: CellTrack) {
        val cellNumber = positionCellByCoordinates(cell)

    }

    fun setCellsTrackData(data: MutableMap<CoordinatesOfCell, MutableList<CellTrack>>) {
        tracksDataByCells = data
    }

    fun updateColumnsNumber(number: Int) {
        columnsNumber = number
    }

    private fun coordinatesCellByPosition(position: Int): CoordinatesOfCell {
        val currentCellY = (position/columnsNumber) + 1 // позиция начинается с 0
        val currentCellX = (position + columnsNumber) - (currentCellY * columnsNumber) + 1
        return CoordinatesOfCell(x = currentCellX, y = currentCellY)
    }

    private fun positionCellByCoordinates(coordinates: CoordinatesOfCell): Int {
        return coordinates.y * columnsNumber + coordinates.x
    }
}

class CellsTracksData {
    val data = mapOf<CoordinatesOfCell, List<SplittedByCellsTrack>>()


}