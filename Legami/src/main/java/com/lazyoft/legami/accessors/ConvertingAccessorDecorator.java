package com.lazyoft.legami.accessors;

import com.lazyoft.legami.logging.ILoggable;
import com.lazyoft.legami.logging.ILogger;
import com.lazyoft.legami.logging.NullLogger;

public class ConvertingAccessorDecorator implements IAccessor, ILoggable {
    private final IAccessor baseAccessor;
    private final IValueConverter valueConverter;
    private Object param;
    private ILogger logger;

    public ConvertingAccessorDecorator(IAccessor baseAccessor, IValueConverter valueConverter, Object param) {
        logger = NullLogger.instance;

        this.baseAccessor = baseAccessor;
        this.valueConverter = valueConverter;
        this.param = param;
    }

    @Override
    public Object get(String path, Object source) {
        try {
            Object result = baseAccessor.get(path, source);
            if(result != Accessor.noValue)
                return valueConverter.convert(result, param);
            return result;
        }
        catch(Exception e) {
            logger.debug("Exception while converting value at path " + path + ": " + e.toString());
            return Accessor.noValue;
        }
    }

    @Override
    public boolean set(String path, Object source, Object value) {
        try {
            return baseAccessor.set(path, source, value == Accessor.noValue
                    ? value
                    : valueConverter.convertBack(value, param));
        }
        catch(Exception e) {
            logger.debug("Exception while converting back value at path " + path + ": " + e.toString());
            return true;
        }
    }

    @Override
    public void setLogger(ILogger logger) {
        this.logger = logger;
    }
}

