package com.example.kienldmbtvn.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<STATE>(
    initialState: STATE
) : ViewModel() {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state.asStateFlow()

    protected fun updateState(update: (STATE) -> STATE) {
        _state.update(update)
    }
}