package com.onlineup.core.canvas.models.layer

import android.graphics.Path
import android.view.MotionEvent
import com.onlineup.core.canvas.di.Providers
import com.onlineup.core.canvas.models.DrawingSuggester
import com.onlineup.core.canvas.models.StrokeStyleHolder
import com.onlineup.core.canvas.utils.StrokeManager
import com.google.mlkit.vision.digitalink.Ink

class SuggestionLayer(
    private var inkBuilder: Ink.Builder = Ink.builder(),
    private var strokeBuilder: Ink.Stroke.Builder = Ink.Stroke.builder(),
    private val drawingSuggester: DrawingSuggester = Providers.drawingSuggester,
    stroke: Path = Path(),
    strokeStyleHolder: StrokeStyleHolder = StrokeStyleHolder.tempStroke
) : SimpleStrokesLayer(stroke, strokeStyleHolder) {

    fun recognize(onDone: (String) -> Unit) {
        StrokeManager.recognize(inkBuilder) {
            val result = it.candidates[0].text
            onDone(
                if (drawingSuggester.hasTag(result)) result else ""
            )
        }
    }

    override fun onMotionEvent(event: MotionEvent) {
        super.onMotionEvent(event)

        val action = event.actionMasked
        val x = event.x
        val y = event.y
        val t = System.currentTimeMillis()

        when (action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                strokeBuilder.addPoint(Ink.Point.create(x, y, t))
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