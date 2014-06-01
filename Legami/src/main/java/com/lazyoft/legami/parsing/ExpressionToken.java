package com.lazyoft.legami.parsing;

public class ExpressionToken extends Token {
    private ExpressionToken(Object... tokens) {
        super(tokens);
    }

    @Override
    public <TResult> TResult acceptVisitor(ITokenVisitor<TResult> visitor, TResult param) {
        return visitor.visit(this, param);
    }

    public static Token parse(TokenSource source) {
        // expression = constant-literal / conversion / path
        source.startScan();

        Token expression = ConversionToken.parse(source);
        if(expression == Token.NotFound) {
            expression = ConstantLiteralToken.parse(source);
            if(expression == Token.NotFound) {
                expression = PathToken.parse(source);
            }
        }
        if(expression == Token.NotFound)
            return source.error("Expected constant expression, conversion expression or path");

        source.endScan();
        return new ExpressionToken(expression);
    }
}

