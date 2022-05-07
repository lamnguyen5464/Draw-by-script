package com.example.digitalink.models

import org.json.JSONObject

class DrawingSuggestingItem(private val data: JSONObject) {

    fun getListPoint(): List<List<NotePoint>>? = try {
        val drawing = data.getJSONArray("drawing")
        val strokes = mutableListOf<List<NotePoint>>()

        for (i in 0 until drawing.length()) {
            val notePoints = mutableListOf<NotePoint>()
            val stroke = drawing.getJSONArray(i)
            val listX = stroke.getJSONArray(0)
            val listY = stroke.getJSONArray(1)

            for (j in 0 until listX.length()) {
                val x = listX.getDouble(j).toFloat()
                val y = listY.getDouble(j).toFloat()
                notePoints.add(NotePoint(x, y))
            }
            strokes.add(notePoints)
        }
        strokes
    } catch (_: Exception) {
        null
    }
}