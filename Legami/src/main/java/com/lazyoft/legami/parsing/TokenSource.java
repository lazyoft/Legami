package com.lazyoft.legami.parsing;

import java.util.Stack;

class TokenSource {
    private String sourceText;
    private int position;
    private Stack<Integer> offsetStack;
    private String errorMessage;

    public TokenSource(String sourceText) {
        this.sourceText = sourceText;
        offsetStack = new Stack<Integer>();
        errorMessage = "";
        position = 0;
    }

    public void startScan() {
        offsetStack.push(position);
    }

    public void endScan() {
        if(offsetStack.size() > 0)
            offsetStack.pop();
        errorMessage = "";
    }

    public Token error(String message) {
        errorMessage = "@" + position + ": " + message;
        if(offsetStack.size() > 0)
            position = offsetStack.pop();
        else
            position = 0;
        return Token.NotFound;
    }

    public void advance() {
        position++;
    }

    public void consumeWhitespace() {
        while(peek() == Terminals.Whitespace)
            advance();
    }

    public boolean atEnd() {
        return position >= sourceText.length();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Token next() {
        Token result = peek();
        if(result != Token.NotFound)
            advance();
        return result;
    }

    public Token peek() {
        return peek(0);
    }

    public Token peek(int delta) {
        int position = this.position + delta;
        if (position >= sourceText.length() || position < 0)
            return Token.NotFound;

        return charToToken(sourceText.charAt(position));
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
