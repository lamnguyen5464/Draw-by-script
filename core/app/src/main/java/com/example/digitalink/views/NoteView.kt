package com.example.digitalink.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.digitalink.models.layer.DoubleBufferLayer
import com.example.digitalink.models.layer.SuggestionLayer
import kotlinx.coroutines.*

class NoteView @JvmOverloads constructor(
    context: Context?,
    attributeSet: AttributeSet? = null
) :
    View(context, attributeSet) {

    private val suggestionLayer = context?.let { SuggestionLayer(it) }
    private var doubleBufferBaseLayer: DoubleBufferLayer? = null
    private val debounceSuggestingScope = CoroutineScope(Dispatchers.IO + Job())
    private var suggestingJob: Job? = null

    private fun confirmDrawing() {
        suggestionLayer?.let {
            doubleBufferBaseLayer?.accumulate(it)
            it.onClear()
            startSuggesting()
        }
        invalidate()
    }

    private fun startSuggesting() {
        if (suggestionLayer?.isEmpty() == false) {
            suggestingJob?.cancel()
            suggestingJob = debounceSuggestingScope.launch {
                delay(1000)
                suggest()
            }
        }
    }

    fun suggest() {
        suggestionLayer?.recognize {
            confirmDrawing()
        }
    }

    override fun onSizeChanged(
        width: Int,
        height: Int,
        oldWidth: Int,
        oldHeight: Int
    ) {
        doubleBufferBaseLayer = DoubleBufferLayer(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        doubleBufferBaseLayer?.onDraw(canvas)
        suggestionLayer?.onDraw(canvas)
        startSuggesting()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        suggestionLayer?.onMotionEvent(event)
        invalidate()
        return true
    }

    fun clear() {
        doubleBufferBaseLayer?.onClear()
        suggestionLayer?.onClear()
        invalidate()
    }
}