package com.lazyoft.legami.parsing;

public class ConstantLiteralToken extends Token {
    private String name;

    private ConstantLiteralToken(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Constant[" + name + "]";
    }

    public static Token parse(TokenSource source) {
        source.startScan();

        // constant-literal = hash identifier
        if(source.next() == Terminals.Hash) {
            Token identifier = IdentifierToken.parse(source);
            if (identifier == Token.NotFound)
                return source.error("Expected identifier in constant literal");

            source.endScan();
            return new ConstantLiteralToken(((IdentifierToken)identifier).getName());
        }

        return source.error("Expected constant literal prefix");
    }
}
