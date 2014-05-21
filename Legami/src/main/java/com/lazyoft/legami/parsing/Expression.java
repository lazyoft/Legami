package com.lazyoft.legami.parsing;

public class Expression extends Token {
    private Expression(Object ...tokens) {
        super(tokens);
    }

    public static Token produce(Scanner scanner) {
        // expression = constant-literal / conversion / path
        scanner.start();

        Token expression = Conversion.produce(scanner);
        if(expression == Token.Empty) {
            expression = ConstantLiteral.produce(scanner);
            if(expression == Token.Empty) {
                expression = Path.produce(scanner);
            }
        }
        if(expression == null)
            return scanner.error("Expected constant expression, conversion expression or path");

        scanner.commit();
        return new Expression(expression);
    }
}

