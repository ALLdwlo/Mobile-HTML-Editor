package com.trebedit.webcraft.editor.undo

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UndoRedoManagerTest {
    
    private lateinit var manager: UndoRedoManager
    
    @Before
    fun setup() {
        manager = UndoRedoManager()
    }
    
    @Test
    fun `initial state cannot undo or redo`() = runTest {
        assertFalse(manager.canUndo.first())
        assertFalse(manager.canRedo.first())
    }
    
    @Test
    fun `updateState updates undo and redo state`() = runTest {
        manager.updateState(canUndo = true, canRedo = false)
        
        assertTrue(manager.canUndo.first())
        assertFalse(manager.canRedo.first())
    }
    
    @Test
    fun `updateState can enable both undo and redo`() = runTest {
        manager.updateState(canUndo = true, canRedo = true)
        
        assertTrue(manager.canUndo.first())
        assertTrue(manager.canRedo.first())
    }
    
    @Test
    fun `reset clears all state`() = runTest {
        manager.updateState(canUndo = true, canRedo = true)
        manager.reset()
        
        assertFalse(manager.canUndo.first())
        assertFalse(manager.canRedo.first())
    }
    
    @Test
    fun `multiple updates preserve latest state`() = runTest {
        manager.updateState(canUndo = true, canRedo = false)
        manager.updateState(canUndo = false, canRedo = true)
        
        assertFalse(manager.canUndo.first())
        assertTrue(manager.canRedo.first())
    }
}
