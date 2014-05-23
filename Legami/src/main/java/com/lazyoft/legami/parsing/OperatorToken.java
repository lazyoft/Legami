package com.lazyoft.legami.parsing;

public class OperatorToken {
    public static Token parse(TokenSource source) {
        // operator = left-bind / right-bind / left-assignment / right-assignment / full-bind
        source.startScan();
        Token operator = Token.NotFound;
        if(source.peek() == Terminals.Colon && source.peek(1) == Terminals.Equal)
            operator = Terminals.LeftBind;

        if(operator == Token.NotFound && source.peek() == Terminals.Equal && source.peek(1) == Terminals.Colon)
            operator = Terminals.RightBind;

        if(operator == Token.NotFound && source.peek() == Terminals.Colon && source.peek(1) == Terminals.Hyphen)
            operator = Terminals.LeftAssignment;

        if(operator == Token.NotFound && source.peek() == Terminals.Hyphen && source.peek(1) == Terminals.Colon)
            operator = Terminals.RightAssignment;

        if(operator == Token.NotFound && source.peek() == Terminals.Equal && source.peek(1) == Terminals.Equal)
            operator = Terminals.FullBind;

        source.advance();
        source.advance();
        source.endScan();
        return operator;
    }
}
