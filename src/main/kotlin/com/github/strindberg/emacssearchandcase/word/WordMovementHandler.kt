package com.github.strindberg.emacssearchandcase.word

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler

enum class MovementType { NEXT, PREVIOUS }

class WordMovementHandler(private val type: MovementType) : EditorActionHandler.ForEachCaret() {

    @Suppress("unused")
    private val logger: Logger = Logger.getInstance("WordMovementHandler")

    override fun doExecute(editor: Editor, caret: Caret?, dataContext: DataContext?) {
        val stop = when (type) {
            MovementType.NEXT -> nextWordEnd(editor.caretModel.currentCaret, editor.document, editor) ?: return
            MovementType.PREVIOUS -> previousWordStart(editor.caretModel.currentCaret, editor.document, editor) ?: return
        }

        editor.caretModel.moveToOffset(stop)
    }

}
