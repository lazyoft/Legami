package com.lazyoft.legami.accessors;

public interface IAccessorResolver {
    IAccessor resolve(Object source, String path);
}

