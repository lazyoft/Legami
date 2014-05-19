package com.lazyoft.legami.parsing.tokens;

import com.lazyoft.legami.parsing.TerminalToken;
import com.lazyoft.legami.parsing.Token;

public final class Terminals extends Token {
    public final static class CloseParen extends TerminalToken { public CloseParen() { super(")"); } }
    public final static class CloseAngular extends TerminalToken { public CloseAngular() { super("}"); } }
    public final static class Colon extends TerminalToken { public Colon() { super(":"); } }
    public final static class Dot extends TerminalToken { public Dot() { super("."); } }
    public final static class Underscore extends TerminalToken { public Underscore() { super("_"); } }
    public final static class Quote extends TerminalToken { public Quote() { super("/"); } }
    public final static class OpenAngular extends TerminalToken { public OpenAngular() { super("{"); } }
    public final static class Whitespace extends TerminalToken { public Whitespace() { super(" "); } }
    public final static class Letter extends TerminalToken { public Letter(char c) { super(String.valueOf(c)); } }
    public final static class OpenParen extends TerminalToken { public OpenParen() { super("("); } }
    public final static class RightBind extends TerminalToken { public RightBind() { super("=:"); } }
    public final static class LeftBind extends TerminalToken { public LeftBind() { super(":="); } }
    public final static class Hyphen extends TerminalToken { public Hyphen() { super("-"); } }
    public final static class Digit extends TerminalToken { public Digit(char c) { super(String.valueOf(c)); } }
    public final static class Comma extends TerminalToken { public Comma() { super(","); } }
    public final static class Equal extends TerminalToken { public Equal() { super("="); } }
    public final static class Escape extends TerminalToken { public Escape() { super("\\"); } }
    public final static class Hash extends TerminalToken { public Hash() { super("#"); } }
    public final static class RightAssignment extends TerminalToken { public RightAssignment() { super("-:"); } }
    public final static class LeftAssignment extends TerminalToken { public LeftAssignment() { super(":-"); } }
    public final static class FullBind extends TerminalToken { public FullBind() { super("=="); } }
}



