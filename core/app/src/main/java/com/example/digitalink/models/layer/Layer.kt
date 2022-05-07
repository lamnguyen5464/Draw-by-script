package com.example.digitalink.models.layer

import android.graphics.Canvas
import android.view.MotionEvent
import com.example.digitalink.models.Point

abstract class Layer {
    var alignTopLeft: Point? = null
    var alignBottomRight: Point? = null
    private var level: Int? = null
    protected var childrenLayers: MutableList<Layer> = mutableListOf()

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

    fun extend(currentPoint: Point) {
        alignTopLeft?.let {
            if (currentPoint.x < it.x) it.x = currentPoint.x
            if (currentPoint.y < it.y) it.y = currentPoint.y
        }

        alignBottomRight?.let {
            if (currentPoint.x > it.x) it.x = currentPoint.x
            if (currentPoint.y > it.y) it.y = currentPoint.y
        }

        if (alignTopLeft == null) {
            alignTopLeft = currentPoint.clone()
        }

        if (alignBottomRight == null) {
            alignBottomRight = currentPoint.clone()
        }
    }
}