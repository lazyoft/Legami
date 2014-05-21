package com.lazyoft.legami.parsing;

public class ConstantLiteralToken extends Token {
    private ConstantLiteralToken(Object... tokens) {
        super(tokens);
    }

    public static Token produce(Scanner scanner) {
        scanner.start();

        // constant-literal = hash identifier
        if(scanner.next() == Terminals.Hash) {
            Token identifier = IdentifierToken.produce(scanner);
            if (identifier == null)
                return scanner.error("Expected identifier in constant literal");

            scanner.commit();
            return new ConstantLiteralToken(identifier);
        }

        return scanner.error("Expected constant literal prefix");
    }
}
