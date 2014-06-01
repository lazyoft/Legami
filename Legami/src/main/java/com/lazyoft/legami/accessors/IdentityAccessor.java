package com.lazyoft.legami.accessors;

public class IdentityAccessor extends ChainedAccessorBase {
    @Override
    protected Object internalGet(String path, Object source) {
        if(path.equals("this"))
            return source;
        return Accessor.cannotSolve;
    }

    @Override
    protected boolean internalSet(String path, Object source, Object value) {
        if(path.equals("this")) {
            logger.warn("Cannot set 'this' property");
            return true;
        }
        return false;
    }
}
