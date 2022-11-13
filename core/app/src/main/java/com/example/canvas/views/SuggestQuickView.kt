package com.example.canvas.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.example.canvas.di.Providers
import com.example.canvas.models.DrawingSuggester
import com.example.canvas.models.NotePoint
import com.example.canvas.models.layer.SimpleStrokesLayer

class SuggestQuickView(
    context: Context?,
    attributeSet: AttributeSet? = null,
    private val tag: String,
    private val index: Int
) : View(context, attributeSet) {

    private val drawingSuggester: DrawingSuggester = Providers.drawingSuggester

    override fun onDraw(canvas: Canvas) {
        val drawer = drawingSuggester.constructShapeOf(
            tag = tag,
            index = index,
            baseTopLeft = NotePoint(0f, 0f),
            baseBottomRight = NotePoint(200f, 200f)
        )
        drawer?.onDraw(canvas)
    }

    fun getShape(): SimpleStrokesLayer? = drawingSuggester.constructShapeOf(tag, index)
}