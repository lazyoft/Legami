package com.lazyoft.legami.parsing;

class TerminalToken extends Token {
    String content;

    public TerminalToken(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }
}
