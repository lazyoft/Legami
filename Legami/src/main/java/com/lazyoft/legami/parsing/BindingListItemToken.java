package com.lazyoft.legami.parsing;

public class BindingListItemToken extends Token {
    private BindingListItemToken(Object... tokens) {
        super(tokens);
    }

    @Override
    public <TResult> TResult acceptVisitor(ITokenVisitor<TResult> visitor, TResult param) {
        return visitor.visit(this, param);
    }

    public static Token parse(TokenSource source) {
        // binding-list-item = *ws *(open-angular *ws binding-expression *ws close-angular)
        source.startScan();
        source.consumeWhitespace();

        if(source.next() != Terminals.OpenAngular)
            return source.error("Expected open angular in binding list item");

        source.consumeWhitespace();
        Token expression = BindingExpressionToken.parse(source);
        if(expression == Token.NotFound)
            return source.error("Expected binding expression in binding list item");

        source.consumeWhitespace();
        if(source.next() != Terminals.CloseAngular)
            return source.error("Expected close angular in binding list item");

        source.endScan();
        return new BindingListItemToken(expression);
    }
}
