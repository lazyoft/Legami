package com.lazyoft.legami.parsing;

import java.util.ArrayList;
import java.util.List;

public class StringLiteral extends Token {
    private StringLiteral(Object ...tokens) {
        super(tokens);
    }

    public static Token produce(Scanner scanner) {
        List<Token> result = new ArrayList<Token>();
        scanner.start();

        // string-literal = quote *char-in-string quote
        Token current = scanner.next();
        if(current == Terminals.Quote) {
            do {
                result.add(scanner.next());
                current = scanner.peek();
            }
            while (current != Token.Empty && (current != Terminals.Quote) || (current == Terminals.Quote && scanner.peek(-1) == Terminals.Escape));
        }

        if(result.isEmpty())
            return scanner.error("Expected string literal");

        scanner.advance();
        scanner.commit();
        return new StringLiteral(result);
    }
}
