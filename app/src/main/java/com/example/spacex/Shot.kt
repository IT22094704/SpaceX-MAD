package com.example.spacex

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Shot(context: Context, val shx: Int, var shy: Int) {
    private val shot: Bitmap

    init {
        shot = BitmapFactory.decodeResource(context.resources, R.drawable.fire)
    }

    fun getShot(): Bitmap {
        return shot
    }
}
