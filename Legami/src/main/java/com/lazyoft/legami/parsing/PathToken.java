package com.lazyoft.legami.parsing;

import java.util.ArrayList;
import java.util.List;

public class PathToken extends Token {
    private PathToken(Object... tokens) {
        super(tokens);
    }

    public static Token produce(Scanner scanner) {
        List<Token> result = new ArrayList<Token>();
        scanner.start();

        // path = identifier *[dotted-identifier]
        Token identifier = IdentifierToken.produce(scanner);
        while(identifier != Token.Empty) {
            result.add(identifier);
            if(scanner.peek() == Terminals.Dot)
                scanner.advance();
             else
                break;
            identifier = IdentifierToken.produce(scanner);
        }

        if(result.isEmpty())
            return scanner.error("Expected path");

        scanner.commit();
        return new PathToken(result);
    }
}
