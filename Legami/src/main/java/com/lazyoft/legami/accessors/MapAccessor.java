package com.lazyoft.legami.accessors;

import java.util.Map;

public class MapAccessor extends ChainedAccessorBase {
    @Override
    protected Object internalGet(String path, Object source) {
        if(!(source instanceof Map))
            return Accessor.cannotSolve;

        Map map = (Map)source;
        if(map.containsKey(path))
            return map.get(path);

        logger.warn("Key " + path + " cannot be found on map. Returning noValue");
        return Accessor.noValue;
    }

    @Override
    protected boolean internalSet(String path, Object source, Object value) {
        if(!(source instanceof Map))
            return false;

        ((Map)source).put(path, value);
        return true;
    }
}
