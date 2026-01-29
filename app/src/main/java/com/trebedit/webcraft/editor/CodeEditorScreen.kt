package com.trebedit.webcraft.editor

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trebedit.webcraft.editor.webview.CodeEditorWebView
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeEditorScreen(
    fileId: String,
    filename: String,
    initialContent: String,
    onBackPressed: () -> Unit,
    viewModel: EditorViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isDarkTheme = isSystemInDarkTheme()
    
    val editorState by viewModel.editorState.collectAsState()
    val isDirty by viewModel.isDirty.collectAsState()
    val canUndo by viewModel.undoRedoManager.canUndo.collectAsState()
    val canRedo by viewModel.undoRedoManager.canRedo.collectAsState()
    val fontSize by viewModel.fontSize.collectAsState()
    val theme by viewModel.theme.collectAsState()
    val validationErrors by viewModel.syntaxValidator.validationErrors.collectAsState()
    val isFormatting by viewModel.codeFormatter.isFormatting.collectAsState()
    
    var webView by remember { mutableStateOf<CodeEditorWebView?>(null) }
    var showExitDialog by remember { mutableStateOf(false) }
    var showFormatDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(fileId, filename, initialContent) {
        viewModel.loadFile(fileId, filename, initialContent)
    }
    
    LaunchedEffect(isDarkTheme) {
        viewModel.setDarkMode(isDarkTheme)
    }
    
    LaunchedEffect(theme) {
        webView?.setTheme(theme)
    }
    
    LaunchedEffect(fontSize) {
        webView?.setFontSize(fontSize)
    }
    
    DisposableEffect(Unit) {
        onDispose {
            webView?.cleanup()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = filename,
                                style = MaterialTheme.typography.titleMedium
                            )
                            if (isDirty) {
                                Text(
                                    text = " •",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                        if (validationErrors.isNotEmpty()) {
                            Text(
                                text = "${viewModel.syntaxValidator.getErrorCount()} errors, ${viewModel.syntaxValidator.getWarningCount()} warnings",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (viewModel.syntaxValidator.hasErrors()) 
                                    MaterialTheme.colorScheme.error 
                                else 
                                    MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (isDirty) {
                            showExitDialog = true
                        } else {
                            onBackPressed()
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { webView?.undo() },
                        enabled = canUndo
                    ) {
                        Icon(Icons.Default.Undo, contentDescription = "Undo")
                    }
                    
                    IconButton(
                        onClick = { webView?.redo() },
                        enabled = canRedo
                    ) {
                        Icon(Icons.Default.Redo, contentDescription = "Redo")
                    }
                    
                    IconButton(onClick = { showFormatDialog = true }) {
                        Icon(Icons.Default.Code, contentDescription = "Format Code")
                    }
                    
                    IconButton(onClick = { viewModel.decreaseFontSize() }) {
                        Icon(Icons.Default.ZoomOut, contentDescription = "Decrease Font")
                    }
                    
                    IconButton(onClick = { viewModel.increaseFontSize() }) {
                        Icon(Icons.Default.ZoomIn, contentDescription = "Increase Font")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val state = editorState) {
                is EditorState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is EditorState.Loaded -> {
                    AndroidView(
                        factory = { ctx ->
                            CodeEditorWebView(
                                context = ctx,
                                scope = scope,
                                onContentChanged = { content ->
                                    viewModel.onContentChanged(content)
                                },
                                onCursorPositionChanged = { line, ch ->
                                    viewModel.onCursorPositionChanged(line, ch)
                                },
                                onUndoRedoStateChanged = { undo, redo ->
                                    viewModel.onUndoRedoStateChanged(undo, redo)
                                },
                                onValidationComplete = { errors ->
                                    viewModel.onValidationComplete(errors)
                                },
                                onFormatComplete = { success ->
                                    viewModel.onFormatComplete(success)
                                },
                                onEditorReady = {
                                    scope.launch {
                                        webView?.setContent(state.content)
                                        webView?.setLanguage(state.language)
                                        webView?.setTheme(theme)
                                        webView?.setFontSize(fontSize)
                                    }
                                }
                            ).also { webView = it }
                        },
                        modifier = Modifier.fillMaxSize(),
                        update = { view ->
                            view.layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }
                    )
                }
                is EditorState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            
            if (isFormatting) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                )
            }
        }
    }
    
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Unsaved Changes") },
            text = { Text("You have unsaved changes. Do you want to save before exiting?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.saveFile()
                    showExitDialog = false
                    onBackPressed()
                }) {
                    Text("Save & Exit")
                }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = {
                        showExitDialog = false
                        onBackPressed()
                    }) {
                        Text("Discard")
                    }
                    TextButton(onClick = { showExitDialog = false }) {
                        Text("Cancel")
                    }
                }
            }
        )
    }
    
    if (showFormatDialog) {
        AlertDialog(
            onDismissRequest = { showFormatDialog = false },
            title = { Text("Format Code") },
            text = { Text("This will format your code according to standard style guidelines. Continue?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.startFormatting()
                    webView?.formatCode()
                    showFormatDialog = false
                }) {
                    Text("Format")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFormatDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    LaunchedEffect(viewModel.codeFormatter.lastFormatSuccess.collectAsState().value) {
        viewModel.codeFormatter.lastFormatSuccess.value?.let { success ->
            // Could show a toast or snackbar here
        }
    }
}
