package com.github.strindberg.emacssearchandcase.search

import com.intellij.find.FindManager
import com.intellij.find.FindModel
import com.intellij.find.FindResult
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_BACKSPACE
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_BACKWARD_PARAGRAPH
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_BACKWARD_PARAGRAPH_WITH_SELECTION
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_COPY
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_ENTER
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_ESCAPE
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_FORWARD_PARAGRAPH
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_FORWARD_PARAGRAPH_WITH_SELECTION
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_MOVE_CARET_DOWN
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_MOVE_CARET_DOWN_WITH_SELECTION
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_MOVE_CARET_LEFT
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_MOVE_CARET_LEFT_WITH_SELECTION
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_MOVE_CARET_PAGE_DOWN
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_MOVE_CARET_PAGE_DOWN_WITH_SELECTION
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_MOVE_CARET_PAGE_UP
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_MOVE_CARET_PAGE_UP_WITH_SELECTION
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_MOVE_CARET_RIGHT
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_MOVE_CARET_RIGHT_WITH_SELECTION
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_MOVE_CARET_UP
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_MOVE_CARET_UP_WITH_SELECTION
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_MOVE_LINE_END
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_MOVE_LINE_END_WITH_SELECTION
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_MOVE_LINE_START
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_MOVE_LINE_START_WITH_SELECTION
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_NEXT_PARAMETER
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_NEXT_WORD
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_NEXT_WORD_WITH_SELECTION
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_PASTE
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_PREVIOUS_WORD
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_PREVIOUS_WORD_WITH_SELECTION
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_PREV_PARAMETER
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_TEXT_END
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_TEXT_END_WITH_SELECTION
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_TEXT_START
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_TEXT_START_WITH_SELECTION
import com.intellij.openapi.application.ex.ClipboardUtil
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.editor.actionSystem.EditorActionManager
import com.intellij.openapi.editor.actionSystem.TypedAction
import com.intellij.openapi.editor.colors.EditorColors.SEARCH_RESULT_ATTRIBUTES
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory
import java.lang.Character.isUpperCase
import java.lang.Character.toLowerCase
import java.lang.Character.toUpperCase

class ISearchHandler(private val forward: Boolean, private val regexp: Boolean = false) : EditorActionHandler() {

    @Suppress("unused")
    private val logger: Logger = Logger.getInstance("ISearchHandler")

    private val movements = listOf(
        ACTION_EDITOR_BACKWARD_PARAGRAPH, ACTION_EDITOR_BACKWARD_PARAGRAPH_WITH_SELECTION, ACTION_EDITOR_FORWARD_PARAGRAPH,
        ACTION_EDITOR_FORWARD_PARAGRAPH_WITH_SELECTION, ACTION_EDITOR_MOVE_CARET_DOWN, ACTION_EDITOR_MOVE_CARET_DOWN_WITH_SELECTION,
        ACTION_EDITOR_MOVE_CARET_LEFT, ACTION_EDITOR_MOVE_CARET_LEFT_WITH_SELECTION, ACTION_EDITOR_MOVE_CARET_PAGE_DOWN,
        ACTION_EDITOR_MOVE_CARET_PAGE_DOWN_WITH_SELECTION, ACTION_EDITOR_MOVE_CARET_PAGE_UP,
        ACTION_EDITOR_MOVE_CARET_PAGE_UP_WITH_SELECTION, ACTION_EDITOR_MOVE_CARET_RIGHT, ACTION_EDITOR_MOVE_CARET_RIGHT_WITH_SELECTION,
        ACTION_EDITOR_MOVE_CARET_UP, ACTION_EDITOR_MOVE_CARET_UP_WITH_SELECTION, ACTION_EDITOR_MOVE_LINE_END,
        ACTION_EDITOR_MOVE_LINE_END_WITH_SELECTION, ACTION_EDITOR_MOVE_LINE_START, ACTION_EDITOR_MOVE_LINE_START_WITH_SELECTION,
        ACTION_EDITOR_NEXT_PARAMETER, ACTION_EDITOR_NEXT_WORD, ACTION_EDITOR_NEXT_WORD_WITH_SELECTION, ACTION_EDITOR_PREVIOUS_WORD,
        ACTION_EDITOR_PREVIOUS_WORD_WITH_SELECTION, ACTION_EDITOR_PREV_PARAMETER, ACTION_EDITOR_TEXT_END,
        ACTION_EDITOR_TEXT_END_WITH_SELECTION, ACTION_EDITOR_TEXT_START, ACTION_EDITOR_TEXT_START_WITH_SELECTION,
        "EditorCodeBlockStart", "EditorCodeBlockEnd", "EditorSwapSelectionBoundaries",
    )

    private val copyCommands = listOf(ACTION_EDITOR_COPY, "EditorKillRingSave")

    override fun doExecute(editor: Editor, caret: Caret?, dataContext: DataContext) {
        if (!registered) {
            TypedAction.getInstance().apply {
                setupRawHandler(TypedHandler(rawHandler))
            }

            EditorActionManager.getInstance().apply {
                setActionHandler(ACTION_EDITOR_BACKSPACE, HintHandler(getActionHandler(ACTION_EDITOR_BACKSPACE)) {
                    popHistory(editor, this)
                })

                setActionHandler(ACTION_EDITOR_ENTER, HintHandler(getActionHandler(ACTION_EDITOR_ENTER)) {
                    hide()
                })

                setActionHandler(ACTION_EDITOR_ESCAPE, HintHandler(getActionHandler(ACTION_EDITOR_ESCAPE)) {
                    editor.caretModel.runForEachCaret { it.moveToOffset(it.searchData.start) }
                    editor.scrollingModel.scrollToCaret(ScrollType.MAKE_VISIBLE)
                    hide()
                })

                setActionHandler(ACTION_EDITOR_PASTE, HintHandler(getActionHandler(ACTION_EDITOR_PASTE)) {
                    val text = ClipboardUtil.getTextInClipboard()
                    updatePositionAndHint(editor, this, text)
                })

                (movements + copyCommands).forEach {
                    setActionHandler(it, HideHandler(getActionHandler(it)))
                }
            }

            registered = true
        }

        editor.iSearchHint?.let { hint ->
            hint.forward = forward
            updatePositionAndHint(editor, hint)
            return
        }

        pushLastSearch()
        editor.caretModel.runForEachCaret { it.searchData = CaretSearchData(it.offset) }
        editor.iSearchHint = ISearchHint(editor, forward = forward, regexp = regexp).apply { show() }
    }

    companion object {

        @Suppress("unused")
        private val logger: Logger = Logger.getInstance("iSearch.companion")

        private var registered = false

        private var target: String = ""

        private val lastSearches = mutableListOf<String>()

        private fun pushLastSearch() {
            if (target.isNotEmpty() ) {
                lastSearches.add(target)
            }
        }

        internal fun updatePositionAndHint(editor: Editor, hint: ISearchHint, newText: String? = null) {
            pushHistory(editor, hint)

            hint.text += newText ?: ""

            target = hint.text.ifEmpty { lastSearches.removeLastOrNull() ?: "" }
            if (target.isNotEmpty()) {
                val isNext = newText == null && hint.text.isNotEmpty()
                val single = editor.caretModel.allCarets.size == 1
                val results =
                    editor.caretModel.allCarets.map { updatePosition(editor, target, it, hint.forward, isNext, single, hint.regexp) }

                if (searchStateChanged(editor, isNext)) {
                    hint.updateLabel(target, if (single) results[0] else SearchResult(hint.forward, false, results.any { it.isFound }))
                } else {
                    popHistory(editor, hint)
                }
            }
        }

        private fun pushHistory(editor: Editor, hint: ISearchHint) {
            hint.pushHistory()
            editor.caretModel.runForEachCaret { caret ->
                val caretData = caret.searchData
                caretData.history.add(CaretState(caretData.offset, caretData.matchLength))
            }
        }

        private fun popHistory(editor: Editor, hint: ISearchHint) {
            hint.popHistory()
            editor.caretModel.runForEachCaret { caret ->
                val caretData = caret.searchData
                caretData.history.removeLastOrNull()?.let { caretState ->
                    moveCaret(editor, caret, caretState.offset, caretState.matchLength)
                }
            }
        }

        private fun updatePosition(
            editor: Editor, target: String, caret: Caret, forward: Boolean, isNext: Boolean, single: Boolean, regexp: Boolean
        ): SearchResult {
            val (firstResult, finalResult) =
                if (forward) searchForward(editor, target, caret.searchData.offset, regexp, isNext).run {
                    Pair(this, found(isNext, single) ?: searchForward(editor, target, 0, regexp, isNext = false))
                }
                else searchBackward(editor, target, caret.offset, regexp, isNext).run {
                    Pair(this, found(isNext, single) ?: searchBackward(editor, target, editor.document.textLength, regexp, isNext = false))
                }

            if (finalResult.isStringFound) {
                moveCaret(editor, caret, finalResult.startOffset, finalResult.length)
            }
            return SearchResult(forward, finalResult != firstResult, finalResult.isStringFound)
        }

        private fun FindResult.found(isNext: Boolean, single: Boolean) = if (!isStringFound && isNext && single) null else this

        private fun searchForward(editor: Editor, target: String, matchOffset: Int, regexp: Boolean, isNext: Boolean): FindResult =
            search(editor, target, matchOffset + if (isNext) 1 else 0, regexp, forward = true)

        private fun searchBackward(editor: Editor, target: String, caretOffset: Int, regexp: Boolean, isNext: Boolean): FindResult =
            search(editor, target, caretOffset + 2 - if (isNext) 2 else 0, regexp, forward = false)

        private fun search(editor: Editor, target: String, start: Int, regexp: Boolean, forward: Boolean) =
            FindManager.getInstance(editor.project)
                .findString(editor.document.text, minOf(editor.document.text.length, start), FindModel().apply {
                    stringToFind = target
                    isForward = forward
                    isCaseSensitive = regexp || caseSensitive(target)
                    isRegularExpressions = regexp
                })

        private fun caseSensitive(text: String): Boolean = text.any { isUpperCase(it) && toUpperCase(it) != toLowerCase(it) }

        private fun searchStateChanged(editor: Editor, isNext: Boolean): Boolean =
            !isNext || editor.caretModel.allCarets.map { it.searchData }.any { caretData ->
                CaretState(caretData.offset, caretData.matchLength) != caretData.history.lastOrNull()
            }

        private fun moveCaret(editor: Editor, caret: Caret, offset: Int, matchLength: Int) {
            caret.moveToOffset(offset + matchLength)
            addHighlight(editor, caret.searchData, offset, matchLength)

            editor.scrollingModel.scrollToCaret(ScrollType.MAKE_VISIBLE)
            IdeDocumentHistory.getInstance(editor.project).includeCurrentCommandAsNavigation()
        }

        private fun addHighlight(editor: Editor, caretData: CaretSearchData, offset: Int, matchLength: Int) {
            caretData.matchLength = matchLength
            caretData.offset = offset
            caretData.disposeHighlighter()
            caretData.segmentHighlighter = editor.markupModel.addRangeHighlighter(
                SEARCH_RESULT_ATTRIBUTES,
                offset,
                offset + matchLength,
                HighlighterLayer.LAST + 1,
                HighlighterTargetArea.EXACT_RANGE
            )
        }
    }

}
