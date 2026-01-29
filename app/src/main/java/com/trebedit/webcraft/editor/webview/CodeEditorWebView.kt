package com.trebedit.webcraft.editor.webview

import android.annotation.SuppressLint
import android.content.Context
import android.util.Base64
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("SetJavaScriptEnabled")
class CodeEditorWebView(
    context: Context,
    private val scope: CoroutineScope,
    private val onContentChanged: (String) -> Unit,
    private val onCursorPositionChanged: (Int, Int) -> Unit,
    private val onUndoRedoStateChanged: (Boolean, Boolean) -> Unit,
    private val onValidationComplete: (String) -> Unit,
    private val onFormatComplete: (Boolean) -> Unit,
    private val onEditorReady: () -> Unit
) : WebView(context) {
    
    private var editorReady = false
    private val pendingCalls = mutableListOf<String>()
    
    init {
        setupWebView()
        loadEditor()
    }
    
    private fun setupWebView() {
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            allowFileAccess = true
            allowContentAccess = true
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
        }
        
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                processPendingCalls()
            }
        }
        
        val jsInterface = EditorJavascriptInterface(
            scope = scope,
            onContentChanged = onContentChanged,
            onCursorPositionChanged = onCursorPositionChanged,
            onUndoRedoStateChanged = onUndoRedoStateChanged,
            onValidationComplete = onValidationComplete,
            onFormatComplete = onFormatComplete,
            onEditorReady = {
                editorReady = true
                onEditorReady()
                processPendingCalls()
            }
        )
        
        addJavascriptInterface(jsInterface, "AndroidInterface")
    }
    
    private fun loadEditor() {
        try {
            val inputStream = context.assets.open("codemirror_editor.html")
            val htmlContent = inputStream.bufferedReader().use { it.readText() }
            
            val encodedHtml = Base64.encodeToString(htmlContent.toByteArray(), Base64.NO_PADDING)
            loadData(encodedHtml, "text/html", "base64")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun executeJavaScript(js: String) {
        if (editorReady) {
            scope.launch(Dispatchers.Main) {
                evaluateJavascript(js, null)
            }
        } else {
            pendingCalls.add(js)
        }
    }
    
    private fun processPendingCalls() {
        if (editorReady && pendingCalls.isNotEmpty()) {
            scope.launch(Dispatchers.Main) {
                pendingCalls.forEach { js ->
                    evaluateJavascript(js, null)
                }
                pendingCalls.clear()
            }
        }
    }
    
    fun setContent(content: String) {
        val escapedContent = content
            .replace("\\", "\\\\")
            .replace("'", "\\'")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
        executeJavaScript("setContent('$escapedContent');")
    }
    
    fun getContent(callback: (String) -> Unit) {
        if (editorReady) {
            scope.launch(Dispatchers.Main) {
                evaluateJavascript("getContent();") { result ->
                    val content = result?.trim('"')?.let { unescapeString(it) } ?: ""
                    callback(content)
                }
            }
        }
    }
    
    fun setLanguage(language: String) {
        executeJavaScript("setLanguage('$language');")
    }
    
    fun setTheme(theme: String) {
        executeJavaScript("setTheme('$theme');")
    }
    
    fun setFontSize(size: Int) {
        executeJavaScript("setFontSize($size);")
    }
    
    fun setCursorPosition(line: Int, ch: Int) {
        executeJavaScript("setCursorPosition($line, $ch);")
    }
    
    fun getCursorPosition(callback: (Int, Int) -> Unit) {
        if (editorReady) {
            scope.launch(Dispatchers.Main) {
                evaluateJavascript("getCursorPosition();") { result ->
                    try {
                        val json = result?.trim('"')?.let { unescapeString(it) } ?: "{}"
                        val match = Regex(""""line":(\d+),"ch":(\d+)""").find(json)
                        if (match != null) {
                            val line = match.groupValues[1].toInt()
                            val ch = match.groupValues[2].toInt()
                            callback(line, ch)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
    
    fun undo() {
        executeJavaScript("undo();")
    }
    
    fun redo() {
        executeJavaScript("redo();")
    }
    
    fun formatCode() {
        executeJavaScript("formatCode();")
    }
    
    fun canUndo(callback: (Boolean) -> Unit) {
        if (editorReady) {
            scope.launch(Dispatchers.Main) {
                evaluateJavascript("canUndo();") { result ->
                    callback(result?.toBoolean() ?: false)
                }
            }
        }
    }
    
    fun canRedo(callback: (Boolean) -> Unit) {
        if (editorReady) {
            scope.launch(Dispatchers.Main) {
                evaluateJavascript("canRedo();") { result ->
                    callback(result?.toBoolean() ?: false)
                }
            }
        }
    }
    
    private fun unescapeString(str: String): String {
        return str
            .replace("\\\\", "\\")
            .replace("\\'", "'")
            .replace("\\n", "\n")
            .replace("\\r", "\r")
            .replace("\\t", "\t")
    }
    
    fun cleanup() {
        loadUrl("about:blank")
        removeAllViews()
        destroy()
    }
}
