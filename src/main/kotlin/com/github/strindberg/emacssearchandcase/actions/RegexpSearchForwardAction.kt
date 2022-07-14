package com.github.strindberg.emacssearchandcase.actions

import com.github.strindberg.emacssearchandcase.search.ISearchHandler
import com.intellij.openapi.editor.actionSystem.EditorAction

class RegexpSearchForwardAction : EditorAction(ISearchHandler(forward = true, regexp = true))
