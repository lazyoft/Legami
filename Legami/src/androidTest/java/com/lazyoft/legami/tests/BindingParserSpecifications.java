package com.lazyoft.legami.tests;

import com.lazyoft.legami.parsing.BindingParser;

import junit.framework.TestCase;

public class BindingParserSpecifications extends TestCase {
    public void test1() {
        assertEquals("", BindingParser.getTokenized("foo := function(One.Two.Three, #barbapapa)"));
    }

    public void test2() {
        assertEquals("", BindingParser.getTokenized("foo := #barbapapa"));
    }

    public void test3() {
        assertEquals("", BindingParser.getTokenized("foo := One.Two.Three"));
    }

    public void test4() {
        assertEquals("", BindingParser.getTokenized("foo := function(One.Two.Three, /string literal/)"));
    }

    public void test5() {
        assertEquals("", BindingParser.getTokenized("foo := function(One.Two.Three, /string literal with an \\/escape\\/ quote/)"));
    }

    public void test6() {
        assertEquals("", BindingParser.getTokenized("foo := function(One.Two.Three, /string literal with weird chars '+?^%@àèìòù/)"));
    }

    public void test7() {
        assertEquals("", BindingParser.getTokenized("{ foo := function(One.Two.Three, /string literal with weird chars '+?^%@àèìòù/) } { second := #res } { third := some.path }"));
    }

    public void test8() {
        assertEquals("", BindingParser.getTokenized("{ foo := function(One.Two.Three, /string literal with weird chars '+?^%@àèìòù/) } second := #res"));
    }

}