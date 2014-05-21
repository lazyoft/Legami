package com.lazyoft.legami.parsing.tokens;

import java.util.ArrayList;
import java.util.List;

public abstract class Token {
    private List<Token> constituents;

    public List<Token> getConstituents() {
        return constituents;
    }

    public Token(Object ...tokens) {
        constituents = new ArrayList<Token>();
        for(Object token : tokens) {
            if(token instanceof Token)
                constituents.add((Token)token);
            else if(token instanceof List)
                for(Object subToken : (List)token)
                    constituents.add((Token)subToken);
            else
                throw new RuntimeException("Error while constructing token");
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(Token constituent: constituents)
            builder.append(constituent.toString());
        return builder.toString();
    }

    public static final Token Empty = new Token() { public String toString() { return ""; } };
}
