package com.example.canvas.models

import com.example.canvas.di.Providers
import org.json.JSONObject

class DrawingSuggester(dataString: String = Providers.dataSuggestionRaw) {

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