package com.lazyoft.legami.parsing;

public class ConstantLiteralToken extends Token {
    private ConstantLiteralToken(Object... tokens) {
        super(tokens);
    }

    public static Token parse(TokenSource source) {
        source.startScan();

        // constant-literal = hash identifier
        if(source.next() == Terminals.Hash) {
            Token identifier = IdentifierToken.parse(source);
            if (identifier == Token.NotFound)
                return source.error("Expected identifier in constant literal");

            source.endScan();
            return new ConstantLiteralToken(identifier);
        }

        return source.error("Expected constant literal prefix");
    }
}
