package com.lazyoft.legami.parsing.tokens;

import com.lazyoft.legami.parsing.Scanner;

public class BindingListItem extends Token {
    private BindingListItem(Object ...tokens) {
        super(tokens);
    }

    public static Token produce(Scanner scanner) {
        // binding-list-item = *ws *(open-angular *ws binding-expression *ws close-angular)
        scanner.start();
        scanner.consumeWhitespace();

        if(!(scanner.next() == Terminals.OpenAngular))
            return scanner.error("Expected open angular in binding list item");

        scanner.consumeWhitespace();
        Token expression = BindingExpression.produce(scanner);
        if(expression == Token.Empty)
            return scanner.error("Expected binding expression in binding list item");

        scanner.consumeWhitespace();
        if(!(scanner.next() == Terminals.CloseAngular))
            return scanner.error("Expected close angular in binding list item");

        scanner.commit();
        return new BindingListItem(expression);
    }
}
