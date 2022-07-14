package com.github.strindberg.emacssearchandcase.actions

import com.github.strindberg.emacssearchandcase.word.TransposeWordHandler
import com.intellij.openapi.editor.actions.TextComponentEditorAction

class ReversTransposeWordsAction : TextComponentEditorAction(TransposeWordHandler(false))
