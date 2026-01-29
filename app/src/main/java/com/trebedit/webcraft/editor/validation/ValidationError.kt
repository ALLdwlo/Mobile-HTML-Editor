package com.trebedit.webcraft.editor.validation

data class ValidationError(
    val line: Int,
    val ch: Int,
    val message: String,
    val severity: ErrorSeverity
)

enum class ErrorSeverity {
    ERROR,
    WARNING,
    INFO
}
