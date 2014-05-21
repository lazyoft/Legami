package com.lazyoft.legami.parsing;

import java.util.ArrayList;
import java.util.List;

public class BindingListToken extends Token {
    private BindingListToken(Object... tokens) {
        super(tokens);
    }

    public static Token produce(Scanner scanner) {
        List<Token> result = new ArrayList<Token>();
        Token item;

        // binding-list = *(binding-list-item)
        scanner.start();
        do {
            item = BindingListItemToken.produce(scanner);
            if(item != Token.Empty)
                result.add(item);
        } while(item != Token.Empty);

        if(result.isEmpty())
            return scanner.error("Expected Binding List");

        scanner.commit();
        return new BindingListToken(result);
    }
}
