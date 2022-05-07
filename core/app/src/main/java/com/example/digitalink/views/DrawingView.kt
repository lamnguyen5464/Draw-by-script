package com.example.digitalink.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.digitalink.models.layer.DoubleBufferLayer
import com.example.digitalink.models.layer.SuggestionLayer

class DrawingView @JvmOverloads constructor(
    context: Context?,
    attributeSet: AttributeSet? = null
) :
    View(context, attributeSet) {

    private val suggestionLayer = context?.let { SuggestionLayer(it) }
    private var doubleBufferBaseLayer: DoubleBufferLayer? = null

    fun suggest() {
        suggestionLayer?.recognize {
            doubleBufferBaseLayer?.accumulate(suggestionLayer)
            invalidate()
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
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
//        doubleBufferBaseLayer?.onMotionEvent(event)
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