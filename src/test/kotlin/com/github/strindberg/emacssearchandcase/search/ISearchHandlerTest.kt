package com.github.strindberg.emacssearchandcase.search

import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_BACKSPACE
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_ENTER
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_ESCAPE
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ISearchHandlerTest : BasePlatformTestCase() {

    @Test
    fun `iSearch 00`() {
        myFixture.configureByText("a.txt", "<caret>foo")
        myFixture.performEditorAction("ISearchForward")
        myFixture.type("o")
        myFixture.checkResult("fo<caret>o")
    }

    @Test
    fun `iSearch 01`() {
        myFixture.configureByText("a.txt", "<caret>foo")
        myFixture.performEditorAction("ISearchForward")
        myFixture.type("oo")
        myFixture.checkResult("foo<caret>")
    }

    @Test
    fun `iSearch 02`() {
        myFixture.configureByText("a.txt", "<caret>foo bar foo")
        myFixture.performEditorAction("ISearchForward")
        myFixture.type("foo")
        myFixture.performEditorAction("ISearchForward")
        myFixture.checkResult("foo bar foo<caret>")
    }

    @Test
    fun `iSearch 03`() {
        myFixture.configureByText("a.txt", "foo <caret>bar foo")
        myFixture.performEditorAction("ISearchForward")
        myFixture.type("foo")
        myFixture.performEditorAction("ISearchForward")
        myFixture.checkResult("foo<caret> bar foo")
    }

    @Test
    fun `iSearch 04`() {
        myFixture.configureByText("a.txt", "<caret>foo")
        myFixture.performEditorAction("ISearchForward")
        myFixture.type("o")
        myFixture.performEditorAction(ACTION_EDITOR_ENTER)
        myFixture.checkResult("fo<caret>o")
        myFixture.performEditorAction("ISearchForward")
        myFixture.performEditorAction("ISearchForward")
        myFixture.checkResult("foo<caret>")
    }

    @Test
    fun `iSearch 05`() {
        myFixture.configureByText("a.txt", "<caret>foo bar foo")
        myFixture.performEditorAction("ISearchForward")
        myFixture.performEditorAction("ISearchWord")
        myFixture.checkResult("foo<caret> bar foo")
        myFixture.performEditorAction("ISearchForward")
        myFixture.checkResult("foo bar foo<caret>")
        myFixture.performEditorAction(ACTION_EDITOR_BACKSPACE)
        myFixture.checkResult("foo<caret> bar foo")
    }

}
