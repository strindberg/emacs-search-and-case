package com.github.strindberg.emacssearchandcase.word

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WordChangeHandlerTest : BasePlatformTestCase() {

    @Test
    fun `capitalize word 00`() {
        myFixture.configureByText("file.txt", "<caret>foo bar")
        myFixture.performEditorAction("CapitalCase")
        myFixture.checkResult("Foo<caret> bar")
    }

    @Test
    fun `capitalize word 01`() {
        myFixture.configureByText("file.txt", "fo<caret>o bar")
        myFixture.performEditorAction("CapitalCase")
        myFixture.checkResult("foO<caret> bar")
    }

    @Test
    fun `capitalize word 02`() {
        myFixture.configureByText("file.txt", "foo<caret> bar")
        myFixture.performEditorAction("CapitalCase")
        myFixture.checkResult("foo Bar<caret>")
    }

    @Test
    fun `capitalize word 03`() {
        myFixture.configureByText("file.txt", "foo <caret>bar")
        myFixture.performEditorAction("CapitalCase")
        myFixture.checkResult("foo Bar<caret>")
    }

    @Test
    fun `capitalize word 04`() {
        myFixture.configureByText("file.txt", "foo b<caret>ar")
        myFixture.performEditorAction("CapitalCase")
        myFixture.checkResult("foo bAr<caret>")
    }

    @Test
    fun `capitalize word 05`() {
        myFixture.configureByText("file.txt", "foo bar<caret>")
        myFixture.performEditorAction("CapitalCase")
        myFixture.checkResult("foo bar<caret>")
    }

    @Test
    fun `capitalize word 06`() {
        myFixture.configureByText("file.txt", "<caret>Foo bar")
        myFixture.performEditorAction("CapitalCase")
        myFixture.checkResult("Foo<caret> bar")
    }

    @Test
    fun `capitalize word 07`() {
        myFixture.configureByText("file.txt", "foo <caret>Bar")
        myFixture.performEditorAction("CapitalCase")
        myFixture.checkResult("foo Bar<caret>")
    }

    @Test
    fun `capitalize word 10`() {
        myFixture.configureByText("file.txt", "<caret> + - () bar")
        myFixture.performEditorAction("CapitalCase")
        myFixture.checkResult(" + - () Bar<caret>")
    }

    @Test
    fun `capitalize word 11`() {
        myFixture.configureByText("file.txt", "<selection><caret>foo - b</selection>ar")
        myFixture.performEditorAction("CapitalCase")
        myFixture.checkResult("Foo - b<caret>ar")
    }

    @Test
    fun `capitalize word 20`() {
        myFixture.configureByText("file.txt", "<caret>fooBar")
        myFixture.editor.settings.isCamelWords = true
        myFixture.performEditorAction("CapitalCase")
        myFixture.checkResult("Foo<caret>Bar")
    }

    @Test
    fun `upper case word 00`() {
        myFixture.configureByText("file.txt", "<caret>foo bar")
        myFixture.performEditorAction("UpperCase")
        myFixture.checkResult("FOO<caret> bar")
    }

    @Test
    fun `upper case word 01`() {
        myFixture.configureByText("file.txt", "fo<caret>o bar")
        myFixture.performEditorAction("UpperCase")
        myFixture.checkResult("foO<caret> bar")
    }

    @Test
    fun `upper case word 02`() {
        myFixture.configureByText("file.txt", "foo<caret> bar")
        myFixture.performEditorAction("UpperCase")
        myFixture.checkResult("foo BAR<caret>")
    }

    @Test
    fun `upper case word 03`() {
        myFixture.configureByText("file.txt", "foo <caret>bar")
        myFixture.performEditorAction("UpperCase")
        myFixture.checkResult("foo BAR<caret>")
    }

    @Test
    fun `upper case word 04`() {
        myFixture.configureByText("file.txt", "foo b<caret>ar")
        myFixture.performEditorAction("UpperCase")
        myFixture.checkResult("foo bAR<caret>")
    }

    @Test
    fun `upper case word 05`() {
        myFixture.configureByText("file.txt", "foo bar<caret>")
        myFixture.performEditorAction("UpperCase")
        myFixture.checkResult("foo bar<caret>")
    }

    @Test
    fun `upper case word 06`() {
        myFixture.configureByText("file.txt", "<caret>Foo bar")
        myFixture.performEditorAction("UpperCase")
        myFixture.checkResult("FOO<caret> bar")
    }

    @Test
    fun `upper case word 07`() {
        myFixture.configureByText("file.txt", "foo <caret>Bar")
        myFixture.performEditorAction("UpperCase")
        myFixture.checkResult("foo BAR<caret>")
    }

    @Test
    fun `upper case word 08`() {
        myFixture.configureByText("file.txt", "<caret>FOO bar")
        myFixture.performEditorAction("UpperCase")
        myFixture.checkResult("FOO<caret> bar")
    }

    @Test
    fun `upper case word 09`() {
        myFixture.configureByText("file.txt", "foo <caret>BAR")
        myFixture.performEditorAction("UpperCase")
        myFixture.checkResult("foo BAR<caret>")
    }

    @Test
    fun `upper case word 10`() {
        myFixture.configureByText("file.txt", "<caret> + - () bar")
        myFixture.performEditorAction("UpperCase")
        myFixture.checkResult(" + - () BAR<caret>")
    }

    @Test
    fun `upper case word 11`() {
        myFixture.configureByText("file.txt", "<selection><caret>foo - b</selection>ar")
        myFixture.performEditorAction("UpperCase")
        myFixture.checkResult("FOO - B<caret>ar")
    }

    @Test
    fun `upper case word 20`() {
        myFixture.configureByText("file.txt", "<caret>fooBar")
        myFixture.editor.settings.isCamelWords = true
        myFixture.performEditorAction("UpperCase")
        myFixture.checkResult("FOO<caret>Bar")
    }

    @Test
    fun `lower case word 00`() {
        myFixture.configureByText("file.txt", "<caret>FOO bar")
        myFixture.performEditorAction("LowerCase")
        myFixture.checkResult("foo<caret> bar")
    }

    @Test
    fun `lower case word 01`() {
        myFixture.configureByText("file.txt", "fo<caret>O bar")
        myFixture.performEditorAction("LowerCase")
        myFixture.checkResult("foo<caret> bar")
    }

    @Test
    fun `lower case word 02`() {
        myFixture.configureByText("file.txt", "foo<caret> BAR")
        myFixture.performEditorAction("LowerCase")
        myFixture.checkResult("foo bar<caret>")
    }

    @Test
    fun `lower case word 03`() {
        myFixture.configureByText("file.txt", "foo <caret>BAR")
        myFixture.performEditorAction("LowerCase")
        myFixture.checkResult("foo bar<caret>")
    }

    @Test
    fun `lower case word 04`() {
        myFixture.configureByText("file.txt", "foo b<caret>AR")
        myFixture.performEditorAction("LowerCase")
        myFixture.checkResult("foo bar<caret>")
    }

    @Test
    fun `lower case word 05`() {
        myFixture.configureByText("file.txt", "foo BAR<caret>")
        myFixture.performEditorAction("LowerCase")
        myFixture.checkResult("foo BAR<caret>")
    }

    @Test
    fun `lower case word 06`() {
        myFixture.configureByText("file.txt", "<caret>Foo bar")
        myFixture.performEditorAction("LowerCase")
        myFixture.checkResult("foo<caret> bar")
    }

    @Test
    fun `lower case word 07`() {
        myFixture.configureByText("file.txt", "foo <caret>Bar")
        myFixture.performEditorAction("LowerCase")
        myFixture.checkResult("foo bar<caret>")
    }

    @Test
    fun `lower case word 08`() {
        myFixture.configureByText("file.txt", "<caret>foo bar")
        myFixture.performEditorAction("LowerCase")
        myFixture.checkResult("foo<caret> bar")
    }

    @Test
    fun `lower case word 09`() {
        myFixture.configureByText("file.txt", "foo <caret>bar")
        myFixture.performEditorAction("LowerCase")
        myFixture.checkResult("foo bar<caret>")
    }

    @Test
    fun `lower case word 10`() {
        myFixture.configureByText("file.txt", "<caret> + - () BAR")
        myFixture.performEditorAction("LowerCase")
        myFixture.checkResult(" + - () bar<caret>")
    }

    @Test
    fun `lower case word 11`() {
        myFixture.configureByText("file.txt", "<selection><caret>FOO - B</selection>ar")
        myFixture.performEditorAction("LowerCase")
        myFixture.checkResult("foo - b<caret>ar")
    }

    @Test
    fun `lower case word 20`() {
        myFixture.configureByText("file.txt", "F<caret>OoBar")
        myFixture.editor.settings.isCamelWords = true
        myFixture.performEditorAction("LowerCase")
        myFixture.checkResult("Foo<caret>Bar")
    }

    @Test
    fun `delete next word 00`() {
        myFixture.configureByText("file.txt", "<caret>foo bar")
        myFixture.performEditorAction("DeleteNextWord")
        myFixture.checkResult("<caret> bar")
    }

    @Test
    fun `delete next word 01`() {
        myFixture.configureByText("file.txt", "fo<caret>o bar")
        myFixture.performEditorAction("DeleteNextWord")
        myFixture.checkResult("fo<caret> bar")
    }

    @Test
    fun `delete next word 02`() {
        myFixture.configureByText("file.txt", "foo<caret> bar")
        myFixture.performEditorAction("DeleteNextWord")
        myFixture.checkResult("foo<caret>")
    }

    @Test
    fun `delete next word 03`() {
        myFixture.configureByText("file.txt", "foo <caret>bar")
        myFixture.performEditorAction("DeleteNextWord")
        myFixture.checkResult("foo <caret>")
    }

    @Test
    fun `delete next word 04`() {
        myFixture.configureByText("file.txt", "foo b<caret>ar")
        myFixture.performEditorAction("DeleteNextWord")
        myFixture.checkResult("foo b<caret>")
    }

    @Test
    fun `delete next word 05`() {
        myFixture.configureByText("file.txt", "foo bar<caret>")
        myFixture.performEditorAction("DeleteNextWord")
        myFixture.checkResult("foo bar<caret>")
    }

    @Test
    fun `delete next word 06`() {
        myFixture.configureByText("file.txt", "<caret> + - () BAR")
        myFixture.performEditorAction("DeleteNextWord")
        myFixture.checkResult("")
    }

    @Test
    fun `delete next word 10`() {
        myFixture.configureByText("file.txt", "<caret>fooBar")
        myFixture.editor.settings.isCamelWords = true
        myFixture.performEditorAction("DeleteNextWord")
        myFixture.checkResult("<caret>Bar")
    }

    @Test
    fun `delete previous word 00`() {
        myFixture.configureByText("file.txt", "<caret>foo bar")
        myFixture.performEditorAction("DeletePreviousWord")
        myFixture.checkResult("<caret>foo bar")
    }

    @Test
    fun `delete previous word 01`() {
        myFixture.configureByText("file.txt", "fo<caret>o bar")
        myFixture.performEditorAction("DeletePreviousWord")
        myFixture.checkResult("<caret>o bar")
    }

    @Test
    fun `delete previous word 02`() {
        myFixture.configureByText("file.txt", "foo<caret> bar")
        myFixture.performEditorAction("DeletePreviousWord")
        myFixture.checkResult("<caret> bar")
    }

    @Test
    fun `delete previous word 03`() {
        myFixture.configureByText("file.txt", "foo <caret>bar")
        myFixture.performEditorAction("DeletePreviousWord")
        myFixture.checkResult("<caret>bar")
    }

    @Test
    fun `delete previous word 04`() {
        myFixture.configureByText("file.txt", "foo b<caret>ar")
        myFixture.performEditorAction("DeletePreviousWord")
        myFixture.checkResult("foo <caret>ar")
    }

    @Test
    fun `delete previous word 05`() {
        myFixture.configureByText("file.txt", "foo bar<caret>")
        myFixture.performEditorAction("DeletePreviousWord")
        myFixture.checkResult("foo <caret>")
    }

    @Test
    fun `delete previous word 06`() {
        myFixture.configureByText("file.txt", "BAR) () -<caret>")
        myFixture.performEditorAction("DeletePreviousWord")
        myFixture.checkResult("")
    }

    @Test
    fun `delete previous word 10`() {
        myFixture.configureByText("file.txt", "fooBar<caret>")
        myFixture.editor.settings.isCamelWords = true
        myFixture.performEditorAction("DeletePreviousWord")
        myFixture.checkResult("foo<caret>")
    }

}
