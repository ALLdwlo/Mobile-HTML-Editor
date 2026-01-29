package com.trebedit.webcraft.editor.validation

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SyntaxValidatorTest {
    
    private lateinit var validator: SyntaxValidator
    
    @Before
    fun setup() {
        validator = SyntaxValidator()
    }
    
    @Test
    fun `parseValidationResults with empty json returns empty list`() = runTest {
        validator.parseValidationResults("[]")
        val errors = validator.validationErrors.first()
        assertTrue(errors.isEmpty())
    }
    
    @Test
    fun `parseValidationResults parses valid error json`() = runTest {
        val json = """[{"from":{"line":1,"ch":5},"to":{"line":1,"ch":10},"message":"Test error","severity":"error"}]"""
        validator.parseValidationResults(json)
        val errors = validator.validationErrors.first()
        
        assertEquals(1, errors.size)
        assertEquals(1, errors[0].line)
        assertEquals(5, errors[0].ch)
        assertEquals("Test error", errors[0].message)
        assertEquals(ErrorSeverity.ERROR, errors[0].severity)
    }
    
    @Test
    fun `getErrorCount returns correct count`() = runTest {
        val json = """[
            {"from":{"line":1,"ch":5},"to":{"line":1,"ch":10},"message":"Error 1","severity":"error"},
            {"from":{"line":2,"ch":5},"to":{"line":2,"ch":10},"message":"Warning 1","severity":"warning"},
            {"from":{"line":3,"ch":5},"to":{"line":3,"ch":10},"message":"Error 2","severity":"error"}
        ]"""
        validator.parseValidationResults(json)
        
        assertEquals(2, validator.getErrorCount())
    }
    
    @Test
    fun `getWarningCount returns correct count`() = runTest {
        val json = """[
            {"from":{"line":1,"ch":5},"to":{"line":1,"ch":10},"message":"Error 1","severity":"error"},
            {"from":{"line":2,"ch":5},"to":{"line":2,"ch":10},"message":"Warning 1","severity":"warning"},
            {"from":{"line":3,"ch":5},"to":{"line":3,"ch":10},"message":"Warning 2","severity":"warning"}
        ]"""
        validator.parseValidationResults(json)
        
        assertEquals(2, validator.getWarningCount())
    }
    
    @Test
    fun `hasErrors returns true when errors exist`() = runTest {
        val json = """[{"from":{"line":1,"ch":5},"to":{"line":1,"ch":10},"message":"Error","severity":"error"}]"""
        validator.parseValidationResults(json)
        
        assertTrue(validator.hasErrors())
    }
    
    @Test
    fun `hasErrors returns false when only warnings exist`() = runTest {
        val json = """[{"from":{"line":1,"ch":5},"to":{"line":1,"ch":10},"message":"Warning","severity":"warning"}]"""
        validator.parseValidationResults(json)
        
        assertFalse(validator.hasErrors())
    }
    
    @Test
    fun `clearErrors clears all errors`() = runTest {
        val json = """[{"from":{"line":1,"ch":5},"to":{"line":1,"ch":10},"message":"Error","severity":"error"}]"""
        validator.parseValidationResults(json)
        
        validator.clearErrors()
        
        val errors = validator.validationErrors.first()
        assertTrue(errors.isEmpty())
    }
}
