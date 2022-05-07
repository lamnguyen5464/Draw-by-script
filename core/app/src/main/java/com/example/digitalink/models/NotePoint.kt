package com.example.digitalink.models

data class NotePoint(var x: Float = 0f, var y: Float = 0f) {
    fun clone(): NotePoint = NotePoint(x, y)
}