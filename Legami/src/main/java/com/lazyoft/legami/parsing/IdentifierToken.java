package com.lazyoft.legami.parsing;

import java.util.ArrayList;
import java.util.List;

public class IdentifierToken extends Token {
    private IdentifierToken(Object... tokens) {
        super(tokens);
    }

    static Token parse(TokenSource source) {
        List<Token> result = new ArrayList<Token>();
        source.startScan();

        // identifier = letter / underscore *[letter / underscore / digit]
        Token current = source.peek();
        if(current instanceof Terminals.Letter || current == Terminals.Underscore) {
            do {
                source.advance();
                result.add(current);
                current = source.peek();
            }
            while (current != Token.NotFound && (current instanceof Terminals.Letter || current == Terminals.Underscore || current instanceof Terminals.Digit));
        }

        if(result.isEmpty())
            return source.error("Expected identifier");

        source.endScan();
        return new IdentifierToken(result);
    }
}
