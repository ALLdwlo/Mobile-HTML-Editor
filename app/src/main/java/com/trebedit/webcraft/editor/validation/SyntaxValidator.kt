package com.trebedit.webcraft.editor.validation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class SyntaxValidator {
    
    private val _validationErrors = MutableStateFlow<List<ValidationError>>(emptyList())
    val validationErrors: StateFlow<List<ValidationError>> = _validationErrors.asStateFlow()
    
    private val json = Json { 
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    fun parseValidationResults(errorsJson: String) {
        try {
            val errors = parseJsonArray(errorsJson)
            _validationErrors.value = errors
        } catch (e: Exception) {
            e.printStackTrace()
            _validationErrors.value = emptyList()
        }
    }
    
    private fun parseJsonArray(jsonString: String): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()
        
        try {
            if (jsonString.isBlank() || jsonString == "[]") {
                return emptyList()
            }
            
            val cleanJson = jsonString.trim()
            if (!cleanJson.startsWith("[")) {
                return emptyList()
            }
            
            val regex = Regex(""""from":\{"line":(\d+),"ch":(\d+)\}.*?"message":"([^"]+)".*?"severity":"([^"]+)"""")
            val matches = regex.findAll(cleanJson)
            
            for (match in matches) {
                val line = match.groupValues[1].toIntOrNull() ?: 0
                val ch = match.groupValues[2].toIntOrNull() ?: 0
                val message = match.groupValues[3]
                val severityStr = match.groupValues[4]
                
                val severity = when (severityStr.lowercase()) {
                    "error" -> ErrorSeverity.ERROR
                    "warning" -> ErrorSeverity.WARNING
                    else -> ErrorSeverity.INFO
                }
                
                errors.add(ValidationError(line, ch, message, severity))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return errors
    }
    
    fun clearErrors() {
        _validationErrors.value = emptyList()
    }
    
    fun getErrorCount(): Int {
        return _validationErrors.value.count { it.severity == ErrorSeverity.ERROR }
    }
    
    fun getWarningCount(): Int {
        return _validationErrors.value.count { it.severity == ErrorSeverity.WARNING }
    }
    
    fun hasErrors(): Boolean {
        return getErrorCount() > 0
    }
}
