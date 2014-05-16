package com.lazyoft.legami.accessors;

import java.lang.reflect.Field;

import rx.functions.Func2;

public class FieldAccessorResolver extends AccessorResolverBase {

    public FieldAccessorResolver(Func2<Object, String, IAccessor> factory) {
        super(factory);
    }

    @Override
    protected IAccessor internalResolve(Object source, String path, Func2<Object, String, IAccessor> factory) {
        IAccessor result = null;
        try {
            Field field = source.getClass().getField(path);
            if(field != null)
                result = factory.call(source, path);
        }
        catch(Exception e) {
            logger.debug("No field found for " + path);
        }
        return result;
    }
}

