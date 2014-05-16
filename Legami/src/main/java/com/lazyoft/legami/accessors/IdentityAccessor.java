package com.lazyoft.legami.accessors;

import com.lazyoft.legami.logging.ILoggable;
import com.lazyoft.legami.logging.ILogger;
import com.lazyoft.legami.logging.NullLogger;

public class IdentityAccessor implements IAccessor, ILoggable {
    private ILogger logger;
    private Object source;

    public IdentityAccessor(Object source) {
        logger = NullLogger.instance;
        this.source = source;
    }

    @Override
    public Object get() {
        return source;
    }

    @Override
    public void set(Object value) {
        logger.warn("Attempted to set identity. Ignoring.");
    }

    @Override
    public void setLogger(ILogger logger) {
        this.logger = logger;
    }
}
