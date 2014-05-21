package com.lazyoft.legami.parsing.tokens;

import com.lazyoft.legami.parsing.Scanner;

public class Binding extends Token {
    private Binding(Object ...tokens) {
        super(tokens);
    }

    public static Token produce(Scanner scanner) {
        // binding = (binding-expression / binding-list) *ws
        scanner.start();
        Token binding = BindingExpression.produce(scanner);
        if(binding == Token.Empty) {
            binding = BindingList.produce(scanner);
            if(binding == Token.Empty)
                return scanner.error("Expected binding expression or binding list in binding");
        }

        scanner.consumeWhitespace();
        if(!scanner.atEnd())
            return scanner.error("Expected end of binding");

        scanner.commit();
        return new Binding(binding);
    }
}
