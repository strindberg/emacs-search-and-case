package com.github.strindberg.emacssearchandcase.search

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.util.Key
import com.intellij.ui.JBColor
import java.awt.Color

private val SEARCH_DATA_IN_EDITOR_KEY = Key.create<ISearchHint>("ISearchHandler.SEARCH_DATA_IN_EDITOR_KEY")
private val SEARCH_DATA_IN_CARET_KEY = Key.create<CaretSearchData>("ISearchHandler.SEARCH_DATA_IN_CARET_KEY")

internal var Editor.iSearchHint: ISearchHint?
    get() = getUserData(SEARCH_DATA_IN_EDITOR_KEY)
    set(iSearchHint) {
        putUserData(SEARCH_DATA_IN_EDITOR_KEY, iSearchHint)
    }

internal var Caret.searchData: CaretSearchData
    get() = getUserData(SEARCH_DATA_IN_CARET_KEY)!!
    set(searchData) {
        putUserData(SEARCH_DATA_IN_CARET_KEY, searchData)
    }

internal data class CaretSearchData(var start: Int) {
    val history = mutableListOf<CaretState>()
    var segmentHighlighter: RangeHighlighter? = null
    var offset: Int = start
    var matchLength: Int = 0

    fun disposeHighlighter() {
        segmentHighlighter?.dispose()
        segmentHighlighter = null
    }
}

internal data class SearchResult(val forward: Boolean, val isWrapped: Boolean, val isFound: Boolean) {
    val color: Color = if (!isFound) JBColor.RED else JBColor.foreground()
}

internal data class HintState(val text: String, val color: Color, val title: String)

internal data class CaretState(val offset: Int, val matchLength: Int)
