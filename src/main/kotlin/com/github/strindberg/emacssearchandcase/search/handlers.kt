package com.github.strindberg.emacssearchandcase.search

import com.github.strindberg.emacssearchandcase.search.ISearchHandler.Companion.updatePositionAndHint
import com.intellij.codeInsight.template.impl.editorActions.TypedActionHandlerBase
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.editor.actionSystem.TypedActionHandler

internal class TypedHandler(originalHandler: TypedActionHandler) : TypedActionHandlerBase(originalHandler) {
    override fun execute(editor: Editor, charTyped: Char, dataContext: DataContext) {
        val hint = editor.iSearchHint
        if (hint == null) {
            myOriginalHandler?.execute(editor, charTyped, dataContext)
        } else {
            updatePositionAndHint(editor, hint, charTyped.toString())
        }
    }
}

internal abstract class BaseEditorActionHandler(protected val originalHandler: EditorActionHandler) : EditorActionHandler() {
    public override fun isEnabledForCaret(editor: Editor, caret: Caret, dataContext: DataContext?): Boolean {
        return editor.iSearchHint != null || originalHandler.isEnabled(editor, caret, dataContext)
    }
}

internal class HintHandler(originalHandler: EditorActionHandler, val block: ISearchHint.() -> Unit) :
    BaseEditorActionHandler(originalHandler) {
    public override fun doExecute(editor: Editor, caret: Caret?, dataContext: DataContext) {
        val hint = editor.iSearchHint
        if (hint == null) {
            originalHandler.execute(editor, caret, dataContext)
        } else {
            hint.block()
        }
    }
}

internal class HideHandler(originalHandler: EditorActionHandler) : BaseEditorActionHandler(originalHandler) {
    public override fun doExecute(editor: Editor, caret: Caret?, dataContext: DataContext) {
        editor.iSearchHint?.hide()
        originalHandler.execute(editor, caret, dataContext)
    }
}
