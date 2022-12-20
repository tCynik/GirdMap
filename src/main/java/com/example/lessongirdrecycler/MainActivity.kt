package com.example.lessongirdrecycler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lessongirdrecycler.data.TrackRepository

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentTrack = TrackRepository().loadNextTrack()
        mainViewModel.
    }
}