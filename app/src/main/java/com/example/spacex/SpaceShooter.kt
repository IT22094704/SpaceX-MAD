package com.example.spacex

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Handler
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import java.util.*
import kotlin.collections.ArrayList

class SpaceShooter(context: Context) : View(context) {
    private val background: Bitmap
    private val lifeImage: Bitmap
    private val handler: Handler
    private val UPDATE_MILLIS: Long = 30
    public var points = 0
    private var life = 3
    private val scorePaint = Paint().apply {
        color = Color.RED
        textSize = 80f
        textAlign = Paint.Align.LEFT
    }
    private val TEXT_SIZE = 80
    private var paused = false
    private val ourSpaceship: OurSpaceship
    private val enemySpaceship: EnemySpaceship
    private val random = Random()
    private val enemyShots = ArrayList<Shot>()
    private val ourShots = ArrayList<Shot>()
    private var enemyExplosion = false
    private lateinit var explosion: Explosion
    private val explosions = ArrayList<Explosion>()
    private var enemyShotAction = false

    private val runnable = Runnable { invalidate() }

    init {
        val metrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(metrics)
        screenWidth = metrics.widthPixels
        screenHeight = metrics.heightPixels
        ourSpaceship = OurSpaceship(context)
        enemySpaceship = EnemySpaceship(context)
        background = BitmapFactory.decodeResource(context.resources, R.drawable.background2)
        lifeImage = BitmapFactory.decodeResource(context.resources, R.drawable.life)
        handler = Handler()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(background, 0f, 0f, null)
        canvas.drawText("Pt: $points", 0f, TEXT_SIZE.toFloat(), scorePaint)
        for (i in life downTo 1) {
            canvas.drawBitmap(lifeImage, screenWidth - lifeImage.width * i.toFloat(), 0f, null)
        }
        if (life == 0) {
            paused = true
            handler.removeCallbacks(runnable)
            val intent = Intent(context, GameOver::class.java)
            intent.putExtra("points", points)
            context.startActivity(intent)
            (context as Activity).finish()
        }

        enemySpaceship.ex += enemySpaceship.enemyVelocity
        if (enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() >= screenWidth) {
            enemySpaceship.enemyVelocity *= -1
        }
        if (enemySpaceship.ex <= 0) {
            enemySpaceship.enemyVelocity *= -1
        }
        if (!enemyShotAction && enemySpaceship.ex >= 200 + random.nextInt(400)) {
            val enemyShot = Shot(context, enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() / 2, enemySpaceship.ey)
            enemyShots.add(enemyShot)
            enemyShotAction = true
        }
        if (!enemyExplosion) {
            canvas.drawBitmap(enemySpaceship.getEnemySpaceship(), enemySpaceship.ex.toFloat(), enemySpaceship.ey.toFloat(), null)
        }
        if (ourSpaceship.isAlive) {
            if (ourSpaceship.ox > screenWidth - ourSpaceship.getOurSpaceshipWidth()) {
                ourSpaceship.ox = screenWidth - ourSpaceship.getOurSpaceshipWidth()
            } else if (ourSpaceship.ox < 0) {
                ourSpaceship.ox = 0
            }
            canvas.drawBitmap(ourSpaceship.getOurSpaceship(), ourSpaceship.ox.toFloat(), ourSpaceship.oy.toFloat(), null)
        }
        for (i in enemyShots.size - 1 downTo 0) {
            enemyShots[i].shy += 15
            canvas.drawBitmap(enemyShots[i].getShot(), enemyShots[i].shx.toFloat(), enemyShots[i].shy.toFloat(), null)
            if (enemyShots[i].shx >= ourSpaceship.ox &&
                enemyShots[i].shx <= ourSpaceship.ox + ourSpaceship.getOurSpaceshipWidth() &&
                enemyShots[i].shy >= ourSpaceship.oy &&
                enemyShots[i].shy <= screenHeight
            ) {
                life--
                enemyShots.removeAt(i)
                explosion = Explosion(context, ourSpaceship.ox, ourSpaceship.oy)
                explosions.add(explosion)
            } else if (enemyShots[i].shy >= screenHeight) {
                enemyShots.removeAt(i)
            }
            if (enemyShots.isEmpty()) {
                enemyShotAction = false
            }
        }

        for (i in ourShots.size - 1 downTo 0) {
            ourShots[i].shy -= 15
            canvas.drawBitmap(ourShots[i].getShot(), ourShots[i].shx.toFloat(), ourShots[i].shy.toFloat(), null)
            if (ourShots[i].shx >= enemySpaceship.ex &&
                ourShots[i].shx <= enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() &&
                ourShots[i].shy <= enemySpaceship.getEnemySpaceshipHeight() &&
                ourShots[i].shy >= enemySpaceship.ey
            ) {
                points++
                ourShots.removeAt(i)
                explosion = Explosion(context, enemySpaceship.ex, enemySpaceship.ey)
                explosions.add(explosion)
            } else if (ourShots[i].shy <= 0) {
                ourShots.removeAt(i)
            }
        }
        for (i in explosions.indices) {
            canvas.drawBitmap(
                explosions[i].getExplosion(explosions[i].explosionFrame)!!,
                explosions[i].eX.toFloat(),
                explosions[i].eY.toFloat(),
                null
            )
            explosions[i].explosionFrame++
            if (explosions[i].explosionFrame > 8) {
                explosions.removeAt(i)
            }
        }
        if (!paused) {
            handler.postDelayed(runnable, UPDATE_MILLIS)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x.toInt()
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                if (ourShots.size < 3) {
                    val ourShot = Shot(context, ourSpaceship.ox + ourSpaceship.getOurSpaceshipWidth() / 2, ourSpaceship.oy)
                    ourShots.add(ourShot)
                }
            }
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> ourSpaceship.ox = touchX
        }
        return true
    }

    companion object {
        var screenWidth = 0
        var screenHeight = 0
    }
}
