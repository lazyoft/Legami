package com.lazyoft.legami.parsing.tokens;

import com.lazyoft.legami.parsing.Token;
import com.lazyoft.legami.parsing.TokenUtils;

import java.util.ArrayList;
import java.util.List;

public class BindingExpression extends Token {
    public BindingExpression(Object ...tokens) {
        super(tokens);
    }

    public static BindingExpression produce(List<Token> tokens) {
        List<Token> result = new ArrayList<Token>();

        // binding-expression = *ws identifier *ws (left-bind / right-bind / left-assignment / right-assignment / full-bind) *ws expression
        TokenUtils.consumeWhitespace(tokens);

        Identifier identifier = Identifier.produce(tokens);
        if(identifier != null) {
            result.add(identifier);
            TokenUtils.consumeWhitespace(tokens);
            Token operator = TokenUtils.produceOperator(tokens);

            if(operator != null) {
                result.add(operator);
                TokenUtils.consumeWhitespace(tokens);
                Expression expression = Expression.produce(tokens);

                if(expression != null) {
                    result.add(expression);
                }
            }
        }

        if(!result.isEmpty())
            return new BindingExpression(result);
        return null;
    }
}
