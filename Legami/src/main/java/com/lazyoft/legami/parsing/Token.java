package com.lazyoft.legami.parsing;

import com.lazyoft.legami.binding.ITokenVisitor;
import com.lazyoft.legami.binding.ITokenVisitorAcceptor;

import java.util.ArrayList;
import java.util.List;

public abstract class Token implements ITokenVisitorAcceptor {
    private List<Token> tokens;

    public List<Token> getTokens() {
        return tokens;
    }

    public Token(Object ...tokens) {
        this.tokens = new ArrayList<Token>();
        for(Object token : tokens) {
            if(token instanceof Token)
                this.tokens.add((Token) token);
            else if(token instanceof List)
                for(Object subToken : (List)token)
                    this.tokens.add((Token) subToken);
            else
                throw new RuntimeException("Error while constructing token");
        }
    }

    public <TResult> TResult acceptVisitor(ITokenVisitor<TResult> visitor, TResult param) {
        return visitor.visit(this, param);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(Token token: tokens)
            builder.append(token.toString());
        return builder.toString();
    }

    public static final Token NotFound = new Token() { public String toString() { return ""; } };
}
