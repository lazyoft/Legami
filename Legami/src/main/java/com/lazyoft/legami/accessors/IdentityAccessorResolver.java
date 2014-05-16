package com.lazyoft.legami.accessors;

import rx.functions.Func2;

public class IdentityAccessorResolver extends AccessorResolverBase {

    public IdentityAccessorResolver(Func2<Object, String, IAccessor> factory) {
        super(factory);
    }

    @Override
    protected IAccessor internalResolve(Object source, String path, Func2<Object, String, IAccessor> factory) {
        IAccessor result = null;
        if(path.equals("this"))
            result = factory.call(source, path);
        return result;
    }
}
