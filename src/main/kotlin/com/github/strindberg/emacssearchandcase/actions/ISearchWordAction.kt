package com.github.strindberg.emacssearchandcase.actions

import com.github.strindberg.emacssearchandcase.search.ISearchWordHandler
import com.intellij.openapi.editor.actionSystem.EditorAction

class ISearchWordAction : EditorAction(ISearchWordHandler())
