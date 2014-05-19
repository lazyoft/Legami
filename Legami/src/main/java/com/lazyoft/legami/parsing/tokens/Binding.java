package com.lazyoft.legami.parsing.tokens;

import com.lazyoft.legami.parsing.Token;

import java.util.List;

public class Binding extends Token {
    public Binding(Object ...tokens) {
        super(tokens);
    }

    public static Binding produce(List<Token> tokens) {
        // binding = binding-expression / binding-list
        BindingExpression expression = BindingExpression.produce(tokens);
        if(expression != null)
            return new Binding(expression);
        else {
            BindingList list = BindingList.produce(tokens);
            if(list != null)
                return new Binding(list);
        }
        return null;
    }
}
