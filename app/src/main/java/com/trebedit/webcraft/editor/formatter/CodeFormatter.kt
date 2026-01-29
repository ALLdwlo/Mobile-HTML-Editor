package com.trebedit.webcraft.editor.formatter

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CodeFormatter {
    
    private val _isFormatting = MutableStateFlow(false)
    val isFormatting: StateFlow<Boolean> = _isFormatting.asStateFlow()
    
    private val _lastFormatSuccess = MutableStateFlow<Boolean?>(null)
    val lastFormatSuccess: StateFlow<Boolean?> = _lastFormatSuccess.asStateFlow()
    
    fun setFormattingState(isFormatting: Boolean) {
        _isFormatting.value = isFormatting
    }
    
    fun onFormatComplete(success: Boolean) {
        _isFormatting.value = false
        _lastFormatSuccess.value = success
    }
    
    fun reset() {
        _isFormatting.value = false
        _lastFormatSuccess.value = null
    }
}
