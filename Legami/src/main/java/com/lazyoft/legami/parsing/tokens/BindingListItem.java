package com.lazyoft.legami.parsing.tokens;

import com.lazyoft.legami.parsing.Token;
import com.lazyoft.legami.parsing.TokenUtils;

import java.util.ArrayList;
import java.util.List;

public class BindingListItem extends Token {
    public BindingListItem(Object ...tokens) {
        super(tokens);
    }

    public static BindingListItem produce(List<Token> tokens) {
        List<Token> result = new ArrayList<Token>();

        // binding-list-item = *ws *(open-angular *ws binding-expression *ws close-angular)
        TokenUtils.consumeWhitespace(tokens);
        if(!tokens.isEmpty() && tokens.get(0) instanceof Terminals.OpenAngular) {
            result.add(tokens.get(0));
            tokens.remove(0);
            TokenUtils.consumeWhitespace(tokens);
            BindingExpression expression = BindingExpression.produce(tokens);
            if (expression != null) {
                result.add(expression);
                TokenUtils.consumeWhitespace(tokens);
                if (tokens.get(0) instanceof Terminals.CloseAngular) {
                    result.add(tokens.get(0));
                    tokens.remove(0);
                    return new BindingListItem(result);
                }
            }
        }

        return null;
    }
}
