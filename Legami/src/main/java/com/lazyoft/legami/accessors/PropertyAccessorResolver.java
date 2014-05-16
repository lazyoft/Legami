package com.lazyoft.legami.accessors;

import com.lazyoft.legami.helpers.IPropertyHelper;

import java.lang.reflect.Method;
import java.util.Map;

import rx.functions.Func2;

public class PropertyAccessorResolver extends AccessorResolverBase {
    private final IPropertyHelper propertyHelper;

    public PropertyAccessorResolver(Func2<Object, String, IAccessor> factory, IPropertyHelper propertyHelper) {
        super(factory);
        this.propertyHelper = propertyHelper;
    }

    @Override
    protected IAccessor internalResolve(Object source, String path, Func2<Object, String, IAccessor> factory) {
        IAccessor result = null;
        try {
            Method method = propertyHelper.getGetter(source.getClass(), path);
            if(method != null)
                result = factory.call(source, path);
        }
        catch(Exception e) {
            logger.debug("No property found for " + path);
        }
        return result;
    }

}
