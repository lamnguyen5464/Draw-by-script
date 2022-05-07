package com.example.digitalink

import android.app.Activity
import android.os.Bundle
import com.example.digitalink.utils.StrokeManager
import kotlinx.android.synthetic.main.note_play_ground.*

class NotePlayGround : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_play_ground)
        StrokeManager.download()

        undo.setOnClickListener {
            drawingView.undo()
        }

        clear.setOnClickListener {
            drawingView.clear()
        }
    }
}