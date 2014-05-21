package com.lazyoft.legami.parsing;

import java.util.ArrayList;
import java.util.List;

public class StringLiteralToken extends Token {
    private StringLiteralToken(Object... tokens) {
        super(tokens);
    }

    public static Token parse(TokenSource source) {
        List<Token> result = new ArrayList<Token>();
        source.startScan();

        // string-literal = quote *char-in-string quote
        Token current = source.next();
        if(current == Terminals.Quote) {
            do {
                result.add(source.next());
                current = source.peek();
            }
            while (current != Token.NotFound && (current != Terminals.Quote) || (current == Terminals.Quote && source.peek(-1) == Terminals.Escape));
        }

        if(result.isEmpty())
            return source.error("Expected string literal");

        source.advance();
        source.endScan();
        return new StringLiteralToken(result);
    }
}
