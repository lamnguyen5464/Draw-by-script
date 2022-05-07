package com.example.digitalink.models.layer

import android.content.Context
import android.graphics.Path
import android.view.MotionEvent
import com.example.digitalink.utils.StrokeManager
import com.example.digitalink.models.DrawingSuggester
import com.example.digitalink.models.DrawingSuggestingItem
import com.example.digitalink.models.NotePoint
import com.example.digitalink.models.StrokeStyleHolder
import com.google.mlkit.vision.digitalink.Ink

class SuggestionLayer(
    context: Context,
    private var inkBuilder: Ink.Builder = Ink.builder(),
    private var strokeBuilder: Ink.Stroke.Builder = Ink.Stroke.builder(),
    private val drawingSuggester: DrawingSuggester = DrawingSuggester(context),
    stroke: Path = Path(),
    strokeStyleHolder: StrokeStyleHolder = StrokeStyleHolder.tempStroke
) : SimpleStrokesLayer(stroke, strokeStyleHolder) {

    fun recognize(onDone: () -> Unit) {
        StrokeManager.recognize(inkBuilder) {
            val result = it.candidates[0].text
            internalConstructShapeOf(result)
            onDone()
        }
    }

    private fun internalConstructShapeOf(result: String) {
        drawingSuggester.getDrawingOf(result.toLowerCase())?.let { obj ->
            this.stroke.reset()

            val suggestionPaint = DrawingSuggestingItem(obj)

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
        val notePoint = NotePoint(x, y)

        when (action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                strokeBuilder.addPoint(Ink.Point.create(x, y, t))
                this.extend(notePoint)
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