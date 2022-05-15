package com.onlineup.core.canvas.models

import android.graphics.Paint

class StrokeStyleHolder(private val paint: Paint = Paint()) {
    companion object {
        val defaultBlackStroke = StrokeStyleHolder(Paint().apply {
            color = -0x1000000 // black
            isAntiAlias = true
            // Set stroke width based on display density.
            strokeWidth = 10f
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        })

        val tempStroke = StrokeStyleHolder(Paint().apply {
            color = -0x12f2f2f // black
            isAntiAlias = true
            // Set stroke width based on display density.
            strokeWidth = 10f
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        })
    }

    fun getStyle(): Paint = paint
}
