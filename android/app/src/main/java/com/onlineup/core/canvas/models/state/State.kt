package com.onlineup.core.canvas.models.state

interface State<T> {
    fun clone(): T
}