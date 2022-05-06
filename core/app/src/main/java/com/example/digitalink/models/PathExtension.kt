package com.example.digitalink.models

import android.graphics.Path

fun Path.lineTo(point: Point) {
    this.lineTo(point.x, point.y)
}

fun Path.moveTo(point: Point) {
    this.moveTo(point.x, point.y)
}