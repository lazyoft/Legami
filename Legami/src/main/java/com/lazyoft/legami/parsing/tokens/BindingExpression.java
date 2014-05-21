package com.lazyoft.legami.parsing.tokens;

import com.lazyoft.legami.parsing.Scanner;

public class BindingExpression extends Token {
    private BindingExpression(Object ...tokens) {
        super(tokens);
    }

    public static Token produce(Scanner scanner) {
        // binding-expression = *ws identifier *ws operator *ws expression
        scanner.start();

        scanner.consumeWhitespace();
        Token identifier = Identifier.produce(scanner);
        if(identifier == Token.Empty)
            return scanner.error("Expected identifier in binding expression");

        scanner.consumeWhitespace();
        Token operator = Operator.produce(scanner);
        if(operator == Token.Empty)
            return scanner.error("Expected operator in binding expression");

        scanner.consumeWhitespace();
        Token expression = Expression.produce(scanner);
        if(expression == Token.Empty)
            return scanner.error("Expected expression in binding expression");

        scanner.commit();
        return new BindingExpression(identifier, operator, expression);
    }
}
