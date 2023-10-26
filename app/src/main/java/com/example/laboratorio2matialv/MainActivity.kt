package com.example.laboratorio2matialv

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var actionTextView: TextView
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionTextView = findViewById(R.id.actionTextView)

        gestureDetector = GestureDetector(this, GestureListener())
    }

    fun openAudio(view: View) {
        val intent = Intent(this, AudioActivity::class.java) // Replace with the name of your new interface activity
        startActivity(intent)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (isVerticalFling(velocityX, velocityY)) {
                actionTextView.text = "Latest Action: Vertical Fling"
            } else {
                actionTextView.text = "Latest Action: Horizontal Fling"
            }
            return true
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            actionTextView.text = "Latest Action: Scroll (dx: $distanceX, dy: $distanceY)"
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            actionTextView.text = "Latest Action: Long Press"
        }

        override fun onDown(e: MotionEvent): Boolean {
            actionTextView.text = "Latest Action: Down"
            return true
        }

        private fun isVerticalFling(velocityX: Float, velocityY: Float): Boolean {
            val angle = Math.toDegrees(Math.atan2(velocityY.toDouble(), velocityX.toDouble()))
            val direction = when {
                angle < -45 && angle > -135 -> "upward"
                angle > 45 && angle < 135 -> "downward"
                else -> "horizontal"
            }
            actionTextView.text = "Latest Action: $direction fling"
            return direction == "upward" || direction == "downward"
        }

    }
}


/*
    fun openAudio(view: View) {
        val intent = Intent(this, AudioActivity::class.java) // Replace with the name of your new interface activity
        startActivity(intent)
    }
 */