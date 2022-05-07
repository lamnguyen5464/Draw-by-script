package com.example.digitalink.models.layer

import android.content.Context
import android.graphics.Path
import android.view.MotionEvent
import com.example.digitalink.StrokeManager
import com.example.digitalink.models.DrawingSuggestion
import com.example.digitalink.models.DrawingSuggestionItem
import com.example.digitalink.models.Point
import com.example.digitalink.models.StrokeStyle
import com.google.mlkit.vision.digitalink.Ink

class SuggestionLayer(
    context: Context,
    private var inkBuilder: Ink.Builder = Ink.builder(),
    private var strokeBuilder: Ink.Stroke.Builder = Ink.Stroke.builder(),
    private val drawingSuggestion: DrawingSuggestion = DrawingSuggestion(context),
    stroke: Path = Path(),
    strokeStyle: StrokeStyle = StrokeStyle.tempStroke
) : SimpleStrokesLayer(stroke, strokeStyle) {

    fun recognize(onDone: () -> Unit) {
        StrokeManager.recognize(inkBuilder) {
            val result = it.candidates[0].text
            internalConstructShapeOf(result)
            onDone()
        }
    }

    private fun internalConstructShapeOf(result: String) {
        drawingSuggestion.getDrawingOf(result.toLowerCase())?.let { obj ->
            this.stroke.reset()

            val suggestionPaint = DrawingSuggestionItem(obj)

            val paddingLeft = alignTopLeft?.x ?: 0f
            val paddingTop = alignTopLeft?.y ?: 0f

            suggestionPaint.getListPoint()?.onEach { listPoints ->
                listPoints.firstOrNull()?.also { firstPoint ->
                    this.stroke.moveTo(firstPoint.x + paddingLeft, firstPoint.y + paddingTop)
                }

                listPoints.forEach { point ->
                    val x = point.x + paddingLeft
                    val y = point.y + paddingTop
                    this.stroke.lineTo(x, y)
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