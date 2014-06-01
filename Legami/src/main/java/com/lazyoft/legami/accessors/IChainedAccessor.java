package com.lazyoft.legami.accessors;

public interface IChainedAccessor extends IAccessor {
    void setNext(IChainedAccessor accessor);
}
