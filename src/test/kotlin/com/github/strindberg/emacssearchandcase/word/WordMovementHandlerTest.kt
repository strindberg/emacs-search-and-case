package com.github.strindberg.emacssearchandcase.word

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WordMovementHandlerTest : BasePlatformTestCase() {

    @Test
    fun `next word 00`() {
        myFixture.configureByText("file.txt", "foo<caret>")
        myFixture.performEditorAction("NextWord")
        myFixture.checkResult("foo<caret>")
    }

    @Test
    fun `next word 01`() {
        myFixture.configureByText("file.txt", "<caret>foo")
        myFixture.performEditorAction("NextWord")
        myFixture.checkResult("foo<caret>")
    }

    @Test
    fun `next word 02`() {
        myFixture.configureByText("file.txt", "<caret> foo")
        myFixture.performEditorAction("NextWord")
        myFixture.checkResult(" foo<caret>")
    }

    @Test
    fun `next word 03`() {
        myFixture.configureByText("file.txt", "<caret>+ (foo")
        myFixture.performEditorAction("NextWord")
        myFixture.checkResult("+ (foo<caret>")
    }

    @Test
    fun `next word 04`() {
        myFixture.configureByText("file.txt", "<caret>fooBar")
        myFixture.editor.settings.isCamelWords = true
        myFixture.performEditorAction("NextWord")
        myFixture.checkResult("foo<caret>Bar")
    }

    @Test
    fun `previous word 00`() {
        myFixture.configureByText("file.txt", "<caret>foo")
        myFixture.performEditorAction("PreviousWord")
        myFixture.checkResult("<caret>foo")
    }

    @Test
    fun `previous word 01`() {
        myFixture.configureByText("file.txt", "foo<caret>")
        myFixture.performEditorAction("PreviousWord")
        myFixture.checkResult("<caret>foo")
    }

    @Test
    fun `previous word 02`() {
        myFixture.configureByText("file.txt", "foo <caret>")
        myFixture.performEditorAction("PreviousWord")
        myFixture.checkResult("<caret>foo ")
    }

    @Test
    fun `previous word 03`() {
        myFixture.configureByText("file.txt", "foo) ()<caret>")
        myFixture.performEditorAction("PreviousWord")
        myFixture.checkResult("<caret>foo) ()")
    }

    @Test
    fun `previous word 04`() {
        myFixture.configureByText("file.txt", "fooBar<caret>")
        myFixture.editor.settings.isCamelWords = true
        myFixture.performEditorAction("PreviousWord")
        myFixture.checkResult("foo<caret>Bar")
    }

}
