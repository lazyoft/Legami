package com.lazyoft.legami.parsing;

public interface ITokenVisitorAcceptor {
    <TResult> TResult acceptVisitor(ITokenVisitor<TResult> visitor, TResult param);
}
