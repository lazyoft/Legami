package com.lazyoft.legami.parsing;

import java.util.ArrayList;
import java.util.List;

public class Path extends Token {
    private Path(Object ...tokens) {
        super(tokens);
    }

    public static Token produce(Scanner scanner) {
        List<Token> result = new ArrayList<Token>();
        scanner.start();

        // path = identifier *[dotted-identifier]
        Token identifier = Identifier.produce(scanner);
        while(identifier != Token.Empty) {
            result.add(identifier);
            if(scanner.peek() == Terminals.Dot)
                scanner.advance();
             else
                break;
            identifier = Identifier.produce(scanner);
        }

        if(result.isEmpty())
            return scanner.error("Expected path");

        scanner.commit();
        return new Path(result);
    }
}
