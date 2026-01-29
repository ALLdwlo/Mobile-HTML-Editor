package com.trebedit.webcraft.editor.undo

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UndoRedoManager {
    
    private val _canUndo = MutableStateFlow(false)
    val canUndo: StateFlow<Boolean> = _canUndo.asStateFlow()
    
    private val _canRedo = MutableStateFlow(false)
    val canRedo: StateFlow<Boolean> = _canRedo.asStateFlow()
    
    fun updateState(canUndo: Boolean, canRedo: Boolean) {
        _canUndo.value = canUndo
        _canRedo.value = canRedo
    }
    
    fun reset() {
        _canUndo.value = false
        _canRedo.value = false
    }
}
