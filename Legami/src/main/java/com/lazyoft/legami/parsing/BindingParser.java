package com.lazyoft.legami.parsing;

public class BindingParser {
    public static ParseResult parse(String source) {
        TokenSource tokenSource = new TokenSource(source);
        Token result = BindingToken.parse(tokenSource);
        return result == Token.NotFound ? new ParseResult(tokenSource.getErrorMessage()) : new ParseResult(result);
    }

    public static String getTokenized(String source) {
        return parse(source).toString();
    }

    static void dumpToken(StringBuilder builder, Token token, int depth)
    {
        final String spaces = "________________________________________________________________________";

        builder.append(spaces.substring(0, depth * 4));
        builder.append(token.getClass().getName());
        builder.append(" (");
        builder.append(token.toString());
        builder.append(")\n");

        if(token.getTokens() != null)
            for(Token childToken: token.getTokens())
                dumpToken(builder, childToken, depth + 1);
    }
}

