package com.lazyoft.legami.parsing;

public class Operator {
    public static Token produce(Scanner scanner) {
        // operator = left-bind / right-bind / left-assignment / right-assignment / full-bind
        scanner.start();
        Token operator = Token.Empty;
        if(scanner.peek() == Terminals.Colon && scanner.peek(1) == Terminals.Equal)
            operator = Terminals.LeftBind;

        if(operator == Token.Empty && scanner.peek() == Terminals.Equal && scanner.peek(1) == Terminals.Colon)
            operator = Terminals.RightBind;

        if(operator == Token.Empty && scanner.peek() == Terminals.Colon && scanner.peek(1) == Terminals.Hyphen)
            operator = Terminals.LeftAssignment;

        if(operator == Token.Empty && scanner.peek() == Terminals.Hyphen && scanner.peek(1) == Terminals.Colon)
            operator = Terminals.RightAssignment;

        if(operator == Token.Empty && scanner.peek() == Terminals.Equal && scanner.peek(1) == Terminals.Equal)
            operator = Terminals.FullBind;

        scanner.advance();
        scanner.advance();
        scanner.commit();
        return operator;
    }
}
