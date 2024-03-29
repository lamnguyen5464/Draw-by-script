package com.example.canvas

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.marginTop
import com.example.canvas.di.Providers
import com.example.canvas.utils.StrokeManager
import com.example.canvas.viewModels.NoteViewModel
import com.example.canvas.views.SuggestQuickView
import kotlinx.android.synthetic.main.note_play_ground.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NotePlayGround : Activity() {
    private val noteViewModel = NoteViewModel(this)

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

        listQuickSuggestionsView.visibility = View.INVISIBLE


        drawingView.onEmittingSuggestion()
            .onEach { tag ->
                this.runOnUiThread {
                    updateSuggestionView(tag)
                }
            }
            .launchIn(CoroutineScope(Dispatchers.IO + Job()))

    }

    private fun updateSuggestionView(tag: String) {
        if (tag.isEmpty()) {
            listQuickSuggestionsView.visibility = View.INVISIBLE
            listQuickSuggestions.removeAllViews()
        } else {
            listQuickSuggestionsView.visibility = View.VISIBLE
            listQuickSuggestions.removeAllViews()

            (0..10).forEach { index ->
                val quickView = SuggestQuickView(
                    context = applicationContext,
                    tag = tag,
                    index = index
                )
                quickView.minimumWidth = 200
                quickView.minimumHeight = 200

                quickView.setOnClickListener {
                    drawingView.onUpdateSuggestionDrawing(tag, index)
                }
                listQuickSuggestions.addView(quickView)
            }
        }
    }
}