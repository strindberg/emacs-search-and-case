package com.github.strindberg.emacssearchandcase.word

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler
import java.util.Locale

enum class ChangeType { UPPER, LOWER, CAPITAL, DELETE, DELETE_PREVIOUS }

class WordChangeHandler(private val type: ChangeType) : EditorWriteActionHandler.ForEachCaret() {

    @Suppress("unused")
    private val logger: Logger = Logger.getInstance("WordChangeHandler")

    override fun executeWriteAction(editor: Editor, caret: Caret, dataContext: DataContext) {
        val document = editor.document

        val (start, end) =
            if (type == ChangeType.DELETE_PREVIOUS)
                Pair(previousWordStart(caret, document, editor) ?: return, caret.offset)
            else if (type == ChangeType.DELETE)
                Pair(caret.offset, nextWordEnd(caret, document, editor) ?: return)
            else
                if (editor.selectionModel.hasSelection())
                    Pair(editor.selectionModel.selectionStart, editor.selectionModel.selectionEnd)
                else
                    nextWordBoundaries(caret, document, editor) ?: return

        when (type) {
            ChangeType.UPPER -> replaceTextAndMove(document, start, end, caret) { uppercase() }
            ChangeType.LOWER -> replaceTextAndMove(document, start, end, caret) { lowercase() }
            ChangeType.CAPITAL -> replaceTextAndMove(document, start, end, caret) {
                lowercase().replaceFirstChar { it.titlecase(Locale.getDefault()) }
            }
            ChangeType.DELETE, ChangeType.DELETE_PREVIOUS -> document.deleteString(start, end)
        }

        editor.selectionModel.removeSelection()
    }

    private fun replaceTextAndMove(
        document: Document,
        start: Int,
        end: Int,
        caret: Caret,
        operation: String.() -> String,
    ) {
        document.replaceString(start, end, document.charsSequence.subSequence(start, end).toString().operation())
        caret.moveToOffset(end)
    }

}
