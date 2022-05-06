package com.example.digitalink.models.layer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import com.example.digitalink.models.Point
import com.example.digitalink.models.StrokeStyle

class DoubleBufferLayer(
    private val width: Int,
    private val height: Int,
    private val stroke: Path = Path(),
    private val strokeStyle: StrokeStyle = StrokeStyle.defaultBlackStroke,
    private val canvasStyle: Paint = Paint(Paint.DITHER_FLAG)
) : SimpleStrokesLayer(stroke, strokeStyle) {

    private var cacheCanvas: Canvas = Canvas()
    private lateinit var cacheBitMap: Bitmap

    init {
        onCreate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(cacheBitMap, 0f, 0f, canvasStyle)
        super.onDraw(canvas)

    }

    override fun onMotionEvent(event: MotionEvent) {
        val action = event.actionMasked
        val x = event.x
        val y = event.y
        when (action) {
            MotionEvent.ACTION_DOWN -> super.moveStrokeTo(Point(x, y))
            MotionEvent.ACTION_MOVE -> super.lineStrokeTo(Point(x, y))
            MotionEvent.ACTION_UP -> {
                lineStrokeTo(Point(x, y))
                cacheCanvas.drawPath(this.stroke, this.strokeStyle.getStyle())
                this.stroke.reset()
            }
            else -> {}
        }
    }

    override fun onClear() {
        super.onClear()
        onCreate()
    }

    private fun onCreate() {
        cacheBitMap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        cacheCanvas = Canvas(cacheBitMap)
    }


}