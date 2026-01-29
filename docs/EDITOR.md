# Code Editor Documentation

This document provides comprehensive information about the WebCraft code editor implementation, including CodeMirror integration, keyboard shortcuts, syntax validation, and performance optimization.

## Table of Contents

1. [Overview](#overview)
2. [CodeMirror Integration](#codemirror-integration)
3. [Architecture](#architecture)
4. [Features](#features)
5. [Keyboard Shortcuts](#keyboard-shortcuts)
6. [Syntax Validation](#syntax-validation)
7. [Performance Tuning](#performance-tuning)
8. [Adding Language Modes](#adding-language-modes)
9. [API Reference](#api-reference)

## Overview

The WebCraft code editor is built using CodeMirror 5.65.16, a versatile text editor implemented in JavaScript for the browser. It's integrated into the Android app via WebView and provides a rich code editing experience with syntax highlighting, real-time validation, and code formatting.

### Key Components

- **CodeEditorWebView**: Android WebView wrapper for CodeMirror
- **EditorJavascriptInterface**: JavaScript bridge for Kotlin ↔ JavaScript communication
- **EditorViewModel**: Manages editor state and business logic
- **SyntaxHighlighter**: Language detection and mode switching
- **SyntaxValidator**: Real-time syntax validation
- **CodeFormatter**: Code formatting using Prettier.js
- **UndoRedoManager**: Undo/redo state management

## CodeMirror Integration

### Setup

The CodeMirror library is loaded from CDN and bundled as an HTML asset in `app/src/main/assets/codemirror_editor.html`.

#### CDN Resources

- **CodeMirror Core**: v5.65.16
- **Themes**: Material (dark), Eclipse (light)
- **Language Modes**: HTML, CSS, JavaScript, PHP
- **Addons**: 
  - Bracket matching/closing
  - Tag matching/closing
  - Code folding
  - Linting
  - Active line highlighting

#### Libraries

- **HTMLHint**: v1.1.4 - HTML validation
- **CSSLint**: v1.0.5 - CSS validation
- **JSHint**: v2.13.6 - JavaScript validation
- **Prettier**: v2.8.8 - Code formatting

### Configuration

The editor is initialized with the following default configuration:

```javascript
{
  mode: 'htmlmixed',
  theme: 'default',
  lineNumbers: true,
  lineWrapping: true,
  indentUnit: 2,
  indentWithTabs: false,
  matchBrackets: true,
  autoCloseBrackets: true,
  autoCloseTags: true,
  matchTags: { bothTags: true },
  styleActiveLine: true,
  foldGutter: true,
  gutters: [
    'CodeMirror-linenumbers',
    'CodeMirror-foldgutter',
    'CodeMirror-lint-markers'
  ],
  lint: true
}
```

## Architecture

### Communication Flow

```
Kotlin (EditorViewModel)
    ↓ ↑
CodeEditorWebView
    ↓ ↑
JavaScript Bridge (EditorJavascriptInterface)
    ↓ ↑
CodeMirror (JavaScript)
```

### State Management

The editor uses Kotlin StateFlow for reactive state management:

- **editorState**: Current editor state (Loading, Loaded, Error)
- **currentContent**: Current document content
- **isDirty**: Whether there are unsaved changes
- **cursorPosition**: Current cursor line and column
- **fontSize**: Current font size (10-24sp)
- **theme**: Current theme (default, material, eclipse)
- **validationErrors**: List of syntax validation errors

### Data Flow

1. User types in editor → JavaScript `onChange` event
2. Content debounced (300ms) → `onContentChanged` callback
3. JavaScript bridge → Kotlin `EditorJavascriptInterface`
4. Coroutine dispatched to Main thread → ViewModel update
5. StateFlow emits new value → Compose UI recomposes

## Features

### 1. Multi-Language Syntax Highlighting

Supported languages:
- **HTML** (htmlmixed mode)
- **CSS** (css mode)
- **JavaScript** (javascript mode)
- **PHP** (php mode)

Language is auto-detected based on file extension:
```kotlin
val language = syntaxHighlighter.detectLanguage("index.html") // Returns "html"
```

### 2. Auto-Indent

- Automatically indents on Enter key
- Preserves indentation from previous line
- Configurable indent size (default: 2 spaces)
- Never uses tabs (always spaces)

### 3. Bracket Matching

- Highlights matching brackets: `()`, `{}`, `[]`
- Highlights matching HTML tags: `<div>` ... `</div>`
- Shows matching pair when cursor is adjacent to bracket

### 4. Line Numbers

- Gutter with line numbers on the left
- Current line number is highlighted
- Clickable to set cursor position

### 5. Code Folding

- Collapsible code blocks for:
  - HTML tags
  - CSS rules
  - JavaScript functions
- Click gutter icon to fold/unfold
- Fold state is preserved during editing

### 6. Auto-Closing

- Auto-closes HTML tags when typing `</`
- Auto-closes brackets: `(`, `{`, `[`
- Auto-closes quotes: `"`, `'`
- Can be toggled in editor configuration

### 7. Real-Time Syntax Validation

Validates code as you type with debouncing (500ms):

#### HTML Validation
- Unclosed tags
- Invalid tag nesting
- Deprecated HTML tags
- Missing required attributes

#### CSS Validation
- Invalid properties
- Unknown selectors
- Syntax errors

#### JavaScript Validation
- Syntax errors
- Undefined variables (basic scope analysis)
- Missing semicolons (configurable)

### 8. Code Formatting

Uses Prettier.js for consistent code formatting:

#### HTML Formatting
- Indentation: 2 spaces
- Tag spacing
- Attribute formatting
- HTML whitespace sensitivity: ignore

#### CSS Formatting
- Property spacing
- Rule formatting
- Semicolon placement

#### JavaScript Formatting
- Semicolons: always
- Quotes: single
- Print width: 80 characters
- Tab width: 2 spaces

### 9. Undo/Redo

- CodeMirror's native undo/redo stack
- Maximum 100 operations (default)
- Keyboard shortcuts: Ctrl+Z (undo), Ctrl+Shift+Z (redo)
- UI buttons with enabled/disabled states

### 10. Pinch-to-Zoom

- Pinch in/out to change font size
- Font size range: 10sp - 24sp
- Persisted in DataStore (future enhancement)
- Updates CodeMirror font size dynamically

## Keyboard Shortcuts

### Desktop Shortcuts (when using physical keyboard)

| Shortcut | Action |
|----------|--------|
| `Ctrl+Z` or `Cmd+Z` | Undo |
| `Ctrl+Y` or `Cmd+Shift+Z` | Redo |
| `Tab` | Indent selection |
| `Shift+Tab` | Unindent selection |
| `Ctrl+/` or `Cmd+/` | Toggle comment |
| `Ctrl+D` or `Cmd+D` | Delete line |
| `Ctrl+F` or `Cmd+F` | Find (future) |
| `Ctrl+H` or `Cmd+Option+F` | Replace (future) |

### Mobile Gestures

| Gesture | Action |
|---------|--------|
| Pinch in/out | Increase/decrease font size |
| Long press | Show context menu (copy, paste, select) |
| Double tap | Select word |
| Triple tap | Select line |

## Syntax Validation

### Validation Rules

#### HTML
```javascript
{
  'tagname-lowercase': true,
  'attr-lowercase': true,
  'attr-value-double-quotes': true,
  'tag-pair': true,
  'spec-char-escape': true,
  'id-unique': true,
  'src-not-empty': true,
  'attr-no-duplication': true
}
```

#### CSS
- Validates against CSS specification
- Checks for unknown properties
- Validates selector syntax

#### JavaScript
```javascript
{
  esversion: 6,
  asi: true,          // Allow automatic semicolon insertion
  boss: true,         // Allow assignments in conditions
  loopfunc: true,     // Allow functions in loops
  expr: true          // Allow expressions as statements
}
```

### Error Severity Levels

1. **ERROR** (red): Critical syntax errors that prevent code execution
2. **WARNING** (orange): Non-critical issues or deprecated features
3. **INFO** (blue): Suggestions and best practices

### Custom Validation

To add custom validation rules, modify the `customLintFunction` in `codemirror_editor.html`:

```javascript
function customLintFunction(text, callback, options, cm) {
  const mode = cm.getOption('mode');
  const found = [];
  
  // Add your custom validation logic here
  
  callback(found);
}
```

## Performance Tuning

### Optimization Strategies

#### 1. Lazy Loading
- CodeMirror is loaded only when editor screen is opened
- Not initialized on app startup
- Reduces initial app load time

#### 2. Debouncing
- Content changes debounced to 300ms
- Validation debounced to 500ms
- Prevents excessive updates and validation runs

#### 3. WebView Caching
```kotlin
settings.cacheMode = WebSettings.LOAD_DEFAULT
```
- Caches CodeMirror assets from CDN
- Reduces network requests on subsequent loads

#### 4. Incremental Updates
- CodeMirror re-highlights only changed regions
- Validation runs only on modified lines
- Reduces CPU usage during editing

#### 5. Memory Management
- WebView destroyed when leaving editor screen
- Editor state cleared on cleanup
- Prevents memory leaks

```kotlin
DisposableEffect(Unit) {
    onDispose {
        webView?.cleanup()
    }
}
```

### Performance Metrics

- **Initial Load**: ~500-800ms (CDN dependent)
- **Content Update**: ~10-50ms (debounced)
- **Syntax Highlighting**: Real-time (incremental)
- **Validation**: ~100-300ms (debounced)
- **Formatting**: ~200-500ms (depends on file size)

### Large File Handling

For files larger than 1MB:
1. Show warning dialog before opening
2. Disable real-time linting
3. Disable code folding
4. Increase debounce times

```kotlin
if (fileSize > 1_000_000) {
    // Disable features for large files
    webView?.executeJavaScript("editor.setOption('lint', false);")
}
```

## Adding Language Modes

To add support for a new language:

### 1. Add CodeMirror Mode

Update `codemirror_editor.html`:
```html
<script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.65.16/mode/python/python.min.js"></script>
```

### 2. Update SyntaxHighlighter

```kotlin
fun detectLanguage(filename: String): String {
    val extension = File(filename).extension.lowercase()
    return when (extension) {
        "html", "htm" -> "html"
        "css" -> "css"
        "js" -> "javascript"
        "php" -> "php"
        "py" -> "python"  // Add new language
        else -> "html"
    }
}
```

### 3. Add Mode Mapping

```kotlin
fun getCodeMirrorMode(language: String): String {
    return when (language.lowercase()) {
        "html", "htm" -> "htmlmixed"
        "css" -> "css"
        "javascript", "js" -> "javascript"
        "php" -> "php"
        "python", "py" -> "python"  // Add new mapping
        else -> "htmlmixed"
    }
}
```

### 4. Add Validation (Optional)

If language-specific validation is needed:

```javascript
if (mode === 'python') {
    // Add Python validation logic
    // Use a library like pyflakes or pylint
}
```

### 5. Add Formatting (Optional)

Update `formatCode()` function:
```javascript
if (mode === 'python') {
    formatted = prettier.format(content, {
        parser: 'python',
        plugins: prettierPlugins
    });
}
```

## API Reference

### CodeEditorWebView

```kotlin
class CodeEditorWebView(
    context: Context,
    scope: CoroutineScope,
    onContentChanged: (String) -> Unit,
    onCursorPositionChanged: (Int, Int) -> Unit,
    onUndoRedoStateChanged: (Boolean, Boolean) -> Unit,
    onValidationComplete: (String) -> Unit,
    onFormatComplete: (Boolean) -> Unit,
    onEditorReady: () -> Unit
)
```

#### Methods

```kotlin
fun setContent(content: String)
fun getContent(callback: (String) -> Unit)
fun setLanguage(language: String)
fun setTheme(theme: String)
fun setFontSize(size: Int)
fun setCursorPosition(line: Int, ch: Int)
fun getCursorPosition(callback: (Int, Int) -> Unit)
fun undo()
fun redo()
fun formatCode()
fun canUndo(callback: (Boolean) -> Unit)
fun canRedo(callback: (Boolean) -> Unit)
fun cleanup()
```

### EditorViewModel

```kotlin
class EditorViewModel : ViewModel
```

#### Properties

```kotlin
val editorState: StateFlow<EditorState>
val currentContent: StateFlow<String>
val isDirty: StateFlow<Boolean>
val cursorPosition: StateFlow<Pair<Int, Int>>
val fontSize: StateFlow<Int>
val theme: StateFlow<String>
val syntaxHighlighter: SyntaxHighlighter
val undoRedoManager: UndoRedoManager
val syntaxValidator: SyntaxValidator
val codeFormatter: CodeFormatter
```

#### Methods

```kotlin
fun loadFile(fileId: String, filename: String, content: String)
fun onContentChanged(content: String)
fun onCursorPositionChanged(line: Int, ch: Int)
fun onUndoRedoStateChanged(canUndo: Boolean, canRedo: Boolean)
fun onValidationComplete(errorsJson: String)
fun onFormatComplete(success: Boolean)
fun setFontSize(size: Int)
fun increaseFontSize()
fun decreaseFontSize()
fun setTheme(theme: String)
fun setDarkMode(isDark: Boolean)
fun startFormatting()
fun saveFile()
fun markAsSaved()
```

### SyntaxHighlighter

```kotlin
fun detectLanguage(filename: String): String
fun setLanguage(language: String)
fun getCurrentLanguage(): String
fun getCodeMirrorMode(language: String): String
fun getSupportedLanguages(): List<String>
fun getLanguageDisplayName(language: String): String
```

### SyntaxValidator

```kotlin
val validationErrors: StateFlow<List<ValidationError>>

fun parseValidationResults(errorsJson: String)
fun clearErrors()
fun getErrorCount(): Int
fun getWarningCount(): Int
fun hasErrors(): Boolean
```

### CodeFormatter

```kotlin
val isFormatting: StateFlow<Boolean>
val lastFormatSuccess: StateFlow<Boolean?>

fun setFormattingState(isFormatting: Boolean)
fun onFormatComplete(success: Boolean)
fun reset()
```

### UndoRedoManager

```kotlin
val canUndo: StateFlow<Boolean>
val canRedo: StateFlow<Boolean>

fun updateState(canUndo: Boolean, canRedo: Boolean)
fun reset()
```

## Best Practices

### 1. Content Updates

Always debounce content updates to avoid excessive JavaScript calls:
```kotlin
private var contentChangeTimeout: Job? = null

fun updateContent(content: String) {
    contentChangeTimeout?.cancel()
    contentChangeTimeout = viewModelScope.launch {
        delay(300)
        webView?.setContent(content)
    }
}
```

### 2. Memory Management

Always cleanup WebView when leaving editor:
```kotlin
DisposableEffect(Unit) {
    onDispose {
        webView?.cleanup()
    }
}
```

### 3. Error Handling

Handle JavaScript errors gracefully:
```kotlin
try {
    webView?.setContent(content)
} catch (e: Exception) {
    Log.e("Editor", "Failed to set content", e)
    // Show error to user
}
```

### 4. Dark Mode

Sync theme with system dark mode:
```kotlin
LaunchedEffect(isDarkTheme) {
    viewModel.setDarkMode(isDarkTheme)
}
```

### 5. Saving State

Auto-save on content changes with debouncing:
```kotlin
private fun scheduleAutoSave() {
    autoSaveJob?.cancel()
    autoSaveJob = viewModelScope.launch {
        delay(2000)
        if (isDirty.value) {
            saveFile()
        }
    }
}
```

## Troubleshooting

### Issue: Editor not loading

**Solution**: Check WebView JavaScript is enabled and asset file exists:
```kotlin
settings.javaScriptEnabled = true
```

### Issue: Content not updating

**Solution**: Ensure editor is ready before setting content:
```kotlin
onEditorReady = {
    webView?.setContent(content)
}
```

### Issue: Validation not working

**Solution**: Check linting libraries are loaded in HTML:
```html
<script src="https://cdnjs.cloudflare.com/ajax/libs/htmlhint/1.1.4/htmlhint.min.js"></script>
```

### Issue: Poor performance with large files

**Solution**: Disable features for large files:
```kotlin
if (fileSize > 1_000_000) {
    webView?.executeJavaScript("editor.setOption('lint', false);")
    webView?.executeJavaScript("editor.setOption('foldGutter', false);")
}
```

## Future Enhancements

1. **Autocomplete**: Add code completion suggestions
2. **Find/Replace**: Implement search and replace functionality
3. **Multiple Cursors**: Support for multiple simultaneous cursors
4. **Minimap**: Add code overview minimap
5. **Split View**: Support for split-screen editing
6. **Vim/Emacs Keybindings**: Add alternative keybinding modes
7. **Custom Themes**: Allow users to create custom themes
8. **Offline Mode**: Bundle CodeMirror assets locally

## Resources

- [CodeMirror Documentation](https://codemirror.net/5/doc/manual.html)
- [Prettier Documentation](https://prettier.io/docs/en/)
- [HTMLHint Rules](https://htmlhint.com/docs/user-guide/list-rules)
- [CSSLint Rules](https://github.com/CSSLint/csslint/wiki/Rules)
- [JSHint Options](https://jshint.com/docs/options/)

---

**Last Updated**: January 29, 2026  
**Version**: 1.0.0
