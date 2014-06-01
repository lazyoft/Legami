package com.lazyoft.legami.accessors;

import com.lazyoft.legami.helpers.IReflectionHelper;

import java.lang.reflect.Method;

public class PropertyAccessor extends ChainedAccessorBase {
    private IReflectionHelper helper;

    public PropertyAccessor(final IReflectionHelper helper) {
        this.helper = helper;
    }

    @Override
    public Object internalGet(String path, Object source) {
        try {
            Method getter = helper.getGetter(source.getClass(), path);
            if(getter == null)
                return Accessor.cannotSolve;

            return getter.invoke(source);
        } catch (Exception e) {
            logger.warn("Property " + path + " cannot be accessed. Returning noValue");
            return Accessor.noValue;
        }
    }

    @Override
    public boolean internalSet(String path, Object source, Object value) {
        Method setter = helper.getSetter(source.getClass(), path);
        if(setter == null)
            return false;

        try {
            setter.invoke(source, value);
        } catch (Exception e) {
            logger.warn("Property " + path + "cannot be set.");
        }
        return true;
    }
}
