package com.example.canvas.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.example.canvas.models.DrawingSuggester
import com.example.canvas.models.DrawingSuggestingItem
import com.example.canvas.models.NotePoint
import com.example.canvas.models.layer.SimpleStrokesLayer

class SuggestQuickView(
    context: Context?,
    attributeSet: AttributeSet? = null
) : View(context, attributeSet) {

    private val drawingSuggester: DrawingSuggester = DrawingSuggester()

    override fun onDraw(canvas: Canvas) {
        val drawer = internalConstructShapeOf("cat")
        drawer?.onDraw(canvas)
    }

    private fun internalConstructShapeOf(result: String): SimpleStrokesLayer? {
        return drawingSuggester.getDrawingOf(result.toLowerCase())?.let { obj ->
            val shape = SimpleStrokesLayer()

            val suggestionPaint = DrawingSuggestingItem(obj)

            suggestionPaint.getListScaledPoint(null, null)
                ?.onEach { listPoints ->
                    listPoints.firstOrNull()?.also { firstPoint ->
                        shape.moveStrokeTo(NotePoint(firstPoint.x, firstPoint.y))
                    }

                    listPoints.forEach { point ->
                        val x = point.x
                        val y = point.y
                        shape.lineStrokeTo(NotePoint(x, y))
                    }
                }
            shape
        }
    }

}