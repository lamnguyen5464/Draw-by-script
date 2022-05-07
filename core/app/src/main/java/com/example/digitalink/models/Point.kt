package com.example.digitalink.models

data class Point(var x: Float = 0f, var y: Float = 0f) {
    fun clone(): Point = Point(x, y)
}