package com.lazyoft.legami.parsing;

public class BindingToken extends Token {
    private BindingToken(Object... tokens) {
        super(tokens);
    }

    public static Token produce(Scanner scanner) {
        // binding = (binding-expression / binding-list) *ws
        scanner.start();
        Token binding = BindingExpressionToken.produce(scanner);
        if(binding == Token.Empty) {
            binding = BindingListToken.produce(scanner);
            if(binding == Token.Empty)
                return scanner.error("Expected binding expression or binding list in binding");
        }

        scanner.consumeWhitespace();
        if(!scanner.atEnd())
            return scanner.error("Expected end of binding");

        scanner.commit();
        return new BindingToken(binding);
    }
}
