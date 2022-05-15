package com.onlineup.core.canvas.di

import android.content.Context
import com.onlineup.core.canvas.models.DrawingSuggester
import com.onlineup.R

object Providers {
    var applicationContext: Context? = null

    val dataSuggestionRaw by lazy {
        applicationContext?.resources?.openRawResource(R.raw.drawing_data)
            ?.bufferedReader().use { it?.readText() } ?: ""
    }

    val drawingSuggester by lazy {
        DrawingSuggester(dataString = dataSuggestionRaw)
    }
}