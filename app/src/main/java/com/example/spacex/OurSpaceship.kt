package com.example.spacex

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.Random

class OurSpaceship(private val context: Context) {
    private val ourSpaceship: Bitmap
    var ox = 0
    var oy = 0
    var isAlive = true
    private var ourVelocity = 0
    private val random = Random()

    init {
        ourSpaceship = BitmapFactory.decodeResource(context.resources, R.drawable.rocket2)
        resetOurSpaceship()
    }

    fun getOurSpaceship(): Bitmap {
        return ourSpaceship
    }

    fun getOurSpaceshipWidth(): Int {
        return ourSpaceship.width
    }

    private fun resetOurSpaceship() {
        ox = random.nextInt(SpaceShooter.screenWidth)
        oy = SpaceShooter.screenHeight - ourSpaceship.height
        ourVelocity = 10 + random.nextInt(6)
    }
}
