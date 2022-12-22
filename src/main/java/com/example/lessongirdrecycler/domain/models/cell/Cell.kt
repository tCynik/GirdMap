package com.example.lessongirdrecycler.domain.models.cell

import com.example.lessongirdrecycler.domain.models.CellNumber

data class Cell(val cellData: MutableMap<CellNumber, CellTracks>) {
}