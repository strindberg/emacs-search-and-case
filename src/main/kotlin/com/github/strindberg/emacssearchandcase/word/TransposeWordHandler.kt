package com.github.strindberg.emacssearchandcase.word

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.SelectionModel
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler

class TransposeWordHandler(private val forward: Boolean) : EditorWriteActionHandler.ForEachCaret() {

    @Suppress("unused")
    private val logger: Logger = Logger.getInstance("TransposeWordHandler")

    override fun executeWriteAction(editor: Editor, caret: Caret, dataContext: DataContext) {
        val document = editor.document
        val selectionModel = editor.selectionModel

        val (currentStart, currentEnd) = currentWordBoundaries(editor, selectionModel, forward) ?: return
        val currentWord = document.subString(currentStart, currentEnd)

        if (forward) {
            caret.moveToOffset(currentEnd)

            val (nextStart, nextEnd) = nextWordBoundaries(caret, document, editor) ?: return
            val nextWord = document.subString(nextStart, nextEnd)

            document.replaceWords(
                currentStart,
                currentEnd,
                nextStart,
                nextEnd,
                lowerCamelCase(selectionModel, currentEnd, nextStart, currentWord, nextWord)
            )

            caret.moveToOffset(nextEnd)

            if (selectionModel.hasSelection()) {
                selectionModel.setSelection(nextEnd - (currentEnd - currentStart), nextEnd)
            }
        } else {
            caret.moveToOffset(currentStart)

            val (prevStart, prevEnd) = previousWordBoundaries(caret, document, editor) ?: return
            val prevWord = document.subString(prevStart, prevEnd)

            document.replaceWords(
                prevStart,
                prevEnd,
                currentStart,
                currentEnd,
                lowerCamelCase(selectionModel, prevEnd, currentStart, prevWord, currentWord)
            )

            caret.moveToOffset(prevStart)

            if (selectionModel.hasSelection()) {
                selectionModel.setSelection(prevStart, prevStart + (currentEnd - currentStart))
            }
        }
    }

    private fun Document.subString(start: Int, end: Int) = charsSequence.subSequence(start, end).toString()

    private fun Document.replaceWords(firstStart: Int, firstEnd: Int, secondStart: Int, secondEnd: Int, words: Pair<String, String>) {
        replaceString(secondStart, secondEnd, words.first)
        replaceString(firstStart, firstEnd, words.second)
    }

    private fun lowerCamelCase(selectionModel: SelectionModel, firstEnd: Int, secondStart: Int, firstWord: String, secondWord: String) =
        if (!selectionModel.hasSelection() && firstEnd == secondStart && firstWord[0].isLowerCase())
            Pair(firstWord.replaceFirstChar { it.titlecaseChar() }, secondWord.replaceFirstChar { it.lowercase() })
        else Pair(firstWord, secondWord)

}
