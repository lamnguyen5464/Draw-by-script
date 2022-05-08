package com.example.canvas.models

import com.example.canvas.di.Providers
import com.example.canvas.models.layer.SimpleStrokesLayer
import org.json.JSONObject

class DrawingSuggester(
    dataString: String = Providers.dataSuggestionRaw
) {

    private val data = try {
        JSONObject(dataString)
    } catch (_: Exception) {
        JSONObject()
    }

    fun hasTag(tag: String): Boolean {
        return try {
            data.getJSONArray(tag)
            true
        } catch (_: Exception) {
            false
        }
    }

    private fun getDrawingOf(name: String, index: Int = 0): JSONObject? {
        return try {
            val listResult = data.getJSONArray(name)
            val length = listResult.length()
            val indexToPick = if (index < length) index else (0 until length).random()
            val stringData = listResult.getString(indexToPick)
            JSONObject(stringData)
        } catch (_: Exception) {
            null
        }
    }

    fun constructShapeOf(
        tag: String,
        index: Int = 0,
        baseTopLeft: NotePoint? = null,
        baseBottomRight: NotePoint? = null
    ): SimpleStrokesLayer? {
        return this.getDrawingOf(tag.toLowerCase(), index)?.let { obj ->
            val shape = SimpleStrokesLayer()

            val suggestionPaint = DrawingSuggestingItem(obj)

            suggestionPaint.getListScaledPoint(baseTopLeft, baseBottomRight)
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