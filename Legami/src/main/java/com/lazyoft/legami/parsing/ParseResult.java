package com.lazyoft.legami.parsing;

public class ParseResult {
    private final Token root;
    private final String error;
    private final boolean hasError;

    public ParseResult(String error) {
        this.root = Token.NotFound;
        this.error = error;
        this.hasError = true;
    }

    public ParseResult(Token root) {
        this.root = root;
        this.error = "";
        this.hasError = false;
    }

    public Token getRoot() {
        return root;
    }

    public String error() {
        return error;
    }

    public boolean hasError() {
        return hasError;
    }

    @Override
    public String toString() {
        return root.toString() + error;
    }
}
