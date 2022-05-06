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
//        doubleBufferBaseLayer?.onDraw(canvas)
        suggestionLayer?.onDraw(canvas)


//        drawingSuggestion?.getDrawingOf(currentTag ?: "")?.let { obj ->
//
//            val drawing = obj.getJSONArray("drawing")
//
//            for (i in 0 until drawing.length()) {
//                val stroke = drawing.getJSONArray(i)
//                val listX = stroke.getJSONArray(0)
//                val listY = stroke.getJSONArray(1)
//
//                val x = listX.getDouble(0) + 100
//                val y = listY.getDouble(0) + 100
//                currentStroke.moveTo(x.toFloat(), y.toFloat())
//
//                for (j in 1 until listX.length()) {
//                    val x = listX.getDouble(j) + 100
//                    val y = listY.getDouble(j) + 100
//                    currentStroke.lineTo(x.toFloat(), y.toFloat())
//                }
//                drawCanvas.drawPath(currentStroke, currentStrokePaint)
//                currentStroke.reset()
//
//            }
//        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        doubleBufferBaseLayer?.onMotionEvent(event)
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