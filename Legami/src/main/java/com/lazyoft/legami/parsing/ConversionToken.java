package com.lazyoft.legami.parsing;

public class ConversionToken extends Token {
    private ConversionToken(Object... tokens) {
        super(tokens);
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
        if(source.next() == Terminals.Comma) {
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
