package com.example.digitalink.models

import android.content.Context
import com.example.digitalink.R
import org.json.JSONObject

class DrawingSuggester(context: Context) {
    private val dataString = context.resources.openRawResource(R.raw.drawing_data)
        .bufferedReader().use { it.readText() }

    private val data = try {
        JSONObject(dataString)
    } catch (_: Exception) {
        JSONObject()
    }

    fun getDrawingOf(name: String): JSONObject? {
        return try {
            val listResult = data.getJSONArray(name)
            val length = listResult.length()
            val stringData = listResult.getString((0 until length).random())
            JSONObject(stringData)
        } catch (_: Exception) {
            null
        }
    }

}