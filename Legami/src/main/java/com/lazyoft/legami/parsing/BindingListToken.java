package com.lazyoft.legami.parsing;

import com.lazyoft.legami.binding.ITokenVisitor;

import java.util.ArrayList;
import java.util.List;

public class BindingListToken extends Token {
    private BindingListToken(Object... tokens) {
        super(tokens);
    }

    @Override
    public String toString() {
        return getTokens().toString().substring(1, getTokens().toString().length() - 1);
    }

    @Override
    public <TResult> TResult acceptVisitor(ITokenVisitor<TResult> visitor, TResult param) {
        return visitor.visit(this, param);
    }

    public static Token parse(TokenSource source) {
        List<Token> result = new ArrayList<Token>();
        Token item;

        // binding-list = *(binding-list-item)
        source.startScan();
        do {
            item = BindingListItemToken.parse(source);
            if(item != Token.NotFound)
                result.add(item);
        } while(item != Token.NotFound);

        if(result.isEmpty())
            return source.error("Expected Binding List");

        source.endScan();
        return new BindingListToken(result);
    }
}
