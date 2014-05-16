package com.lazyoft.legami.helpers;

import rx.functions.Func0;

public class Lazy<T> {
    private Func0<T> factory;
    private boolean valueCreated;
    private T value;

    public Lazy(Func0<T> factory) {
        this.factory = factory;
    }

    public boolean hasValue() {
        return valueCreated;
    }

    public T value() {
        synchronized (factory) {
            if (!valueCreated) {
                value = factory.call();
                valueCreated = true;
            }
        }
        return value;
    }
}
