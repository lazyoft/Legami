package com.lazyoft.legami.parsing;

public class ExpressionToken extends Token {
    private ExpressionToken(Object... tokens) {
        super(tokens);
    }

    public static Token produce(Scanner scanner) {
        // expression = constant-literal / conversion / path
        scanner.start();

        Token expression = ConversionToken.produce(scanner);
        if(expression == Token.Empty) {
            expression = ConstantLiteralToken.produce(scanner);
            if(expression == Token.Empty) {
                expression = PathToken.produce(scanner);
            }
        }
        if(expression == null)
            return scanner.error("Expected constant expression, conversion expression or path");

        scanner.commit();
        return new ExpressionToken(expression);
    }
}

