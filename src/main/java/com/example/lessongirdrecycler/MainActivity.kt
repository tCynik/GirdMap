package com.example.lessongirdrecycler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.lessongirdrecycler.data.TrackRepository
import com.example.lessongirdrecycler.presentation.MainViewModel

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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentTrack = TrackRepository().loadNextTrack()
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }
}