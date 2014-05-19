package com.lazyoft.legami.parsing.tokens;

import com.lazyoft.legami.parsing.Token;

import java.util.ArrayList;
import java.util.List;

public class Identifier extends Token {
    public Identifier(Object ...tokens) {
        super(tokens);
    }

    static Identifier peek(List<Token> tokens) {
        List<Token> result = new ArrayList<Token>();
        int position = 0;

        // identifier = letter / underscore *[letter / underscore / digit]
        if(tokens.get(0) instanceof Terminals.Letter || tokens.get(0) instanceof Terminals.Underscore) {
            result.add(tokens.get(0));
            for(position = 1; position < tokens.size(); position++) {
                Token token = tokens.get(position);
                if(token instanceof Terminals.Letter || token instanceof Terminals.Underscore || token instanceof Terminals.Digit)
                    result.add(token);
                else
                    break;
            }
        }
        if(!result.isEmpty())
            return new Identifier(result);
        return null;
    }

    public static int sizeOf(Identifier identifier) {
        return identifier != null ? identifier.toString().length() : 0;
    }

    public static Identifier produce(List<Token> tokens) {
        Identifier identifier = peek(tokens);
        consume(tokens, identifier);
        return identifier;
    }

    public static void consume(List<Token> tokens, Identifier identifier) {
        tokens.subList(0, sizeOf(identifier)).clear();
    }
}
