package com.lazyoft.legami.parsing;

public class BindingParser {
    public static Token parse(String source) {
        return Binding.produce(new Scanner(source));
    }

    public static String getTokenized(String text) {
        StringBuilder builder = new StringBuilder();
        Scanner scanner = new Scanner(text);
        Token binding = Binding.produce(scanner);
        dumpToken(builder, binding, 0);
        builder.append("\n" + scanner.getError());
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

        if(token.getConstituents() != null)
            for(Token childToken: token.getConstituents())
                dumpToken(builder, childToken, depth + 1);
    }
}

