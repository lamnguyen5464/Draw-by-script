package com.example.canvas

import android.app.Activity
import android.os.Bundle
import com.example.canvas.di.Providers
import com.example.canvas.utils.StrokeManager
import com.example.canvas.views.SuggestQuickView
import kotlinx.android.synthetic.main.note_play_ground.*

class NotePlayGround : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Providers.applicationContext = applicationContext

        setContentView(R.layout.note_play_ground)
        StrokeManager.download()

        undo.setOnClickListener {
            drawingView.undo()
        }

        clear.setOnClickListener {
            drawingView.clear()
        }

        (0..20).forEach {

            val quickView = SuggestQuickView(applicationContext)
            quickView.minimumWidth = 100
            quickView.minimumHeight = 200

            quickView.setOnClickListener {

            }

            listQuickSuggestions.addView(quickView)
        }

    }
}