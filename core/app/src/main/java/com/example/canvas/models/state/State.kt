package com.example.canvas.models.state

interface State<T> {
    fun clone(): T
}