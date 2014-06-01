package com.lazyoft.legami.parsing;

public class BindingParser implements IBindingParser {
    public ParseResult parse(String source) {
        TokenSource tokenSource = new TokenSource(source);
        Token result = BindingToken.parse(tokenSource);
        return result == Token.NotFound ? new ParseResult(tokenSource.getErrorMessage()) : new ParseResult(result);
    }

    public String getTokenized(String source) {
        return parse(source).toString();
    }
}

