package com.lazyoft.legami.binding;

public interface IProperty {
    Object get(Object source);
    void set(Object source, Object value);
}
