package com.github.strindberg.emacssearchandcase.search

import com.github.strindberg.emacssearchandcase.actions.ISearchWordAction
import com.intellij.openapi.actionSystem.ActionPromoter
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR
import com.intellij.openapi.actionSystem.DataContext

class ISearchActionsPromoter : ActionPromoter {

    override fun promote(actions: MutableList<out AnAction>, context: DataContext): List<AnAction> {
        val newList = actions.toMutableList()
        if (EDITOR.getData(context)?.iSearchHint != null) {
            newList.sortWith { a, b -> if (a is ISearchWordAction) -1 else if (b is ISearchWordAction) 1 else 0 }
        }
        return newList
    }

}
