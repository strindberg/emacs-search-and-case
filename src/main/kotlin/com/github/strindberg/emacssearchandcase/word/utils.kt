package com.github.strindberg.emacssearchandcase.word

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.SelectionModel
import com.intellij.openapi.editor.actions.CaretStop
import com.intellij.openapi.editor.actions.CaretStopPolicy
import com.intellij.openapi.editor.actions.EditorActionUtil.getNextCaretStopOffset
import com.intellij.openapi.editor.actions.EditorActionUtil.getPreviousCaretStopOffset
import com.intellij.openapi.editor.actions.EditorActionUtil.isHumpBound

internal fun currentWordBoundaries(editor: Editor, selectionModel: SelectionModel, forward: Boolean): Pair<Int, Int>? {
    return if (selectionModel.hasSelection())
        Pair(selectionModel.selectionStart, selectionModel.selectionEnd)
    else {
        val currentCaret = editor.caretModel.currentCaret
        val text = editor.document.charsSequence
        val isCamelWords = editor.settings.isCamelWords

        if (forward) {
            currentCaret.moveToOffset(previousWordEnd(currentCaret.offset, text) ?: return null)

            val previousStop = getPreviousCaretStopOffset(editor, CaretStopPolicy(CaretStop.START, CaretStop.NONE), isCamelWords, true)
            val nextStop = getNextCaretStopOffset(editor, CaretStopPolicy(CaretStop.END, CaretStop.NONE), isCamelWords, true)

            if (isWordEnd(text, currentCaret.offset, isCamelWords))
                Pair(previousStop, currentCaret.offset)
            else
                Pair(previousStop, nextStop)
        } else {
            currentCaret.moveToOffset(nextWordStart(currentCaret.offset, text) ?: return null)

            val previousStop = getPreviousCaretStopOffset(editor, CaretStopPolicy(CaretStop.START, CaretStop.NONE), isCamelWords, true)
            val nextStop = getNextCaretStopOffset(editor, CaretStopPolicy(CaretStop.END, CaretStop.NONE), isCamelWords, true)

            if (isWordStart(text, currentCaret.offset, isCamelWords))
                Pair(currentCaret.offset, nextStop)
            else
                Pair(previousStop, nextStop)
        }
    }
}

internal fun nextWordEnd(caret: Caret, document: Document, editor: Editor): Int? =
    nextWordBoundaries(caret, document, editor)?.second

internal fun nextWordBoundaries(caret: Caret, document: Document, editor: Editor): Pair<Int, Int>? {
    val origin = caret.offset
    val wordStart = nextWordStart(caret.offset, document.charsSequence) ?: return null
    caret.moveToOffset(wordStart)
    val wordEnd =
        getNextCaretStopOffset(
            editor,
            CaretStopPolicy(CaretStop.END, CaretStop.NONE),
            editor.settings.isCamelWords,
            true
        )
    caret.moveToOffset(origin)
    return Pair(wordStart, wordEnd)
}

internal fun nextWordStart(offset: Int, text: CharSequence): Int? {
    var currentOffset = offset
    if (currentOffset >= text.length) return null
    while (!isWordChar(text[currentOffset])) {
        currentOffset += 1
        if (currentOffset >= text.length) return null
    }
    return currentOffset
}

internal fun previousWordStart(caret: Caret, document: Document, editor: Editor): Int? =
    previousWordBoundaries(caret, document, editor)?.first

internal fun previousWordBoundaries(caret: Caret, document: Document, editor: Editor): Pair<Int, Int>? {
    val origin = caret.offset
    val wordEnd = previousWordEnd(caret.offset, document.charsSequence) ?: return null
    caret.moveToOffset(wordEnd)
    val wordStart =
        getPreviousCaretStopOffset(
            editor,
            CaretStopPolicy(CaretStop.START, CaretStop.NONE),
            editor.settings.isCamelWords,
            true
        )
    caret.moveToOffset(origin)
    return Pair(wordStart, wordEnd)
}

internal fun previousWordEnd(offset: Int, text: CharSequence): Int? {
    var currentOffset = offset
    if (currentOffset <= 0) return null
    while (!isWordChar(text[currentOffset - 1])) {
        currentOffset -= 1
        if (currentOffset <= 0) return null
    }
    return currentOffset
}

internal fun isWordStart(text: CharSequence, offset: Int, isCamel: Boolean): Boolean {
    return isWordBoundary(text, offset, isCamel, true)
}

internal fun isWordEnd(text: CharSequence, offset: Int, isCamel: Boolean): Boolean {
    return isWordBoundary(text, offset, isCamel, false)
}

internal fun isWordBoundary(text: CharSequence, offset: Int, isCamel: Boolean, isStart: Boolean): Boolean {
    if (offset < 0 || offset > text.length) return false
    val prev: Char = if (offset > 0) text[offset - 1] else Char.MAX_VALUE
    val curr: Char = if (offset < text.length) text[offset] else Char.MAX_VALUE
    val word = if (isStart) curr else prev
    val neighbor = if (isStart) prev else curr
    if (isWordChar(word)) {
        if (!isWordChar(neighbor)) return true
        if (isCamel && isHumpBound(text, offset, isStart)) return true
    }
    return !isWordChar(word) && isWordChar(neighbor)
}

internal fun isWordChar(c: Char): Boolean = c.isLetterOrDigit()
