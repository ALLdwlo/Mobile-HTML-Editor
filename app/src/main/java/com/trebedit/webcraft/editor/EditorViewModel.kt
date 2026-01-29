package com.trebedit.webcraft.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trebedit.webcraft.editor.formatter.CodeFormatter
import com.trebedit.webcraft.editor.syntax.SyntaxHighlighter
import com.trebedit.webcraft.editor.undo.UndoRedoManager
import com.trebedit.webcraft.editor.validation.SyntaxValidator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditorViewModel : ViewModel() {
    
    private val _editorState = MutableStateFlow<EditorState>(EditorState.Loading)
    val editorState: StateFlow<EditorState> = _editorState.asStateFlow()
    
    private val _currentContent = MutableStateFlow("")
    val currentContent: StateFlow<String> = _currentContent.asStateFlow()
    
    private val _isDirty = MutableStateFlow(false)
    val isDirty: StateFlow<Boolean> = _isDirty.asStateFlow()
    
    private val _cursorPosition = MutableStateFlow(Pair(0, 0))
    val cursorPosition: StateFlow<Pair<Int, Int>> = _cursorPosition.asStateFlow()
    
    private val _fontSize = MutableStateFlow(14)
    val fontSize: StateFlow<Int> = _fontSize.asStateFlow()
    
    private val _theme = MutableStateFlow("default")
    val theme: StateFlow<String> = _theme.asStateFlow()
    
    val syntaxHighlighter = SyntaxHighlighter()
    val undoRedoManager = UndoRedoManager()
    val syntaxValidator = SyntaxValidator()
    val codeFormatter = CodeFormatter()
    
    private var autoSaveJob: Job? = null
    private var initialContent = ""
    
    fun loadFile(fileId: String, filename: String, content: String) {
        val language = syntaxHighlighter.detectLanguage(filename)
        syntaxHighlighter.setLanguage(language)
        
        _currentContent.value = content
        initialContent = content
        _isDirty.value = false
        
        _editorState.value = EditorState.Loaded(
            fileId = fileId,
            filename = filename,
            content = content,
            language = language
        )
    }
    
    fun onContentChanged(content: String) {
        _currentContent.value = content
        _isDirty.value = content != initialContent
        scheduleAutoSave()
    }
    
    fun onCursorPositionChanged(line: Int, ch: Int) {
        _cursorPosition.value = Pair(line, ch)
    }
    
    fun onUndoRedoStateChanged(canUndo: Boolean, canRedo: Boolean) {
        undoRedoManager.updateState(canUndo, canRedo)
    }
    
    fun onValidationComplete(errorsJson: String) {
        syntaxValidator.parseValidationResults(errorsJson)
    }
    
    fun onFormatComplete(success: Boolean) {
        codeFormatter.onFormatComplete(success)
    }
    
    fun setFontSize(size: Int) {
        val clampedSize = size.coerceIn(10, 24)
        _fontSize.value = clampedSize
    }
    
    fun increaseFontSize() {
        setFontSize(_fontSize.value + 2)
    }
    
    fun decreaseFontSize() {
        setFontSize(_fontSize.value - 2)
    }
    
    fun setTheme(theme: String) {
        _theme.value = theme
    }
    
    fun setDarkMode(isDark: Boolean) {
        _theme.value = if (isDark) "material" else "eclipse"
    }
    
    fun startFormatting() {
        codeFormatter.setFormattingState(true)
    }
    
    fun saveFile() {
        val state = _editorState.value
        if (state is EditorState.Loaded) {
            viewModelScope.launch {
                try {
                    initialContent = _currentContent.value
                    _isDirty.value = false
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    
    private fun scheduleAutoSave() {
        autoSaveJob?.cancel()
        autoSaveJob = viewModelScope.launch {
            delay(2000)
            if (_isDirty.value) {
                saveFile()
            }
        }
    }
    
    fun markAsSaved() {
        initialContent = _currentContent.value
        _isDirty.value = false
    }
    
    override fun onCleared() {
        super.onCleared()
        autoSaveJob?.cancel()
    }
}
