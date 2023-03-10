package com.example.lessongirdrecycler

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lessongirdrecycler.data.TrackRepository
import com.example.lessongirdrecycler.models.TracksPack
import com.example.lessongirdrecycler.presentation.MainViewModel
import com.example.lessongirdrecycler.presentation.MapCellsAdapter
import com.example.lessongirdrecycler.presentation.painting.TrackPainter

/**
 * 1. генерим рандомный трек гобальных координат
 * 2. передаем трек во ViewModel
 *      - перевод глобальных координат в местные???
 * 3. трек передается в контроллер треков (он хранит все ячейки со всеми треками)
 *      - перевод глобальных координат в местные???
 * 4. трек передается в калькулятор, возвращается в виде коллекции треков для каждой ячейки
 *      4.1 переводим глобальные координаты в местные
 *      4.2 растаскиваем местные координаты по ячейкам, возвращаем массив <номер ячеки+трек в ячейке>:
 *      - для первой точки трека определяем номер ячейки и координаты в ячейке.
 *      берем координаты ячейки, получаем координаты первой точки в ячейке. Если вторая точка внутри
 *      ячейки, добавляем ее в трек, далее считаем заново. Если вторая точка вне
 *      ячейки -> считаем вторую точку внутри ячейки, и коорд следующей ячейку -> всё заново
 *
 * 5. контроллер суммирует все полученные треки ячеек в массив ячеек со всеми треками,
 * 6. массив ячеек со всеми треками передается во ViewModel в LiveData
 * 7. активити отслеживает LiveData и через recyclerView отрисовывает ячейки, котоыре видны
 *
 */
class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    var mapRecyclerView: RecyclerView? = null
    //private val trackPainter = TrackPainter(this, CellLocation(0,0)) // todo: Убрать отсюда - в каждой ячейке свой (см ниже в обсерверах, разбраться)
    var numberOfColumns = 5
    private val elementsCount = 100
    val adapter = MapCellsAdapter(elementsCount, numberOfColumns)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val adapter = MapCellsAdapter(elementsCount, numberOfColumns)
        mapRecyclerView = findViewById(R.id.my_recycler)

        val layoutManager = GridLayoutManager(this, numberOfColumns)
        mapRecyclerView!!.layoutManager = layoutManager
        mapRecyclerView!!.setHasFixedSize(true) // нужно тупо для эффективности
        mapRecyclerView!!.adapter = adapter

        // работа с кнопками
        val buttonColumnInc = findViewById<Button>(R.id.span_inc)
        val buttonColumnDec = findViewById<Button>(R.id.span_dec)

        buttonColumnInc.setOnClickListener(View.OnClickListener {
            numberOfColumns++
            updateSpanCount()
        })
        buttonColumnDec.setOnClickListener(View.OnClickListener {
            numberOfColumns--
            updateSpanCount()
        })


        val currentTrack = TrackRepository().loadNextTrack()
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.updateTracks(currentTrack)

    }

    override fun onResume() {
        initObservers()

        //adapter.notifyDataSetChanged()
        super.onResume()
    }

    private fun initObservers() {
        mainViewModel.tracksAndCellsLive.observe(
            this, Observer<TracksPack>{ value -> adapter.updateCellsTrackData(value.tracksByCells)})

    }

    private fun updateSpanCount() {
        val layoutManager = GridLayoutManager(this, numberOfColumns)
        mapRecyclerView!!.layoutManager = layoutManager
        adapter.updateColumnsNumber(numberOfColumns)
    }

}