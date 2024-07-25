package com.example.spacex

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameOver : AppCompatActivity() {
    private lateinit var tvPoints: TextView
    private lateinit var tvHighScore: TextView
    private lateinit var ivNewHighest: TextView
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_over)

        // Initialize TextViews
        tvPoints = findViewById(R.id.tvPoints)
        tvHighScore = findViewById(R.id.tvHighScore)
        ivNewHighest = findViewById(R.id.tvNewHighest)

        // Get points from Intent
        val points = intent.extras?.getInt("points", 0) ?: 0
        tvPoints.text = points.toString()

        // Get highest score from SharedPreferences
        sharedPreferences = getSharedPreferences("my_pref", 0)
        var highest = sharedPreferences.getInt("highest", 0)

        // Check if current score is higher than highest
        if (points > highest) {
            // Display new highest score indicator
            ivNewHighest.visibility = View.VISIBLE
            // Update highest score
            highest = points
            // Save new highest score to SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putInt("highest", highest)
            editor.apply() // Use apply() instead of commit()
        }

        // Set highest score text
        tvHighScore.text = highest.toString()
    }

    fun restart(v: View) {
        val intent = Intent(this, StartUp::class.java)
        startActivity(intent)
        finish()
    }

    fun exit(v: View) {
        finish()
    }
}
