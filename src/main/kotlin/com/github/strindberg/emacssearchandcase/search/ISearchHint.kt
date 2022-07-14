package com.github.strindberg.emacssearchandcase.search

import com.intellij.codeInsight.hint.HintManagerImpl
import com.intellij.codeInsight.hint.HintUtil
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.ui.HintHint
import com.intellij.ui.JBColor
import com.intellij.ui.LightweightHint
import com.intellij.util.ui.UIUtil
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.SwingUtilities

internal class ISearchHint(private val editor: Editor, var forward: Boolean, val regexp: Boolean) : LightweightHint(ISearchPanel(editor.component)) {

    @Suppress("unused")
    private val logger: Logger = Logger.getInstance("ISearchHint")

    private val caretListener = object : CaretListener {
        override fun caretAdded(e: CaretEvent) {
            e.editor.iSearchHint?.hide()
        }

        override fun caretRemoved(e: CaretEvent) {
            e.caret?.searchData?.disposeHighlighter()
        }
    }

    private val documentListener = object : DocumentListener {
        override fun documentChanged(e: DocumentEvent) {
            editor.iSearchHint?.hide()
        }
    }

    private val history = mutableListOf<HintState>()

    private val titleLabel = newLabel(getLabelText(forward = forward, isWrapped = false, isFound = true, regexp = regexp))

    private val targetLabel = newLabel("")

    init {
        titleLabel.font = UIUtil.getLabelFont().deriveFont(Font.BOLD)

        val gbc = GridBagConstraints()
        val layout = component.layout as GridBagLayout

        gbc.gridx = 0
        layout.setConstraints(titleLabel, gbc)
        component.add(titleLabel)
        gbc.gridx = 1
        gbc.weightx = 1.0
        gbc.fill = GridBagConstraints.HORIZONTAL
        layout.setConstraints(targetLabel, gbc)
        component.add(targetLabel)

        editor.caretModel.addCaretListener(caretListener)
        editor.document.addDocumentListener(documentListener)
    }

    private fun newLabel(text: String): JLabel =
        JLabel(text).apply {
            background = HintUtil.getInformationColor()
            foreground = JBColor.foreground()
            isOpaque = true
        }

    internal var text: String
        get() = targetLabel.text
        set(newText) {
            targetLabel.text = newText
        }

    internal fun updateLabel(text: String, result: SearchResult) {
        updateLabel(text, result.color, getLabelText(result.forward, result.isWrapped, result.isFound, regexp))
    }

    private fun updateLabel(targetText: String, color: Color, titleText: String) {
        titleLabel.text = titleText
        targetLabel.text = targetText
        targetLabel.foreground = color
        this.pack()
    }

    private fun getLabelText(forward: Boolean, isWrapped: Boolean, isFound: Boolean, regexp: Boolean): String =
        sequenceOf(
            if (!isFound) "Failing" else null,
            if (isWrapped) "Wrapped" else null,
            if (regexp) "Regexp search" else "Search",
            if (!forward) "backward" else null
        ).filterNotNull().joinToString(" ") + ": "

    internal fun pushHistory() {
        history.add(HintState(targetLabel.text, targetLabel.foreground, titleLabel.text))
    }

    internal fun popHistory() {
        history.removeLastOrNull()?.let { hintState ->
            updateLabel(hintState.text, hintState.color, hintState.title)
        }
    }

    internal fun show() {
        if (!ApplicationManager.getApplication().isUnitTestMode) {
            val scroll = SwingUtilities.getAncestorOfClass(JScrollPane::class.java, editor.contentComponent)
            val height = component.preferredSize.getHeight().toInt()
            val bounds = scroll.bounds
            bounds.translate(0, scroll.height - height)
            bounds.height = height
            val pos = SwingUtilities.convertPoint(scroll.parent, bounds.location, editor.contentComponent.rootPane.layeredPane)

            HintManagerImpl.getInstanceImpl().showEditorHint(
                this,
                editor,
                pos,
                HintManagerImpl.HIDE_BY_TEXT_CHANGE or HintManagerImpl.HIDE_IF_OUT_OF_EDITOR,
                0,
                false,
                HintHint(editor, pos).setAwtTooltip(false)
            )
        }
    }

    override fun hide() {
        super.hide()

        editor.caretModel.allCarets.forEach {
            it.searchData.disposeHighlighter()
        }

        editor.iSearchHint?.let { hint ->
            editor.document.removeDocumentListener(documentListener)
            editor.caretModel.removeCaretListener(caretListener)
        }

        editor.iSearchHint = null
    }
}

private class ISearchPanel(val contentComponent: JComponent) : JPanel(GridBagLayout()) {
    override fun getPreferredSize(): Dimension {
        val size = super.getPreferredSize()
        return Dimension(contentComponent.width, size.height)
    }
}
