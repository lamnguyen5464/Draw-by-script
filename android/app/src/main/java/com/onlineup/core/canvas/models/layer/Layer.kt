package com.onlineup.core.canvas.models.layer

import android.graphics.Canvas
import android.view.MotionEvent
import com.onlineup.core.canvas.models.NotePoint

abstract class Layer(
    var alignTopLeft: NotePoint? = null,
    var alignBottomRight: NotePoint? = null,
    private var level: Int? = null,
    protected var childrenLayers: MutableList<Layer> = mutableListOf()
) {

    abstract fun onDraw(canvas: Canvas)
    abstract fun onMotionEvent(event: MotionEvent)
    open fun onClear() {
        alignTopLeft = null
        alignBottomRight = null
    }

    fun append(aboveLayer: Layer) {
        if (aboveLayer.level != null) {
            return
        }
        aboveLayer.level = (level ?: 0) + 1
        childrenLayers.add(aboveLayer)
    }

    fun remove(layer: Layer): Boolean {
        return childrenLayers.remove(layer)
    }

    fun isFulFilled(): Boolean = this.alignBottomRight == null || this.alignTopLeft == null

    fun extend(currentNotePoint: NotePoint) {
        alignTopLeft?.let {
            if (currentNotePoint.x < it.x) it.x = currentNotePoint.x
            if (currentNotePoint.y < it.y) it.y = currentNotePoint.y
        }

        alignBottomRight?.let {
            if (currentNotePoint.x > it.x) it.x = currentNotePoint.x
            if (currentNotePoint.y > it.y) it.y = currentNotePoint.y
        }

        if (alignTopLeft == null) {
            alignTopLeft = currentNotePoint.clone()
        }

        if (alignBottomRight == null) {
            alignBottomRight = currentNotePoint.clone()
        }
    }
}