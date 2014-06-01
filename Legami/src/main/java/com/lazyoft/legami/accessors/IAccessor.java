package com.lazyoft.legami.accessors;

public interface IAccessor {
    boolean set(String path, Object source, Object value);
    Object get(String path, Object source);
}

