package com.example.canvas.di

import android.content.Context
import com.example.canvas.R

object Providers {
    var applicationContext: Context? = null

    val dataSuggestionRaw by lazy {
        applicationContext?.resources?.openRawResource(R.raw.drawing_data)
            ?.bufferedReader().use { it?.readText() } ?: ""
    }
}