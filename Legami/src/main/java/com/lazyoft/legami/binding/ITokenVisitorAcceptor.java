package com.lazyoft.legami.binding;

public interface ITokenVisitorAcceptor {
    <TResult> TResult acceptVisitor(ITokenVisitor<TResult> visitor, TResult param);
}
