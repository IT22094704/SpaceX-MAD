package com.example.spacex

import android.content.Context
import android.os.Bundle
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var highScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(SpaceShooter(this))

        // Enable edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(window, false)
        sharedPreferences = getSharedPreferences("my_game_prefs", Context.MODE_PRIVATE)
        highScore = sharedPreferences.getInt("highScore", 0)
    }

    fun updateHighScore(score: Int) {
        if (score > highScore) {
            highScore = score
            sharedPreferences.edit().putInt("highScore", highScore).apply()
        }
    }

    fun getHighScore(): Int {
        return highScore
    }
}
