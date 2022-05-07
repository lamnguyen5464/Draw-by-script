package com.example.digitalink.models

import org.json.JSONObject

class DrawingSuggestingItem(private val data: JSONObject) {

    private val bottomRightPoint: NotePoint = NotePoint()
    private val internalData: List<List<NotePoint>>? = internalGetListPoint()

    fun getListPoint(): List<List<NotePoint>>? = internalData

    fun getListScaledPoint(
        baseTopLeft: NotePoint?,
        baseBottomRight: NotePoint?
    ): List<List<NotePoint>>? {
        return this.internalData?.map { listStroke ->
            listStroke.map { point ->
                internalScalePoint(baseTopLeft, baseBottomRight, point)
            }
        }
    }

    private fun internalScalePoint(
        baseTopLeft: NotePoint?,
        baseBottomRight: NotePoint?,
        point: NotePoint
    ): NotePoint {
        if (baseTopLeft == null || baseBottomRight == null) return point
        val ratioX: Float = (baseBottomRight.x - baseTopLeft.x) / (bottomRightPoint.x)
        val ratioY: Float = (baseBottomRight.y - baseTopLeft.y) / (bottomRightPoint.y)
        /**
         * suggestX -> ?
         * maxSuggestX -> maxFrameX
         */

        return NotePoint(
            x = point.x * ratioX + baseTopLeft.x,
            y = point.y * ratioY + baseTopLeft.y
        )
    }

    private fun internalGetListPoint(): List<List<NotePoint>>? = try {
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

                if (bottomRightPoint.x < x) bottomRightPoint.x = x
                if (bottomRightPoint.y < y) bottomRightPoint.y = y
            }
            strokes.add(notePoints)
        }
        strokes
    } catch (_: Exception) {
        null
    }
}