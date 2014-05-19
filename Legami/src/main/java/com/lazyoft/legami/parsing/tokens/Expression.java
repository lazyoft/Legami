package com.lazyoft.legami.parsing.tokens;

import com.lazyoft.legami.parsing.Token;

import java.util.List;

public class Expression extends Token {
    public Expression(Object ...tokens) {
        super(tokens);
    }

    public static Expression produce(List<Token> tokens) {
        // expression = constant-literal / conversion / path
        ConstantLiteral constantLiteral = ConstantLiteral.produce(tokens);
        if(constantLiteral != null)
            return new Expression(constantLiteral);

        Conversion conversion = Conversion.produce(tokens);
        if(conversion != null)
            return new Expression(conversion);

        Path path = Path.produce(tokens);
        if(path != null)
            return new Expression(path);

        return null;
    }
}
