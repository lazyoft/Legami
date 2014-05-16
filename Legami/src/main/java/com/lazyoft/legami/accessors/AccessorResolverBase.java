package com.lazyoft.legami.accessors;

import com.lazyoft.legami.logging.ILoggable;
import com.lazyoft.legami.logging.ILogger;
import com.lazyoft.legami.logging.NullLogger;

import rx.functions.Func2;

public abstract class AccessorResolverBase implements IChainedAccessorResolver, ILoggable {
    protected ILogger logger;
    private IChainedAccessorResolver next;
    private Func2<Object, String, IAccessor> factory;

    protected AccessorResolverBase(Func2<Object, String, IAccessor> factory) {
        this.factory = factory;
        logger = NullLogger.instance;
    }

    @Override
    public void setNext(IChainedAccessorResolver next) {
        this.next = next;
    }

    @Override
    public IAccessor resolve(Object source, String path) {
        logger.debug("Resolving " + path + " for " + source);

        IAccessor result = internalResolve(source, path, factory);
        if(result == null && next != null) {
            return next.resolve(source, path);
        }

        return result;
    }

    protected abstract IAccessor internalResolve(Object source, String path, Func2<Object, String, IAccessor> factory);

    public void setLogger(ILogger logger) {
        this.logger = logger;
    }
}

