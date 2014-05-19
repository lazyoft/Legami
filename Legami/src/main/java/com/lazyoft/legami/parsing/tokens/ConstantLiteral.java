package com.lazyoft.legami.parsing.tokens;

import com.lazyoft.legami.parsing.Token;

import java.util.List;

public class ConstantLiteral extends Token {
    public ConstantLiteral(Object ...tokens) {
        super(tokens);
    }

    public static ConstantLiteral produce(List<Token> tokens) {
        // constant-literal = hash identifier
        if(!tokens.isEmpty() && tokens.get(0) instanceof Terminals.Hash) {
            Identifier identifier = Identifier.produce(tokens.subList(1, tokens.size()));
            if (identifier != null) {
                tokens.remove(0);
                return new ConstantLiteral(new Terminals.Hash(), identifier);
            }
        }
        return null;
    }
}
