package com.example.digitalink.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.digitalink.models.layer.DoubleBufferLayer
import com.example.digitalink.models.layer.SuggestionLayer
import com.example.digitalink.models.state.StateHolder
import kotlinx.coroutines.*

class NoteView @JvmOverloads constructor(
    context: Context?,
    attributeSet: AttributeSet? = null
) : View(context, attributeSet) {

    private val suggestionLayer = context?.let { SuggestionLayer(it) }
    private val debounceSuggestingScope = CoroutineScope(Dispatchers.IO + Job())
    private var suggestingJob: Job? = null


    private val baseLayerState = StateHolder(
        initStateProducer = { DoubleBufferLayer() },
        triggerOnChange = { invalidate() }
    )

    private fun confirmDrawing() {
        suggestionLayer?.let {
            suggestionLayer.recognize { recognizedShape ->
                val currentLayer = baseLayerState.getState().clone()
                val currentLayerRaw = currentLayer.clone()
                currentLayerRaw.accumulate(suggestionLayer)
                suggestionLayer.onClear()
                baseLayerState.setState(state = currentLayerRaw)

                recognizedShape?.let {
                    val layerAuto = currentLayer.clone()
                    layerAuto.accumulate(recognizedShape)
                    baseLayerState.setState(state = layerAuto)
                }
                startSuggesting()
            }
        }
    }

    private fun startSuggesting() {
        if (suggestionLayer?.isEmpty() == false) {
            suggestingJob?.cancel()
            suggestingJob = debounceSuggestingScope.launch {
                delay(500)
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
        baseLayerState.getState().onDraw(canvas)
        suggestionLayer?.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        suggestionLayer?.onMotionEvent(event)
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
        suggestionLayer?.onClear()
        baseLayerState.modifyState {
            it.onClear()
        }
    }
}