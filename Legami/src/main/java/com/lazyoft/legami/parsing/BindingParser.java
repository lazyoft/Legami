package com.lazyoft.legami.parsing;

import com.lazyoft.legami.parsing.tokens.Binding;
import com.lazyoft.legami.parsing.tokens.Terminals;

import java.util.ArrayList;
import java.util.List;

public class BindingParser {
    public static Token parse(String s) {
        List<Token> symbols = new ArrayList<Token>();

        for(char c: s.toCharArray()) {
            if(Character.isDigit(c))
                symbols.add(new Terminals.Digit(c));
            else if(Character.isLetter(c))
                symbols.add(new Terminals.Letter(c));
            else {
                switch (c) {
                    case '(':
                        symbols.add(new Terminals.OpenParen());
                        break;
                    case ')':
                        symbols.add(new Terminals.CloseParen());
                        break;
                    case '{':
                        symbols.add(new Terminals.OpenAngular());
                        break;
                    case '}':
                        symbols.add(new Terminals.CloseAngular());
                        break;
                    case '_':
                        symbols.add(new Terminals.Underscore());
                        break;
                    case '#':
                        symbols.add(new Terminals.Hash());
                        break;
                    case ' ':
                        symbols.add(new Terminals.Whitespace());
                        break;
                    case '/':
                        symbols.add(new Terminals.Quote());
                        break;
                    case ',':
                        symbols.add(new Terminals.Comma());
                        break;
                    case '.':
                        symbols.add(new Terminals.Dot());
                        break;
                    case '\\':
                        symbols.add(new Terminals.Escape());
                        break;
                    case ':':
                        symbols.add(new Terminals.Colon());
                        break;
                    case '-':
                        symbols.add(new Terminals.Hyphen());
                        break;
                    case '=':
                        symbols.add(new Terminals.Equal());
                        break;
                    default:
                        symbols.add(Token.Nothing);
                }
            }
        }
        return Binding.produce(symbols);
    }

    public static String getTokenized(String text) {
        StringBuilder builder = new StringBuilder();
        dumpToken(builder, parse(text), 0);
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