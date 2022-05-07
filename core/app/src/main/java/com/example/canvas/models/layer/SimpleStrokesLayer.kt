package com.example.canvas.models.layer

import android.graphics.Canvas
import android.graphics.Path
import android.view.MotionEvent
import com.example.canvas.models.NotePoint
import com.example.canvas.models.StrokeStyleHolder
import com.example.canvas.models.lineTo
import com.example.canvas.models.moveTo

open class SimpleStrokesLayer(
    internal val stroke: Path = Path(),
    internal val strokeStyleHolder: StrokeStyleHolder = StrokeStyleHolder.defaultBlackStroke,
    alignTopLeft: NotePoint? = null,
    alignBottomRight: NotePoint? = null
) : Layer(alignTopLeft = alignTopLeft, alignBottomRight = alignBottomRight) {

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(stroke, strokeStyleHolder.getStyle())
        this.childrenLayers.forEach {
            it.onDraw(canvas)
        }
    }

    override fun onClear() {
        super.onClear()
        this.stroke.reset()
    }

    override fun onMotionEvent(event: MotionEvent) {
        val action = event.actionMasked
        val x = event.x
        val y = event.y
        when (action) {
            MotionEvent.ACTION_DOWN -> moveStrokeTo(NotePoint(x, y))
            MotionEvent.ACTION_MOVE,
            MotionEvent.ACTION_UP -> lineStrokeTo(NotePoint(x, y))
            else -> {}
        }
    }

    internal fun moveStrokeTo(notePoint: NotePoint) {
        this.stroke.moveTo(notePoint)
    }

    internal fun lineStrokeTo(notePoint: NotePoint) {
        this.stroke.lineTo(notePoint)
    }

    fun accumulate(layer: SimpleStrokesLayer) {
        this.stroke.addPath(layer.stroke)
    }

    fun isEmpty() = this.stroke.isEmpty
}