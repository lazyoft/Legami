package com.lazyoft.legami.parsing.tokens;

import com.lazyoft.legami.parsing.Scanner;

public class Conversion extends Token {
    private Conversion(Object ...tokens) {
        super(tokens);
    }

    public static Token produce(Scanner scanner) {
        // conversion = identifier open-paren *ws path *ws [comma *ws [string-literal / constant-literal ] *ws close-paren
        scanner.start();

        Token identifier = Identifier.produce(scanner);
        if(identifier == Token.Empty)
            return scanner.error("Expected identifier in conversion");

        if(!(scanner.peek() == Terminals.OpenParen))
            return scanner.error("Missing open parenthesis in conversion");

        scanner.advance();
        Token path = Path.produce(scanner);
        if(path == Token.Empty)
            return scanner.error("Expected path in conversion");
        scanner.consumeWhitespace();

        Token remainder = Token.Empty;
        if(scanner.peek() == Terminals.Comma) {
            scanner.advance();
            scanner.consumeWhitespace();

            remainder = StringLiteral.produce(scanner);
            if (remainder == Token.Empty)
                remainder = ConstantLiteral.produce(scanner);

            if (remainder == Token.Empty)
                return scanner.error("Expecting string literal or constant expression in conversion");

            scanner.consumeWhitespace();
        }

        if(!(scanner.peek() == Terminals.CloseParen))
            return scanner.error("Missing close parenthesis in conversion");

        scanner.advance();
        scanner.commit();
        if(remainder != Token.Empty)
            return new Conversion(identifier, path, remainder);
        else
            return new Conversion(identifier, path);
    }
}
