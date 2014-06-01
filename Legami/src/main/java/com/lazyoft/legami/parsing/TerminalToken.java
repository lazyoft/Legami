package com.lazyoft.legami.parsing;

import com.lazyoft.legami.binding.ITokenVisitor;

public class TerminalToken extends Token {
    String content;

    public TerminalToken(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }

    @Override
    public <TResult> TResult acceptVisitor(ITokenVisitor<TResult> visitor, TResult param) {
        return visitor.visit(this, param);
    }
}
