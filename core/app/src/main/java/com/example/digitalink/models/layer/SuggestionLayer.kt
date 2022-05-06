package com.example.digitalink.models.layer

import android.content.Context
import android.view.MotionEvent
import com.example.digitalink.StrokeManager
import com.example.digitalink.models.DrawingSuggestion
import com.example.digitalink.models.Point
import com.google.mlkit.vision.digitalink.Ink

class SuggestionLayer(
    context: Context,
    private var inkBuilder: Ink.Builder = Ink.builder(),
    private var strokeBuilder: Ink.Stroke.Builder = Ink.Stroke.builder(),
    private val drawingSuggestion: DrawingSuggestion = DrawingSuggestion(context)
) : SimpleStrokesLayer() {

    fun recognize(onDone: () -> Unit) {
        StrokeManager.recognize(inkBuilder) {
            val result = it.candidates[0].text

            it.candidates.forEach { res -> println("@@ $res") }

            internalConstructShapeOf(result)
            onDone()
        }
    }

    private fun internalConstructShapeOf(result: String) {
        drawingSuggestion.getDrawingOf(result.toLowerCase())?.let { obj ->
            this.stroke.reset()

            val drawing = obj.getJSONArray("drawing")

            val paddingLeft = alignTopLeft?.x ?: 0f
            val paddingTop = alignTopLeft?.y ?: 0f

            for (i in 0 until drawing.length()) {
                val stroke = drawing.getJSONArray(i)
                val listX = stroke.getJSONArray(0)
                val listY = stroke.getJSONArray(1)

                var x = listX.getDouble(0) + paddingLeft
                var y = listY.getDouble(0) + paddingTop
                this.stroke.moveTo(x.toFloat(), y.toFloat())

                for (j in 1 until listX.length()) {
                    x = listX.getDouble(j) + paddingLeft
                    y = listY.getDouble(j) + paddingTop
                    this.stroke.lineTo(x.toFloat(), y.toFloat())
                }
            }
        }
    }

    override fun onMotionEvent(event: MotionEvent) {
        super.onMotionEvent(event)

        val action = event.actionMasked
        val x = event.x
        val y = event.y
        val t = System.currentTimeMillis()
        val point = Point(x, y)

        when (action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                strokeBuilder.addPoint(Ink.Point.create(x, y, t))
                this.extend(point)
            }
            MotionEvent.ACTION_UP -> {
                strokeBuilder.addPoint(Ink.Point.create(x, y, t))
                inkBuilder.addStroke(strokeBuilder.build())
                strokeBuilder = Ink.Stroke.builder()

            }
            else -> {}
        }
    }

    override fun onClear() {
        super.onClear()
        inkBuilder = Ink.builder()
    }
}