package com.example.digitalink.models.layer

import android.graphics.Canvas
import android.graphics.Path
import android.view.MotionEvent
import com.example.digitalink.models.Point
import com.example.digitalink.models.StrokeStyle
import com.example.digitalink.models.lineTo
import com.example.digitalink.models.moveTo

open class SimpleStrokesLayer(
    internal val stroke: Path = Path(),
    internal val strokeStyle: StrokeStyle = StrokeStyle.defaultBlackStroke
) : Layer() {

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(stroke, strokeStyle.getStyle())
        this.childrenLayers.forEach {
            it.onDraw(canvas)
        }
    }

    override fun onClear() {
        this.stroke.reset()
    }

    override fun onMotionEvent(event: MotionEvent) {
        val action = event.actionMasked
        val x = event.x
        val y = event.y
        when (action) {
            MotionEvent.ACTION_DOWN -> moveStrokeTo(Point(x, y))
            MotionEvent.ACTION_MOVE,
            MotionEvent.ACTION_UP -> lineStrokeTo(Point(x, y))
            else -> {}
        }
    }

    internal fun moveStrokeTo(point: Point) {
        this.stroke.moveTo(point)
    }

    internal fun lineStrokeTo(point: Point) {
        this.stroke.lineTo(point)
    }
}