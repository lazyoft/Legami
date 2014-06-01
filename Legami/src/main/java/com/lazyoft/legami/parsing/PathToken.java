package com.lazyoft.legami.parsing;

import java.util.ArrayList;
import java.util.List;

public class PathToken extends Token {

    private PathToken(Object ...identifiers) {
        super(identifiers);
    }

    public List<Token> getIdentifiers() {
        return getTokens();
    }

    @Override
    public String toString() {
        return "Path[" + getTokens().toString().substring(1, getTokens().toString().length() - 1) + "]";
    }

    @Override
    public <TResult> TResult acceptVisitor(ITokenVisitor<TResult> visitor, TResult param) {
        return visitor.visit(this, param);
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
