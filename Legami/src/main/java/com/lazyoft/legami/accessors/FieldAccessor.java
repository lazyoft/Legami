package com.lazyoft.legami.accessors;

import com.lazyoft.legami.logging.ILoggable;
import com.lazyoft.legami.logging.ILogger;
import com.lazyoft.legami.logging.NullLogger;
import com.lazyoft.legami.helpers.Lazy;

import java.lang.reflect.Field;

import rx.functions.Func0;

public class FieldAccessor implements IAccessor, ILoggable {
    private final Object source;
    private final String path;
    private final Lazy<Field> field;

    private ILogger logger;

    public FieldAccessor(final Object source, final String path) {
        this.source = source;
        this.path = path;
        this.logger = NullLogger.instance;

        field = new Lazy<Field>(new Func0<Field>() {
            @Override
            public Field call() {
                try {
                    return source != null ? source.getClass().getDeclaredField(path) : null;
                } catch (Exception e) {
                    logger.error("Path " + path + "is inaccessible on object " + source);
                    return null;
                }
            }
        });
    }

    @Override
    public Object get() {
        try {
            return field.value().get(source);
        } catch (Exception e) {
            logger.warn("Field " + path + " cannot be accessed. Returning NoValue");
            return Accessor.NoValue;
        }
    }

    @Override
    public void set(Object value) {
        if(source == null)
            return;
        try {
            field.value().set(source, value);
        } catch (Exception e) {
            logger.warn("Field " + path + "cannot be set.");
        }
    }

    @Override
    public void setLogger(ILogger logger) {
        this.logger = logger;
    }
}
