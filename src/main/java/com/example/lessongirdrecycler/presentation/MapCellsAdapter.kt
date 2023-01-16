package com.example.lessongirdrecycler.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lessongirdrecycler.R

class MapCellsAdapter(var numberItems: Int): RecyclerView.Adapter<MapCellsAdapter.MapViewHolder>() {
    private var nubmerViewHolders = 0

    class MapViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val listItemNumberView: TextView = itemView.findViewById(R.id.tv_number_item)
        val viewHolderNumber: TextView = itemView.findViewById(R.id.tv_holder_number)

        fun bind(index: Int) {
            listItemNumberView.text = index.toString()
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
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return  numberItems
    }


}