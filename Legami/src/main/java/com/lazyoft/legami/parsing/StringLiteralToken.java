package com.lazyoft.legami.parsing;

public class StringLiteralToken extends Token {
    private String text;

    private StringLiteralToken(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "String[" + text + "]";
    }

    @Override
    public <TResult> TResult acceptVisitor(ITokenVisitor<TResult> visitor, TResult param) {
        return visitor.visit(this, param);
    }

    public static Token parse(TokenSource source) {
        StringBuilder result = new StringBuilder();
        source.startScan();

        // string-literal = quote *char-in-string quote
        Token current = source.next();
        if(current == Terminals.Quote) {
            do {
                result.append(source.next().toString());
                current = source.peek();
                if(current == Terminals.Escape && source.peek(1) == Terminals.Quote)
                    source.advance();
            }
            while (current != Token.NotFound && (current != Terminals.Quote) || (current == Terminals.Quote && source.peek(-1) == Terminals.Escape));
        }

        if(result.length() == 0)
            return source.error("Expected string literal");

        source.advance();
        source.endScan();
        return new StringLiteralToken(result.toString());
    }
}
