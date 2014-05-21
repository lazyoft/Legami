package com.lazyoft.legami.parsing;

public class BindingParser {
    public static Token parse(String source) {
        return BindingToken.parse(new TokenSource(source));
    }

    public static String getTokenized(String text) {
        StringBuilder builder = new StringBuilder();
        TokenSource source = new TokenSource(text);
        Token binding = BindingToken.parse(source);
        dumpToken(builder, binding, 0);
        builder.append("\n" + source.getErrorMessage());
        return builder.toString();
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

