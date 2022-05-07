package com.example.digitalink.models

import android.graphics.Path

fun Path.lineTo(notePoint: NotePoint) {
    this.lineTo(notePoint.x, notePoint.y)
}

fun Path.moveTo(notePoint: NotePoint) {
    this.moveTo(notePoint.x, notePoint.y)
}