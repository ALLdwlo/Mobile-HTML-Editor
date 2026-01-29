package com.trebedit.webcraft.editor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditorViewModelTest {
    
    private lateinit var viewModel: EditorViewModel
    private val testDispatcher = StandardTestDispatcher()
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = EditorViewModel()
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `initial state is Loading`() = runTest {
        assertTrue(viewModel.editorState.first() is EditorState.Loading)
    }
    
    @Test
    fun `loadFile updates state to Loaded`() = runTest {
        viewModel.loadFile("file1", "test.html", "<html></html>")
        advanceUntilIdle()
        
        val state = viewModel.editorState.first()
        assertTrue(state is EditorState.Loaded)
        
        val loaded = state as EditorState.Loaded
        assertEquals("file1", loaded.fileId)
        assertEquals("test.html", loaded.filename)
        assertEquals("<html></html>", loaded.content)
        assertEquals("html", loaded.language)
    }
    
    @Test
    fun `loadFile detects language from filename`() = runTest {
        viewModel.loadFile("file1", "styles.css", "body { }")
        advanceUntilIdle()
        
        val state = viewModel.editorState.first() as EditorState.Loaded
        assertEquals("css", state.language)
    }
    
    @Test
    fun `onContentChanged marks as dirty`() = runTest {
        viewModel.loadFile("file1", "test.html", "<html></html>")
        advanceUntilIdle()
        
        assertFalse(viewModel.isDirty.first())
        
        viewModel.onContentChanged("<html><body></body></html>")
        advanceUntilIdle()
        
        assertTrue(viewModel.isDirty.first())
    }
    
    @Test
    fun `onContentChanged not dirty if same as initial`() = runTest {
        viewModel.loadFile("file1", "test.html", "<html></html>")
        advanceUntilIdle()
        
        viewModel.onContentChanged("<html></html>")
        advanceUntilIdle()
        
        assertFalse(viewModel.isDirty.first())
    }
    
    @Test
    fun `onCursorPositionChanged updates cursor position`() = runTest {
        viewModel.onCursorPositionChanged(5, 10)
        
        val position = viewModel.cursorPosition.first()
        assertEquals(5, position.first)
        assertEquals(10, position.second)
    }
    
    @Test
    fun `onUndoRedoStateChanged updates manager`() = runTest {
        viewModel.onUndoRedoStateChanged(true, false)
        
        assertTrue(viewModel.undoRedoManager.canUndo.first())
        assertFalse(viewModel.undoRedoManager.canRedo.first())
    }
    
    @Test
    fun `setFontSize clamps to valid range`() = runTest {
        viewModel.setFontSize(5)
        assertEquals(10, viewModel.fontSize.first())
        
        viewModel.setFontSize(30)
        assertEquals(24, viewModel.fontSize.first())
        
        viewModel.setFontSize(16)
        assertEquals(16, viewModel.fontSize.first())
    }
    
    @Test
    fun `increaseFontSize increases by 2`() = runTest {
        viewModel.setFontSize(14)
        viewModel.increaseFontSize()
        
        assertEquals(16, viewModel.fontSize.first())
    }
    
    @Test
    fun `decreaseFontSize decreases by 2`() = runTest {
        viewModel.setFontSize(14)
        viewModel.decreaseFontSize()
        
        assertEquals(12, viewModel.fontSize.first())
    }
    
    @Test
    fun `setDarkMode updates theme`() = runTest {
        viewModel.setDarkMode(true)
        assertEquals("material", viewModel.theme.first())
        
        viewModel.setDarkMode(false)
        assertEquals("eclipse", viewModel.theme.first())
    }
    
    @Test
    fun `markAsSaved clears dirty flag`() = runTest {
        viewModel.loadFile("file1", "test.html", "<html></html>")
        viewModel.onContentChanged("<html><body></body></html>")
        advanceUntilIdle()
        
        assertTrue(viewModel.isDirty.first())
        
        viewModel.markAsSaved()
        
        assertFalse(viewModel.isDirty.first())
    }
    
    @Test
    fun `onValidationComplete updates validator`() = runTest {
        val json = """[{"from":{"line":1,"ch":5},"to":{"line":1,"ch":10},"message":"Error","severity":"error"}]"""
        viewModel.onValidationComplete(json)
        
        val errors = viewModel.syntaxValidator.validationErrors.first()
        assertEquals(1, errors.size)
    }
    
    @Test
    fun `startFormatting updates formatter state`() = runTest {
        viewModel.startFormatting()
        
        assertTrue(viewModel.codeFormatter.isFormatting.first())
    }
    
    @Test
    fun `onFormatComplete updates formatter`() = runTest {
        viewModel.startFormatting()
        viewModel.onFormatComplete(true)
        
        assertFalse(viewModel.codeFormatter.isFormatting.first())
        assertEquals(true, viewModel.codeFormatter.lastFormatSuccess.first())
    }
}
