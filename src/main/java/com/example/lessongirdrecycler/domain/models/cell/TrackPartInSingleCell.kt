package com.example.lessongirdrecycler.domain.models.cell

// единица части трека: отдельный участок трека внутри ячейки
// последовательность таких единиц (ячеек с кусочками трека) сосставляют целый трек
// из набора таких последовательностей формируется набор треков для отображения
data class TrackPartInSingleCell (val cell: CoordinatesOfCell, val track: MutableList<CellLocation>) {
    fun addPointToTrack (location: CellLocation) {
        track.add(location)
    }
}