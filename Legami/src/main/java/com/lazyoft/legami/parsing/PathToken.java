package com.lazyoft.legami.parsing;

import java.util.ArrayList;
import java.util.List;

public class PathToken extends Token {
    private PathToken(Object... tokens) {
        super(tokens);
    }

    public static Token parse(TokenSource source) {
        List<Token> result = new ArrayList<Token>();
        source.startScan();

        // path = identifier *[dotted-identifier]
        Token identifier = IdentifierToken.parse(source);
        while(identifier != Token.NotFound) {
            result.add(identifier);
            if(source.peek() == Terminals.Dot)
                source.advance();
             else
                break;
            identifier = IdentifierToken.parse(source);
        }

        if(result.isEmpty())
            return source.error("Expected path");

        source.endScan();
        return new PathToken(result);
    }
}
