package com.lazyoft.legami.accessors;

import com.lazyoft.legami.logging.ILoggable;
import com.lazyoft.legami.logging.ILogger;
import com.lazyoft.legami.logging.NullLogger;

public class ConvertingAccessorDecorator implements IAccessor, ILoggable {
    private final IAccessor baseAccessor;
    private final IValueConverter valueConverter;
    private Class targetType;
    private Object param;
    private ILogger logger;

    public ConvertingAccessorDecorator(IAccessor baseAccessor, IValueConverter valueConverter, Class targetType, Object param) {
        logger = NullLogger.instance;

        this.baseAccessor = baseAccessor;
        this.valueConverter = valueConverter;
        this.targetType = targetType;
        this.param = param;
    }

    @Override
    public Object get() {
        try {
            Object result = baseAccessor.get();
            if(result != Accessor.NoValue)
                return valueConverter.convert(result, targetType, param);
            return result;
        }
        catch(Exception e) {
            logger.debug("Exception while converting to type " + targetType.getName() + ": " + e.toString());
            return Accessor.NoValue;
        }
    }

    @Override
    public void set(Object value) {
        try {
            baseAccessor.set(value == Accessor.NoValue
                    ? value
                    : valueConverter.convertBack(value, targetType, param));
        }
        catch(Exception e) {
            logger.debug("Exception while converting from type " + targetType.getName() + ": " + e.toString());
        }
    }

    @Override
    public void setLogger(ILogger logger) {
        this.logger = logger;
    }
}

