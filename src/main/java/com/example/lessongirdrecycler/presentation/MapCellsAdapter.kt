package com.example.lessongirdrecycler.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lessongirdrecycler.R
import com.example.lessongirdrecycler.domain.models.cell.*
import com.example.lessongirdrecycler.presentation.painting.CurrentTrackLine
import java.util.*

// в полях имеем мапу "координаты ячейки - набор CellTrack'ов"
// при байдинге во вью холдерах рисуем все треки относящиеся к конкретной ячейке


class MapCellsAdapter(var numberItems: Int, var columnsNumber: Int): RecyclerView.Adapter<MapCellsAdapter.MapViewHolder>() {
    private var nubmerViewHolders = 0
    private var tracksDataByCells = mutableMapOf<CoordinatesOfCell, LinkedList<CellTrack>>()

    init{tempInitTestTrack()}

    private fun tempInitTestTrack() { // todo: just for test, to be deleted
        var cell = CoordinatesOfCell(1, 1)
        var turnPoints = LinkedList<CellLocation>()
        turnPoints.add(CellLocation(10, 10))
        turnPoints.add(CellLocation(150, 120))
        turnPoints.add(CellLocation(200, 180))
        turnPoints.add(CellLocation(230, 250))
        turnPoints.add(CellLocation(300, 400))

        var track = CellTrack(id = 1, turnPoints)

        var list = LinkedList<CellTrack>()
        list.add(track)
        tracksDataByCells[cell] = list

        cell = CoordinatesOfCell(1, 2)
        turnPoints = LinkedList<CellLocation>()
        turnPoints.add(CellLocation(10, 10))
        turnPoints.add(CellLocation(150, 120))
        turnPoints.add(CellLocation(200, 180))
        turnPoints.add(CellLocation(230, 250))
        turnPoints.add(CellLocation(300, 400))
        track = CellTrack(id = 2, turnPoints)
        list.add(track)
        tracksDataByCells[cell] = list

    }

    class MapViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val listItemNumberView: TextView = itemView.findViewById(R.id.tv_number_item)
        val viewHolderNumber: TextView = itemView.findViewById(R.id.tv_holder_number)
        val mapPlace: FrameLayout = itemView.findViewById(R.id.cell_map)
        //val trackLines = mutableListOf(CurrentTrackLine(itemView.context, CellLocation(0, 0))) // todo: delete
        val trackLines: MutableList<CurrentTrackLine> = mutableListOf()

        fun addViews() { // todo: try to add several views
            mapPlace.removeAllViews()
            for (currentView in trackLines) {
                mapPlace.addView(currentView)
            }
        }

        fun setText(text: String) {
            listItemNumberView.text = text // todo: delete
        }

        fun makeTrackLines(tracks: List<CellTrack>) {
            trackLines.clear()
            for (track in tracks) {
                val currentTrackLine = CurrentTrackLine(
                    itemView.context,
                    CellLocation(track.turnPoints[0].x, track.turnPoints[0].y))
                currentTrackLine.drawTrack(track.turnPoints)
                trackLines.add(currentTrackLine)
            }
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
        if (tracksDataByCells.containsKey(currentCellCoordinate) &&
            tracksDataByCells[currentCellCoordinate]!=null) {
            Log.i("bugfix: adapter", "data contains track for cell ${currentCellCoordinate.toString()}, position = $position")
            holder.makeTrackLines(tracksDataByCells[currentCellCoordinate]!!)
            holder.addViews()
        }
    }

    override fun getItemCount(): Int {
        return  numberItems
    }

    fun updateCellsTrackData(data: MutableMap<CoordinatesOfCell, LinkedList<CellTrack>>) {
        tracksDataByCells = data
        notifyDataSetChanged() // todo: think about optimization this place
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