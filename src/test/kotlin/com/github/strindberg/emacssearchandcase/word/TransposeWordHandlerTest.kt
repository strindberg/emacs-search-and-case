package com.github.strindberg.emacssearchandcase.word

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TransposeWordHandlerTest : BasePlatformTestCase() {

    @Test
    fun `transpose 00`() {
        myFixture.configureByText("file.txt", "<caret>foo bar")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("<caret>foo bar")
    }

    @Test
    fun `transpose 01`() {
        myFixture.configureByText("file.txt", " <caret>foo bar")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult(" <caret>foo bar")
    }

    @Test
    fun `transpose 02`() {
        myFixture.configureByText("file.txt", "<caret> foo bar")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("<caret> foo bar")
    }

    @Test
    fun `transpose 03`() {
        myFixture.configureByText("file.txt", "f<caret>oo bar")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("bar foo<caret>")
    }

    @Test
    fun `transpose 04`() {
        myFixture.configureByText("file.txt", "foo<caret> bar")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("bar foo<caret>")
    }

    @Test
    fun `transpose 05`() {
        myFixture.configureByText("file.txt", "foo <caret>bar")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("bar foo<caret>")
    }

    @Test
    fun `transpose 06`() {
        myFixture.configureByText("file.txt", "foo b<caret>ar")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("foo bar<caret>")
    }

    @Test
    fun `transpose 07`() {
        myFixture.configureByText("file.txt", "foo bar<caret>")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("foo bar<caret>")
    }

    @Test
    fun `transpose 10`() {
        myFixture.configureByText("file.txt", "f<caret>oo + bar")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("bar + foo<caret>")
    }

    @Test
    fun `transpose 11`() {
        myFixture.configureByText("file.txt", "fo<caret>o + bar")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("bar + foo<caret>")
    }

    @Test
    fun `transpose 12`() {
        myFixture.configureByText("file.txt", "foo<caret> + bar")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("bar + foo<caret>")
    }

    @Test
    fun `transpose 13`() {
        myFixture.configureByText("file.txt", "foo <caret>+ bar")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("bar + foo<caret>")
    }

    @Test
    fun `transpose 14`() {
        myFixture.configureByText("file.txt", "foo +<caret> bar")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("bar + foo<caret>")
    }

    @Test
    fun `transpose 15`() {
        myFixture.configureByText("file.txt", "foo + <caret>bar")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("bar + foo<caret>")
    }

    @Test
    fun `transpose 16`() {
        myFixture.configureByText("file.txt", "foo + b<caret>ar")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("foo + bar<caret>")
    }

    @Test
    fun `transpose 17`() {
        myFixture.configureByText("file.txt", "foo + bar<caret>")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("foo + bar<caret>")
    }

    @Test
    fun `transpose 20`() {
        myFixture.configureByText("file.txt", "fo<caret>o.bar")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("bar.foo<caret>")
    }

    @Test
    fun `transpose 21`() {
        myFixture.configureByText("file.txt", "foo<caret>.bar")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("bar.foo<caret>")
    }

    @Test
    fun `transpose 22`() {
        myFixture.configureByText("file.txt", "foo.<caret>bar")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("bar.foo<caret>")
    }

    @Test
    fun `transpose 23`() {
        myFixture.configureByText("file.txt", "foo.b<caret>ar")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("foo.bar<caret>")
    }

    @Test
    fun `transpose 31`() {
        myFixture.configureByText("file.txt", "<caret><selection>foo b</selection>ar")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("ar<selection>foo b<caret></selection>")
    }

    @Test
    fun `transpose 32`() {
        myFixture.configureByText("file.txt", "<selection>foo b<caret></selection>ar")
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("ar<selection>foo b<caret></selection>")
    }

    @Test
    fun `transpose 41`() {
        myFixture.configureByText("file.txt", "foo<caret>Bar")
        myFixture.editor.settings.isCamelWords = true
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("barFoo<caret>")
    }

    @Test
    fun `transpose 42`() {
        myFixture.configureByText("file.txt", "F<caret>ooBar")
        myFixture.editor.settings.isCamelWords = true
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("BarFoo<caret>")
    }

    @Test
    fun `transpose 43`() {
        myFixture.configureByText("file.txt", "Foo<caret>Bar")
        myFixture.editor.settings.isCamelWords = true
        myFixture.performEditorAction("TransposeWords")
        myFixture.checkResult("BarFoo<caret>")
    }

    @Test
    fun `reverse transpose 01`() {
        myFixture.configureByText("file.txt", "<caret>foo bar")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<caret>foo bar")
    }

    @Test
    fun `reverse transpose 02`() {
        myFixture.configureByText("file.txt", "fo<caret>o bar")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<caret>foo bar")
    }

    @Test
    fun `reverse transpose 03`() {
        myFixture.configureByText("file.txt", "foo<caret> bar")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<caret>bar foo")
    }

    @Test
    fun `reverse transpose 04`() {
        myFixture.configureByText("file.txt", "foo <caret>bar")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<caret>bar foo")
    }

    @Test
    fun `reverse transpose 05`() {
        myFixture.configureByText("file.txt", "foo b<caret>ar")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<caret>bar foo")
    }

    @Test
    fun `reverse transpose 06`() {
        myFixture.configureByText("file.txt", "foo bar<caret>")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("foo bar<caret>")
    }

    @Test
    fun `reverse transpose 07`() {
        myFixture.configureByText("file.txt", "foo bar<caret> ")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("foo bar<caret> ")
    }

    @Test
    fun `reverse transpose 08`() {
        myFixture.configureByText("file.txt", "foo bar <caret>")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("foo bar <caret>")
    }

    @Test
    fun `reverse transpose 10`() {
        myFixture.configureByText("file.txt", "foo () b<caret>ar")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<caret>bar () foo")
    }

    @Test
    fun `reverse transpose 11`() {
        myFixture.configureByText("file.txt", "foo () <caret>bar")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<caret>bar () foo")
    }

    @Test
    fun `reverse transpose 12`() {
        myFixture.configureByText("file.txt", "foo ()<caret> bar")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<caret>bar () foo")
    }

    @Test
    fun `reverse transpose 13`() {
        myFixture.configureByText("file.txt", "foo (<caret>) bar")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<caret>bar () foo")
    }

    @Test
    fun `reverse transpose 14`() {
        myFixture.configureByText("file.txt", "foo <caret>() bar")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<caret>bar () foo")
    }

    @Test
    fun `reverse transpose 15`() {
        myFixture.configureByText("file.txt", "foo<caret> () bar")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<caret>bar () foo")
    }

    @Test
    fun `reverse transpose 16`() {
        myFixture.configureByText("file.txt", "fo<caret>o () bar")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<caret>foo () bar")
    }

    @Test
    fun `reverse transpose 20`() {
        myFixture.configureByText("file.txt", "fo<caret>o.bar")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<caret>foo.bar")
    }

    @Test
    fun `reverse transpose 21`() {
        myFixture.configureByText("file.txt", "foo<caret>.bar")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<caret>bar.foo")
    }

    @Test
    fun `reverse transpose 22`() {
        myFixture.configureByText("file.txt", "foo.<caret>bar")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<caret>bar.foo")
    }

    @Test
    fun `reverse transpose 23`() {
        myFixture.configureByText("file.txt", "foo.b<caret>ar")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<caret>bar.foo")
    }

    @Test
    fun `reverse transpose 31`() {
        myFixture.configureByText("file.txt", "fo<selection><caret>o bar</selection>")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<selection><caret>o bar</selection>fo")
    }

    @Test
    fun `reverse transpose 32`() {
        myFixture.configureByText("file.txt", "fo<selection>o bar<caret></selection>")
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<selection><caret>o bar</selection>fo")
    }

    @Test
    fun `reverse transpose 41`() {
        myFixture.configureByText("file.txt", "fooBa<caret>r")
        myFixture.editor.settings.isCamelWords = true
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<caret>barFoo")
    }

    @Test
    fun `reverse transpose 42`() {
        myFixture.configureByText("file.txt", "Foo<caret>Bar")
        myFixture.editor.settings.isCamelWords = true
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<caret>BarFoo")
    }

    @Test
    fun `reverse transpose 43`() {
        myFixture.configureByText("file.txt", "FooB<caret>ar")
        myFixture.editor.settings.isCamelWords = true
        myFixture.performEditorAction("ReverseTransposeWords")
        myFixture.checkResult("<caret>BarFoo")
    }

}
