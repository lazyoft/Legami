package com.lazyoft.legami.parsing;

import com.lazyoft.legami.binding.ITokenVisitor;

public class ConversionToken extends Token {
    private ConversionToken(Object... tokens) {
        super(tokens);
    }

    public String getName() {
        return ((IdentifierToken)getTokens().get(0)).getName();
    }

    public Token getPath() {
        return getTokens().get(1);
    }

    public Token getParameter() {
        return getTokens().size() > 2 ? getTokens().get(2) : Token.NotFound;
    }

    @Override
    public String toString() {
        String parameter = getParameter() == Token.NotFound ? "" : ", " + getParameter().toString();
        return "Conversion[" + getName() + "(" + getPath() + parameter + ")]";
    }

    @Override
    public <TResult> TResult acceptVisitor(ITokenVisitor<TResult> visitor, TResult param) {
        return visitor.visit(this, param);
    }

    public static Token parse(TokenSource source) {
        // conversion = identifier open-paren *ws path *ws [comma *ws [string-literal / constant-literal ] *ws close-paren
        source.startScan();

        Token identifier = IdentifierToken.parse(source);
        if(identifier == Token.NotFound)
            return source.error("Expected identifier in conversion");

        if(source.next() != Terminals.OpenParen)
            return source.error("Missing open parenthesis in conversion");

        Token path = PathToken.parse(source);
        if(path == Token.NotFound)
            return source.error("Expected path in conversion");
        source.consumeWhitespace();

        Token remainder = Token.NotFound;
        if(source.peek() == Terminals.Comma) {
            source.advance();
            source.consumeWhitespace();

            remainder = StringLiteralToken.parse(source);
            if (remainder == Token.NotFound)
                remainder = ConstantLiteralToken.parse(source);

            if (remainder == Token.NotFound)
                return source.error("Expecting string literal or constant expression in conversion");

            source.consumeWhitespace();
        }

        if(source.next() != Terminals.CloseParen)
            return source.error("Missing close parenthesis in conversion");

        source.endScan();
        if(remainder != Token.NotFound)
            return new ConversionToken(identifier, path, remainder);
        else
            return new ConversionToken(identifier, path);
    }
}
