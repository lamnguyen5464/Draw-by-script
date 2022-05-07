package com.example.digitalink.models

import org.json.JSONObject

class DrawingSuggestionItem(private val data: JSONObject) {

    fun getListPoint(): List<List<Point>>? = try {
        val drawing = data.getJSONArray("drawing")
        val strokes = mutableListOf<List<Point>>()

        for (i in 0 until drawing.length()) {
            val points = mutableListOf<Point>()
            val stroke = drawing.getJSONArray(i)
            val listX = stroke.getJSONArray(0)
            val listY = stroke.getJSONArray(1)

            for (j in 0 until listX.length()) {
                val x = listX.getDouble(j).toFloat()
                val y = listY.getDouble(j).toFloat()
                points.add(Point(x, y))
            }
            strokes.add(points)
        }
        strokes
    } catch (_: Exception) {
        null
    }
}