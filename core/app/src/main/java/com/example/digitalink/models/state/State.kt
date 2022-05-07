package com.example.digitalink.models.state

interface State<T> {
    fun clone(): T
}