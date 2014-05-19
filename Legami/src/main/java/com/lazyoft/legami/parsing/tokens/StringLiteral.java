package com.lazyoft.legami.parsing.tokens;

import com.lazyoft.legami.parsing.Token;

import java.util.ArrayList;
import java.util.List;

public class StringLiteral extends Token {
    public StringLiteral(Object ...tokens) {
        super(tokens);
    }

    public static StringLiteral produce(List<Token> tokens) {
        List<Token> result = new ArrayList<Token>();
        int position = 0;

        // string-literal = quote *char-in-string quote
        if(tokens.get(0) instanceof Terminals.Quote) {
            result.add(tokens.get(0));
            for(position = 1; position < tokens.size(); position++) {
                Token token = tokens.get(position);
                if(!(token instanceof Terminals.Quote)) {
                    result.add(token);
                }
                else {
                    result.add(token);
                    if(!(tokens.get(position - 1) instanceof Terminals.Escape))
                        break;
                }
            }
        }

        if(!result.isEmpty()) {
            tokens.subList(0, position + 1).clear();
            return new StringLiteral(result);
        }
        return null;
    }
}
