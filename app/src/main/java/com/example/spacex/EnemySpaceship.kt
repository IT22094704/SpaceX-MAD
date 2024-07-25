package com.example.spacex

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.Random

class EnemySpaceship(private val context: Context) {
    private val enemySpaceship: Bitmap
    var ex = 0
        set(value) { field = value }
    var ey = 0
        set(value) { field = value }
    var enemyVelocity = 0
    private val random = Random()


    init {
        enemySpaceship = BitmapFactory.decodeResource(context.resources, R.drawable.alienx)
        resetEnemySpaceship()
    }

    fun getEnemySpaceship(): Bitmap {
        return enemySpaceship
    }

    fun getEnemySpaceshipWidth(): Int {
        return enemySpaceship.width
    }

    fun getEnemySpaceshipHeight(): Int {
        return enemySpaceship.height
    }

    private fun resetEnemySpaceship() {
        ex = 200 + random.nextInt(400)
        ey = 0
        enemyVelocity = 14 + random.nextInt(10)
    }
}
