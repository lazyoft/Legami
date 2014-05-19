package com.lazyoft.legami.parsing.tokens;

import com.lazyoft.legami.parsing.Token;

import java.util.ArrayList;
import java.util.List;

public class BindingList extends Token {
    public BindingList(Object ...tokens) {
        super(tokens);
    }

    public static BindingList produce(List<Token> tokens) {
        List<Token> result = new ArrayList<Token>();
        BindingListItem item;

        // binding-list = *(binding-list-item)
        do {
            item = BindingListItem.produce(tokens);
            if (item != null)
                result.add(item);
        } while(item != null);

        if(!result.isEmpty())
            return new BindingList(result);
        return null;
    }
}
