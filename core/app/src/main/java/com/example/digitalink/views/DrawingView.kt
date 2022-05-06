package com.example.digitalink.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.digitalink.StrokeManager
import com.example.digitalink.models.DrawingSuggestion
import com.example.digitalink.models.layer.DoubleBufferLayer
import com.example.digitalink.models.layer.SimpleStrokesLayer

class DrawingView @JvmOverloads constructor(
    context: Context?,
    attributeSet: AttributeSet? = null
) :
    View(context, attributeSet) {
    private lateinit var canvasBitmap: Bitmap
    private val drawingSuggestion = context?.let { DrawingSuggestion(it) }

    private val baseLayer = SimpleStrokesLayer()

    private var doubleBufferBaseLayer: DoubleBufferLayer? = null

    private var currentTag: String? = null

    fun drawTag(tag: String) {
        currentTag = tag
        invalidate()
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
        StrokeManager.addNewTouchEvent(event)
        invalidate()
        return true
    }

    companion object {
        private const val STROKE_WIDTH_DP = 5
    }

    fun clear() {
        currentTag = null
        doubleBufferBaseLayer?.onClear()
        invalidate()
    }
}