package com.lazyoft.legami.parsing;

public class BindingExpressionToken extends Token {
    private BindingExpressionToken(Object... tokens) {
        super(tokens);
    }

    public static Token produce(Scanner scanner) {
        // binding-expression = *ws identifier *ws operator *ws expression
        scanner.start();

        scanner.consumeWhitespace();
        Token identifier = IdentifierToken.produce(scanner);
        if(identifier == Token.Empty)
            return scanner.error("Expected identifier in binding expression");

        scanner.consumeWhitespace();
        Token operator = OperatorToken.produce(scanner);
        if(operator == Token.Empty)
            return scanner.error("Expected operator in binding expression");

        scanner.consumeWhitespace();
        Token expression = ExpressionToken.produce(scanner);
        if(expression == Token.Empty)
            return scanner.error("Expected expression in binding expression");

        scanner.commit();
        return new BindingExpressionToken(identifier, operator, expression);
    }
}
