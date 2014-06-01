package com.lazyoft.legami.accessors;

import com.lazyoft.legami.helpers.IReflectionHelper;
import com.lazyoft.legami.logging.ILogger;

import java.lang.reflect.Field;

public class FieldAccessor extends ChainedAccessorBase {
    private IReflectionHelper helper;

    public FieldAccessor(IReflectionHelper helper) {
        this.helper = helper;
    }

    @Override
    public Object internalGet(String path, Object source) {
        try {
            Field field = helper.getField(source.getClass(), path);
            if(field == null)
                return Accessor.cannotSolve;

            return field.get(source);
        } catch (Exception e) {
            logger.warn("Field " + path + " cannot be accessed. Returning noValue");
            return Accessor.noValue;
        }
    }

    @Override
    public boolean internalSet(String path, Object source, Object value) {
        Field field = helper.getField(source.getClass(), path);
        if(field == null)
            return false;

        try {
            field.set(source, value);
        } catch (Exception e) {
            logger.warn("Field " + path + "cannot be set.");
        }
        return true;
    }
}

