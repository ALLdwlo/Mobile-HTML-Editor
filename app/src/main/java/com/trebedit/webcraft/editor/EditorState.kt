package com.trebedit.webcraft.editor

sealed class EditorState {
    object Loading : EditorState()
    data class Loaded(
        val fileId: String,
        val filename: String,
        val content: String,
        val language: String
    ) : EditorState()
    data class Error(val message: String) : EditorState()
}
