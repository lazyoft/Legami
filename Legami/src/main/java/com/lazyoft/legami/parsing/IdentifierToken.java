package com.lazyoft.legami.parsing;

public class IdentifierToken extends Token {
    private String name;

    private IdentifierToken(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <TResult> TResult acceptVisitor(ITokenVisitor<TResult> visitor, TResult param) {
        return visitor.visit(this, param);
    }

    @Override
    public String toString() {
        return "Identifier[" + name + "]";
    }

    static Token parse(TokenSource source) {
        StringBuilder result = new StringBuilder();
        source.startScan();

        // identifier = letter / underscore *[letter / underscore / digit]
        Token current = source.peek();
        if(current instanceof Terminals.Letter || current == Terminals.Underscore) {
            do {
                source.advance();
                result.append(current.toString());
                current = source.peek();
            }
            while (current != Token.NotFound && (current instanceof Terminals.Letter || current == Terminals.Underscore || current instanceof Terminals.Digit));
        }

        if(result.length() == 0)
            return source.error("Expected identifier");

        source.endScan();
        return new IdentifierToken(result.toString());
    }
}
