package com.lazyoft.legami.accessors;

import java.util.Map;

public class MapAccessor implements IAccessor {
    private final Map<String, Object> source;
    private final String path;

    public MapAccessor(Map<String, Object> source, String path) {
        this.source = source;
        this.path = path;
    }

    @Override
    public Object get() {
        return source.containsKey(path) ? source.get(path) : Accessor.NoValue;
    }

    @Override
    public void set(Object value) {
        source.put(path, value);
    }
}
