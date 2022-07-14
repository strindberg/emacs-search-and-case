package com.github.strindberg.emacssearchandcase.actions

import com.github.strindberg.emacssearchandcase.word.MovementType
import com.github.strindberg.emacssearchandcase.word.WordMovementHandler
import com.intellij.openapi.editor.actions.TextComponentEditorAction

class PreviousWordAction : TextComponentEditorAction(WordMovementHandler(MovementType.PREVIOUS))
