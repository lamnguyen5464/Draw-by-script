package com.example.canvas.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.canvas.di.Providers
import com.example.canvas.models.layer.DoubleBufferLayer
import com.example.canvas.models.layer.SimpleStrokesLayer
import com.example.canvas.models.layer.SuggestionLayer
import com.example.canvas.models.state.StateHolder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow

class NoteCanvas @JvmOverloads constructor(
    context: Context?,
    attributeSet: AttributeSet? = null
) : View(context, attributeSet) {

    private val suggestionLayer = SuggestionLayer()
    private var latestUpdatingLayer: SimpleStrokesLayer? = null
    private val debounceSuggestingScope = CoroutineScope(Dispatchers.IO + Job())
    private var suggestingJob: Job? = null
    private val drawingSuggester = Providers.drawingSuggester

    private val suggestionEmitter = MutableStateFlow("")

    fun onEmittingSuggestion() = suggestionEmitter

    private val baseLayerState = StateHolder(
        initStateProducer = { DoubleBufferLayer() },
        triggerOnChange = { invalidate() }
    )

    fun onUpdateSuggestionDrawing(tag: String, index: Int) {
        val newShape = drawingSuggester.constructShapeOf(
            tag = tag,
            index = index,
            baseTopLeft = latestUpdatingLayer?.alignTopLeft,
            baseBottomRight = latestUpdatingLayer?.alignBottomRight
        )
        println("@@ " + latestUpdatingLayer?.alignBottomRight)
        if (newShape != null) {
            baseLayerState.modifyNearestOfLastState { baseLayer ->
                baseLayer.accumulate(newShape)
            }
            suggestionEmitter.value = ""
        }
    }

    private fun confirmDrawing() {
        suggestionEmitter.value = ""
        suggestionLayer.recognize { tag ->

            latestUpdatingLayer = suggestionLayer.clone()

            baseLayerState.modifyLastState {
                it.accumulate(suggestionLayer)
                suggestionLayer.onClear()
            }


            if (tag.isNotEmpty()) {
                suggestionEmitter.value = tag
//                baseLayerState.modifyNearestOfLastState {
//                    it.accumulate(recognizedShape)
//                }
            }
            startSuggesting()
        }
    }

    private fun startSuggesting() {
        if (!suggestionLayer.isEmpty()) {
            suggestingJob?.cancel()
            suggestingJob = debounceSuggestingScope.launch {
                delay(700)
                confirmDrawing()
            }
        }
    }

    override fun onSizeChanged(
        width: Int,
        height: Int,
        oldWidth: Int,
        oldHeight: Int
    ) {
        baseLayerState.setState(DoubleBufferLayer(width, height))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        baseLayerState.getState().onDraw(canvas)
        suggestionLayer.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        suggestionLayer.onMotionEvent(event)
        startSuggesting()
        invalidate()
        return true
    }

    fun undo() {
        if (baseLayerState.canRollBack()) {
            baseLayerState.rollback()
        } else {
            Toast.makeText(context, "Cannot undo", Toast.LENGTH_SHORT).show()
        }
    }

    fun clear() {
        suggestionLayer.onClear()
        baseLayerState.modifyLastState {
            it.onClear()
        }
    }
}