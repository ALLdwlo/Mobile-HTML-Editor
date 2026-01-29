package com.trebedit.webcraft.editor.syntax

import java.io.File

class SyntaxHighlighter {
    
    private var currentLanguage: String = "html"
    
    fun detectLanguage(filename: String): String {
        val extension = File(filename).extension.lowercase()
        return when (extension) {
            "html", "htm" -> "html"
            "css" -> "css"
            "js" -> "javascript"
            "php" -> "php"
            else -> "html"
        }
    }
    
    fun setLanguage(language: String) {
        currentLanguage = when (language.lowercase()) {
            "html", "htm" -> "html"
            "css" -> "css"
            "javascript", "js" -> "javascript"
            "php" -> "php"
            else -> "html"
        }
    }
    
    fun getCurrentLanguage(): String = currentLanguage
    
    fun getCodeMirrorMode(language: String): String {
        return when (language.lowercase()) {
            "html", "htm" -> "htmlmixed"
            "css" -> "css"
            "javascript", "js" -> "javascript"
            "php" -> "php"
            else -> "htmlmixed"
        }
    }
    
    fun getSupportedLanguages(): List<String> {
        return listOf("html", "css", "javascript", "php")
    }
    
    fun getLanguageDisplayName(language: String): String {
        return when (language.lowercase()) {
            "html", "htm" -> "HTML"
            "css" -> "CSS"
            "javascript", "js" -> "JavaScript"
            "php" -> "PHP"
            else -> "HTML"
        }
    }
}
