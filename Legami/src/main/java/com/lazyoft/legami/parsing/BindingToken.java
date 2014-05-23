package com.lazyoft.legami.parsing;

public class BindingToken extends Token {
    private BindingToken(Object... tokens) {
        super(tokens);
    }

    @Override
    public String toString() {
        return getTokens().get(0).toString();
    }

    public static Token parse(TokenSource source) {
        // binding = (binding-expression / binding-list) *ws
        source.startScan();
        Token binding = BindingExpressionToken.parse(source);
        if(binding == Token.NotFound) {
            binding = BindingListToken.parse(source);
            if(binding == Token.NotFound)
                return source.error("Expected binding expression or binding list in binding");
        }

        source.consumeWhitespace();
        if(!source.atEnd())
            return source.error("Expected end of binding");

        source.endScan();
        return new BindingToken(binding);
    }
}
