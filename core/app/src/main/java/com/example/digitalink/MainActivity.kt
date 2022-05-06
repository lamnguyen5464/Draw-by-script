package com.example.digitalink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StrokeManager.download()
        recognize.setOnClickListener {
            drawingView.suggest()
//            StrokeManager.recognize(this) {
//                drawingView.clear()
//                drawingView.drawTag(it.candidates[0].text)
//            }
        }
        clear.setOnClickListener {
            drawingView.clear()
        }
    }
}