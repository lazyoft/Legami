package com.lazyoft.legami.accessors;

public interface IChainedAccessorResolver extends IAccessorResolver {
    void setNext(IChainedAccessorResolver next);
}

