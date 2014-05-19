package com.lazyoft.legami.parsing;

import com.lazyoft.legami.parsing.tokens.Terminals;

import java.util.List;

public class TokenUtils {
    public static TerminalToken produceOperator(List<Token> tokens) {
        TerminalToken result = null;

        if (hasTokens(tokens, Terminals.Colon.class, Terminals.Equal.class))
            result = new Terminals.LeftBind();
        else if (hasTokens(tokens, Terminals.Equal.class, Terminals.Colon.class))
            result = new Terminals.RightBind();
        else if (hasTokens(tokens, Terminals.Colon.class, Terminals.Hyphen.class))
            result = new Terminals.LeftAssignment();
        else if (hasTokens(tokens, Terminals.Hyphen.class, Terminals.Colon.class))
            result = new Terminals.RightAssignment();
        else if (hasTokens(tokens, Terminals.Equal.class, Terminals.Equal.class))
            result = new Terminals.FullBind();

        if(result != null) {
            tokens.remove(0);
            tokens.remove(0);
        }
        return result;
    }

    static boolean hasTokens(List<Token> tokens, Class ...classes) {
        if(classes.length > tokens.size())
            return false;

        for(int i = 0; i < classes.length; i++) {
            if(!classes[i].isAssignableFrom(tokens.get(i).getClass()))
                return false;
        }
        return true;
    }

    public static void consumeWhitespace(List<Token> tokens) {
        while(!tokens.isEmpty() && tokens.get(0) instanceof Terminals.Whitespace)
            tokens.remove(0);
    }
}
