package com.lazyoft.legami.accessors;

import java.util.Map;

import rx.functions.Func2;

public class MapAccessorResolver extends AccessorResolverBase {

    public MapAccessorResolver(Func2<Object, String, IAccessor> factory) {
        super(factory);
    }

    @Override
    protected IAccessor internalResolve(Object source, String path, Func2<Object, String, IAccessor> factory) {
        IAccessor result = null;
        if(source instanceof Map)
            result = factory.call(source, path);
        return result;
    }
}