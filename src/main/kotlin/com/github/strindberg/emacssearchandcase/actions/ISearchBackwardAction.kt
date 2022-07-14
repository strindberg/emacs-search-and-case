package com.github.strindberg.emacssearchandcase.actions

import com.github.strindberg.emacssearchandcase.search.ISearchHandler
import com.intellij.openapi.editor.actionSystem.EditorAction

class ISearchBackwardAction : EditorAction(ISearchHandler(forward = false))
