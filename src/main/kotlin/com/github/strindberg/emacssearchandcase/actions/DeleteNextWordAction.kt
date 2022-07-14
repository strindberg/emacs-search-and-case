package com.github.strindberg.emacssearchandcase.actions

import com.github.strindberg.emacssearchandcase.word.WordChangeHandler
import com.github.strindberg.emacssearchandcase.word.ChangeType
import com.intellij.openapi.editor.actions.TextComponentEditorAction

class DeleteNextWordAction : TextComponentEditorAction(WordChangeHandler(ChangeType.DELETE))
