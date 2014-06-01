package com.lazyoft.legami.accessors;

public class EmptyAccessor extends ChainedAccessorBase {
    private EmptyAccessor() {}

    @Override
    protected Object internalGet(String path, Object source) {
        return Accessor.noValue;
    }

    @Override
    protected boolean internalSet(String path, Object source, Object value) {
        return true;
    }

    public static final IAccessor instance = new EmptyAccessor();
}
