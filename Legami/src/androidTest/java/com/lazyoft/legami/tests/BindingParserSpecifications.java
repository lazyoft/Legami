package com.lazyoft.legami.tests;

import com.lazyoft.legami.parsing.BindingParser;

import junit.framework.TestCase;

public class BindingParserSpecifications extends TestCase {
    public void testShouldParseSimpleAssignments() {
        assertEquals("Binding[foo := Path[Identifier[bar]]]", BindingParser.getTokenized("foo := bar"));
    }

    public void testShouldFailIfTheAssignmentIsIncomplete() {
        assertTrue(BindingParser.parse("foo := ").hasError());
    }

    public void testShouldFailIfTheOperatorIsUnrecognized() {
        assertTrue(BindingParser.parse("foo += bar").hasError());
    }

    public void testShouldFailIfThereIsNoIdentifierAtTheStart() {
        assertTrue(BindingParser.parse(":= bar").hasError());
    }

    public void testShouldParsePathAssignments() {
        assertEquals("Binding[foo := Path[Identifier[bar], Identifier[baz]]]", BindingParser.getTokenized("foo := bar.baz"));
    }

    public void testShouldIgnoreEndingPathSpecifiersInPath() {
        assertEquals("Binding[foo := Path[Identifier[bar], Identifier[baz]]]", BindingParser.getTokenized("foo := bar.baz."));
    }

    public void testShouldParseConstantAssignments() {
        assertEquals("Binding[foo := Constant[constantValue]]", BindingParser.getTokenized("foo := #constantValue"));
    }

    public void testShouldFailIfTheConstantAssignmentIsIncomplete() {
        assertTrue(BindingParser.parse("foo := #").hasError());
    }

    public void testShouldParseSimpleConversions() {
        assertEquals("Binding[foo := Conversion[convert(Path[Identifier[bar]])]]", BindingParser.getTokenized("foo := convert(bar)"));
    }

    public void testShouldFailIfTheConversionIsMissingAParenthesis() {
        assertTrue(BindingParser.parse("foo := convert(bar").hasError());
    }

    public void testShouldParseConversionsWithStringParameters() {
        assertEquals("Binding[foo := Conversion[convert(Path[Identifier[bar], Identifier[baz]], String[string literal])]]", BindingParser.getTokenized("foo := convert(bar.baz, /string literal/)"));
    }

    public void testShouldParseConversionsWithEscapedStringsParameters() {
        assertEquals("Binding[foo := Conversion[convert(Path[Identifier[bar], Identifier[baz]], String[string/literal])]]", BindingParser.getTokenized("foo := convert(bar.baz, /string\\/literal/)"));
    }

    public void testShouldParseConversionsWithEscapedStringsParametersPreservingRealEscapedText() {
        assertEquals("Binding[foo := Conversion[convert(Path[Identifier[bar], Identifier[baz]], String[string/literal\\nNew line])]]", BindingParser.getTokenized("foo := convert(bar.baz, /string\\/literal\\nNew line/)"));
    }

    public void testShouldFailTheConversionIfMissingTheParameterWithAComma() {
        assertTrue(BindingParser.parse("foo := convert(bar,)").hasError());
    }

    public void testShouldParseConversionsWithConstantParameters() {
        assertEquals("Binding[foo := Conversion[convert(Path[Identifier[bar], Identifier[baz]], Constant[constantValue])]]", BindingParser.getTokenized("foo := convert(bar.baz, #constantValue)"));
    }

    public void testShouldFailTheConversionIfMissingTheIdentifierInAConstantParameter() {
        assertTrue(BindingParser.parse("foo := convert(bar.baz, #)").hasError());
    }

    public void testShouldParseMultipleBindingsIfEnclosedInAngularBrackets() {
        assertEquals("Binding[foo := Path[Identifier[bar]]], Binding[baz =: Path[Identifier[goo]]]", BindingParser.getTokenized("{foo := bar} {baz =: goo}"));
    }

    public void testShouldParseMultipleBindingsIfEnclosedInAngularBracketsRegardlessOfWhitespace() {
        assertEquals("Binding[foo := Path[Identifier[bar]]], Binding[baz =: Path[Identifier[goo]]]", BindingParser.getTokenized("    {foo := bar} {baz =: goo}  "));
    }

    public void testShouldFailMultipleBindingsNotEnclosedInAngularBrackets() {
        assertTrue(BindingParser.parse("{foo := bar} baz := goo").hasError());
    }
}