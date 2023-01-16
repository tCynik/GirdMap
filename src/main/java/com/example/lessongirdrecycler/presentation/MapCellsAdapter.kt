package com.example.lessongirdrecycler.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lessongirdrecycler.R
import com.example.lessongirdrecycler.domain.models.cell.CellLocation

class MapCellsAdapter(var numberItems: Int, var columnsNumber: Int): RecyclerView.Adapter<MapCellsAdapter.MapViewHolder>() {
    private var nubmerViewHolders = 0

    class MapViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val listItemNumberView: TextView = itemView.findViewById(R.id.tv_number_item)
        val viewHolderNumber: TextView = itemView.findViewById(R.id.tv_holder_number)
        val mapPlace: LinearLayout = itemView.findViewById(R.id.cell_map)

        fun updateStatus(track: MutableList<CellLocation>){
            // тут создаем canvas, который будет рисовать трек
        }

        fun setText(text: String) {
            listItemNumberView.text = text
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
        holder.setText(calculateCellPosition(position))
    }

    override fun getItemCount(): Int {
        return  numberItems
    }

    fun updateColumnsNumber(number: Int) {
        columnsNumber = number
    }

    fun calculateCellPosition(position: Int): String {
        val currentCellY = (position/columnsNumber) + 1 // позиция начинается с 0
        val currentCellX = (position + columnsNumber) - (currentCellY * columnsNumber) + 1
        return "$currentCellX : $currentCellY"
    }


}