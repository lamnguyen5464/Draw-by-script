package com.example.canvas.models.state

class StateHolder<T : State<T>>(
    initStateProducer: () -> T,
    private val triggerOnChange: () -> Any
) {
    private var currentState = initStateProducer()
    private val listOldStates: MutableList<T> = mutableListOf()

    fun canRollBack() = listOldStates.isNotEmpty()

    fun setState(state: T) {
        listOldStates.add(currentState.clone())
        currentState = state
        triggerOnChange()
    }

    fun modifyLastState(modifier: (T) -> Unit) {
        val stateToSave = currentState.clone()
        modifier(stateToSave)
        setState(stateToSave)
    }

    fun modifyNearestOfLastState(modifier: (T) -> Unit) {
        val stateToSave = listOldStates[listOldStates.size - 1].clone()
        modifier(stateToSave)
        setState(stateToSave)
    }

    fun getState(): T = currentState

    fun rollback() {
        if (canRollBack()) {
            currentState = listOldStates.removeAt(listOldStates.size - 1)
        }
        triggerOnChange()
    }
}