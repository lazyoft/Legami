package com.lazyoft.legami.accessors;

import com.lazyoft.legami.logging.ILoggable;
import com.lazyoft.legami.logging.ILogger;
import com.lazyoft.legami.logging.NullLogger;
import com.lazyoft.legami.helpers.IPropertyHelper;
import com.lazyoft.legami.helpers.Lazy;

import java.lang.reflect.Method;

import rx.functions.Func0;

public class PropertyAccessor implements IAccessor, ILoggable {
    private final Object source;
    private final String path;
    private final Lazy<Method> getterMethod;
    private final Lazy<Method> setterMethod;
    private ILogger logger;

    public PropertyAccessor(final Object source, final String path, final IPropertyHelper helper) {
        this.source = source;
        this.path = path;
        this.logger = NullLogger.instance;

        getterMethod = new Lazy<Method>(new Func0<Method>() {
            @Override
            public Method call() {
                return helper.getGetter(source.getClass(), path);
            }
        });
        setterMethod = new Lazy<Method>(new Func0<Method>() {
            @Override
            public Method call() {
                return helper.getSetter(source.getClass(), path);
            }
        });
    }

    @Override
    public Object get() {
        try {
            return getterMethod.value().invoke(source);
        } catch (Exception e) {
            logger.warn("Property " + path + " cannot be accessed. Returning NoValue");
            return Accessor.NoValue;
        }
    }

    @Override
    public void set(Object value) {
        try {
            setterMethod.value().invoke(source, value);
        } catch (Exception e) {
            logger.warn("Property " + path + "cannot be set.");
        }
    }

    @Override
    public void setLogger(ILogger logger) {
        this.logger = logger;
    }
}
