package com.lazyoft.legami.parsing;

final class Terminals {
    public final static Token CloseParen = new TerminalToken(")");
    public final static Token CloseAngular = new TerminalToken("}");
    public final static Token Colon = new TerminalToken(":");
    public final static Token Dot = new TerminalToken(".");
    public final static Token Underscore = new TerminalToken("_");
    public final static Token Quote = new TerminalToken("/");
    public final static Token OpenAngular = new TerminalToken("{");
    public final static Token Whitespace = new TerminalToken(" ");
    public final static Token OpenParen = new TerminalToken("(");
    public final static Token RightBind = new TerminalToken("=:");
    public final static Token LeftBind = new TerminalToken(":=");
    public final static Token Hyphen = new TerminalToken("-");
    public final static Token Comma = new TerminalToken(",");
    public final static Token Equal = new TerminalToken("=");
    public final static Token Escape = new TerminalToken("\\");
    public final static Token Hash = new TerminalToken("#");
    public final static Token RightAssignment = new TerminalToken("-:");
    public final static Token LeftAssignment = new TerminalToken(":-");
    public final static Token FullBind = new TerminalToken("==");

    public final static class Letter extends TerminalToken { public Letter(char c) { super(String.valueOf(c)); } }
    public final static class Digit extends TerminalToken { public Digit(char c) { super(String.valueOf(c)); } }
    public final static Token Unknown(final char c) { return new TerminalToken(String.valueOf(c)); }
}



