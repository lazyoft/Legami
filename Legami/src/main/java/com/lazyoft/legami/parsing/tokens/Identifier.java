package com.lazyoft.legami.parsing.tokens;

import com.lazyoft.legami.parsing.Scanner;

import java.util.ArrayList;
import java.util.List;

public class Identifier extends Token {
    private Identifier(Object ...tokens) {
        super(tokens);
    }

    static Token produce(Scanner scanner) {
        List<Token> result = new ArrayList<Token>();
        scanner.start();

        // identifier = letter / underscore *[letter / underscore / digit]
        Token current = scanner.peek();
        if(current instanceof Terminals.Letter || current == Terminals.Underscore) {
            do {
                scanner.advance();
                result.add(current);
                current = scanner.peek();
            }
            while (current != Token.Empty && (current instanceof Terminals.Letter || current == Terminals.Underscore || current instanceof Terminals.Digit));
        }

        if(result.isEmpty())
            return scanner.error("Expected identifier");

        scanner.commit();
        return new Identifier(result);
    }
}
