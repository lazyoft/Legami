package com.lazyoft.legami.parsing.tokens;

import com.lazyoft.legami.parsing.Token;

import java.util.ArrayList;
import java.util.List;

class Path extends Token {
    public Path(Object ...tokens) {
        super(tokens);
    }

    public static Path produce(List<Token> tokens) {
        List<Token> result = new ArrayList<Token>();

        // path = identifier *[dotted-identifier]
        Identifier identifier = Identifier.produce(tokens);
        if(identifier != null) {
            result.add(identifier);

            while (!tokens.isEmpty()) {
                if (tokens.get(0) instanceof Terminals.Dot) {
                    result.add(tokens.get(0));
                    tokens.remove(0);
                    Identifier other = Identifier.produce(tokens);
                    if (identifier != null)
                        result.add(other);
                    else
                        break;
                }
                else
                    break;
            }
        }

        if(!result.isEmpty())
            return new Path(result);
        return null;
    }
}
