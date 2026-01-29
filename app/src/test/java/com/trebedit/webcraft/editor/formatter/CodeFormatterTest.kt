package com.trebedit.webcraft.editor.formatter

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CodeFormatterTest {
    
    private lateinit var formatter: CodeFormatter
    
    @Before
    fun setup() {
        formatter = CodeFormatter()
    }
    
    @Test
    fun `initial state is not formatting`() = runTest {
        assertFalse(formatter.isFormatting.first())
    }
    
    @Test
    fun `setFormattingState updates formatting state`() = runTest {
        formatter.setFormattingState(true)
        assertTrue(formatter.isFormatting.first())
        
        formatter.setFormattingState(false)
        assertFalse(formatter.isFormatting.first())
    }
    
    @Test
    fun `onFormatComplete sets success state and stops formatting`() = runTest {
        formatter.setFormattingState(true)
        formatter.onFormatComplete(true)
        
        assertFalse(formatter.isFormatting.first())
        assertEquals(true, formatter.lastFormatSuccess.first())
    }
    
    @Test
    fun `onFormatComplete can indicate failure`() = runTest {
        formatter.setFormattingState(true)
        formatter.onFormatComplete(false)
        
        assertFalse(formatter.isFormatting.first())
        assertEquals(false, formatter.lastFormatSuccess.first())
    }
    
    @Test
    fun `reset clears all state`() = runTest {
        formatter.setFormattingState(true)
        formatter.onFormatComplete(true)
        formatter.reset()
        
        assertFalse(formatter.isFormatting.first())
        assertNull(formatter.lastFormatSuccess.first())
    }
}
