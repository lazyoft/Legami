package com.lazyoft.legami.accessors;

import com.lazyoft.legami.logging.ILoggable;
import com.lazyoft.legami.logging.ILogger;
import com.lazyoft.legami.logging.NullLogger;

public abstract class ChainedAccessorBase implements IChainedAccessor, ILoggable {
    private IChainedAccessor next;
    protected ILogger logger;
    protected String path;

    public ChainedAccessorBase() {
        logger = NullLogger.instance;
    }

    @Override
    public Object get(String path, Object source) {
        Object result = internalGet(path, source);
        if(result != Accessor.cannotSolve)
            return result;

        if(next != null)
            return next.get(path, source);
        else
            return Accessor.noValue;
    }

    @Override
    public boolean set(String path, Object source, Object value) {
        boolean result = internalSet(path, source, value);
        if(result)
            return result;

        return next == null ? false : next.set(path, source, value);
    }

    @Override
    public void setNext(IChainedAccessor next) {
        this.next = next;
    }

    @Override
    public void setLogger(ILogger logger) {
        this.logger = logger;
    }

    protected abstract Object internalGet(String path, Object source);
    protected abstract boolean internalSet(String path, Object source, Object value);
}
