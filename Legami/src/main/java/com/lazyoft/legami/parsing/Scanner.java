package com.lazyoft.legami.parsing;

import java.util.Stack;

class Scanner {
    private String source;
    private int offset;
    private Stack<Integer> offsetStack;
    private String error;

    public Scanner(String source) {
        this.source = source;
        offsetStack = new Stack<Integer>();
        error = "";
        offset = 0;
    }

    public void start() {
        offsetStack.push(offset);
    }

    public void reset() {
        if(offsetStack.size() > 0)
            offset = offsetStack.pop();
        else
            offset = 0;
    }

    public void commit() {
        if(offsetStack.size() > 0)
            offsetStack.pop();
        error = "";
    }

    public Token error(String message) {
        error = "@" + offset + ": " + message;
        reset();
        return Token.Empty;
    }

    public void advance() {
        offset++;
    }

    public void consumeWhitespace() {
        while(peek() == Terminals.Whitespace)
            advance();
    }

    public boolean atEnd() {
        return offset >= source.length();
    }

    public String getError() {
        return error;
    }

    public Token next() {
        Token result = peek();
        if(result != Token.Empty)
            advance();
        return result;
    }

    public Token peek() {
        return peek(0);
    }

    public Token peek(int delta) {
        int position = offset + delta;
        if (position >= source.length() || position < 0)
            return Token.Empty;

        char c = source.charAt(position);
        return charToToken(c);
    }

    private Token charToToken(char c) {
        if(Character.isDigit(c))
            return new Terminals.Digit(c);
        else if(Character.isLetter(c))
            return new Terminals.Letter(c);
        else {
            switch (c) {
                case '(':
                    return Terminals.OpenParen;
                case ')':
                    return Terminals.CloseParen;
                case '{':
                    return Terminals.OpenAngular;
                case '}':
                    return Terminals.CloseAngular;
                case '_':
                    return Terminals.Underscore;
                case '#':
                    return Terminals.Hash;
                case ' ':
                    return Terminals.Whitespace;
                case '/':
                    return Terminals.Quote;
                case ',':
                    return Terminals.Comma;
                case '.':
                    return Terminals.Dot;
                case '\\':
                    return Terminals.Escape;
                case ':':
                    return Terminals.Colon;
                case '-':
                    return Terminals.Hyphen;
                case '=':
                    return Terminals.Equal;
                default:
                    return Terminals.Unknown(c);
            }
        }
    }
}
