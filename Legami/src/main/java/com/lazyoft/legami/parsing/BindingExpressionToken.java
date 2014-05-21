package com.lazyoft.legami.parsing;

public class BindingExpressionToken extends Token {
    private BindingExpressionToken(Object... tokens) {
        super(tokens);
    }

    public static Token parse(TokenSource source) {
        // binding-expression = *ws identifier *ws operator *ws expression
        source.startScan();

        source.consumeWhitespace();
        Token identifier = IdentifierToken.parse(source);
        if(identifier == Token.NotFound)
            return source.error("Expected identifier in binding expression");

        source.consumeWhitespace();
        Token operator = OperatorToken.parse(source);
        if(operator == Token.NotFound)
            return source.error("Expected operator in binding expression");

        source.consumeWhitespace();
        Token expression = ExpressionToken.parse(source);
        if(expression == Token.NotFound)
            return source.error("Expected expression in binding expression");

        source.endScan();
        return new BindingExpressionToken(identifier, operator, expression);
    }
}
