package com.trebedit.webcraft.editor.syntax

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SyntaxHighlighterTest {
    
    private lateinit var syntaxHighlighter: SyntaxHighlighter
    
    @Before
    fun setup() {
        syntaxHighlighter = SyntaxHighlighter()
    }
    
    @Test
    fun `detectLanguage returns html for html files`() {
        assertEquals("html", syntaxHighlighter.detectLanguage("index.html"))
        assertEquals("html", syntaxHighlighter.detectLanguage("page.htm"))
    }
    
    @Test
    fun `detectLanguage returns css for css files`() {
        assertEquals("css", syntaxHighlighter.detectLanguage("styles.css"))
    }
    
    @Test
    fun `detectLanguage returns javascript for js files`() {
        assertEquals("javascript", syntaxHighlighter.detectLanguage("script.js"))
    }
    
    @Test
    fun `detectLanguage returns php for php files`() {
        assertEquals("php", syntaxHighlighter.detectLanguage("index.php"))
    }
    
    @Test
    fun `detectLanguage returns html for unknown files`() {
        assertEquals("html", syntaxHighlighter.detectLanguage("unknown.txt"))
    }
    
    @Test
    fun `getCodeMirrorMode returns correct mode for html`() {
        assertEquals("htmlmixed", syntaxHighlighter.getCodeMirrorMode("html"))
    }
    
    @Test
    fun `getCodeMirrorMode returns correct mode for css`() {
        assertEquals("css", syntaxHighlighter.getCodeMirrorMode("css"))
    }
    
    @Test
    fun `getCodeMirrorMode returns correct mode for javascript`() {
        assertEquals("javascript", syntaxHighlighter.getCodeMirrorMode("javascript"))
        assertEquals("javascript", syntaxHighlighter.getCodeMirrorMode("js"))
    }
    
    @Test
    fun `setLanguage updates current language`() {
        syntaxHighlighter.setLanguage("css")
        assertEquals("css", syntaxHighlighter.getCurrentLanguage())
        
        syntaxHighlighter.setLanguage("javascript")
        assertEquals("javascript", syntaxHighlighter.getCurrentLanguage())
    }
    
    @Test
    fun `getSupportedLanguages returns all supported languages`() {
        val languages = syntaxHighlighter.getSupportedLanguages()
        assertTrue(languages.contains("html"))
        assertTrue(languages.contains("css"))
        assertTrue(languages.contains("javascript"))
        assertTrue(languages.contains("php"))
    }
    
    @Test
    fun `getLanguageDisplayName returns formatted names`() {
        assertEquals("HTML", syntaxHighlighter.getLanguageDisplayName("html"))
        assertEquals("CSS", syntaxHighlighter.getLanguageDisplayName("css"))
        assertEquals("JavaScript", syntaxHighlighter.getLanguageDisplayName("javascript"))
        assertEquals("PHP", syntaxHighlighter.getLanguageDisplayName("php"))
    }
}
