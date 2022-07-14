package com.github.strindberg.emacssearchandcase.search

import com.github.strindberg.emacssearchandcase.search.ISearchHandler.Companion.updatePositionAndHint
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.editor.actions.CaretStop
import com.intellij.openapi.editor.actions.CaretStopPolicy
import com.intellij.openapi.editor.actions.EditorActionUtil

class ISearchWordHandler : EditorActionHandler() {

    override fun doExecute(editor: Editor, caret: Caret?, dataContext: DataContext) {
        editor.iSearchHint?.let { hint ->
            val start = editor.caretModel.offset
            val end = EditorActionUtil.getNextCaretStopOffset(
                editor,
                CaretStopPolicy(CaretStop.END, CaretStop.END),
                editor.settings.isCamelWords,
                true
            )
            val text = editor.document.charsSequence.subSequence(start, end).toString()
            updatePositionAndHint(editor, hint, text)
        }
    }

}
