package com.trebedit.webcraft.editor.webview

import android.webkit.JavascriptInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditorJavascriptInterface(
    private val scope: CoroutineScope,
    private val onContentChanged: (String) -> Unit,
    private val onCursorPositionChanged: (Int, Int) -> Unit,
    private val onUndoRedoStateChanged: (Boolean, Boolean) -> Unit,
    private val onValidationComplete: (String) -> Unit,
    private val onFormatComplete: (Boolean) -> Unit,
    private val onEditorReady: () -> Unit
) {
    
    @JavascriptInterface
    fun onContentChanged(content: String) {
        scope.launch(Dispatchers.Main) {
            onContentChanged.invoke(content)
        }
    }
    
    @JavascriptInterface
    fun onCursorPositionChanged(line: Int, ch: Int) {
        scope.launch(Dispatchers.Main) {
            onCursorPositionChanged.invoke(line, ch)
        }
    }
    
    @JavascriptInterface
    fun onUndoRedoStateChanged(canUndo: Boolean, canRedo: Boolean) {
        scope.launch(Dispatchers.Main) {
            onUndoRedoStateChanged.invoke(canUndo, canRedo)
        }
    }
    
    @JavascriptInterface
    fun onValidationComplete(errorsJson: String) {
        scope.launch(Dispatchers.Main) {
            onValidationComplete.invoke(errorsJson)
        }
    }
    
    @JavascriptInterface
    fun onFormatComplete(success: Boolean) {
        scope.launch(Dispatchers.Main) {
            onFormatComplete.invoke(success)
        }
    }
    
    @JavascriptInterface
    fun onEditorReady() {
        scope.launch(Dispatchers.Main) {
            onEditorReady.invoke()
        }
    }
}
