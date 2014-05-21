package com.lazyoft.legami.parsing;

public class BindingListItemToken extends Token {
    private BindingListItemToken(Object... tokens) {
        super(tokens);
    }

    public static Token produce(Scanner scanner) {
        // binding-list-item = *ws *(open-angular *ws binding-expression *ws close-angular)
        scanner.start();
        scanner.consumeWhitespace();

        if(!(scanner.next() == Terminals.OpenAngular))
            return scanner.error("Expected open angular in binding list item");

        scanner.consumeWhitespace();
        Token expression = BindingExpressionToken.produce(scanner);
        if(expression == Token.Empty)
            return scanner.error("Expected binding expression in binding list item");

        scanner.consumeWhitespace();
        if(!(scanner.next() == Terminals.CloseAngular))
            return scanner.error("Expected close angular in binding list item");

        scanner.commit();
        return new BindingListItemToken(expression);
    }
}
