package com.lazyoft.legami.helpers;

import java.lang.reflect.Method;

public interface IPropertyHelper {
    Method getGetter(Class objectClass, String path);
    Method getSetter(Class objectClass, String path);
}

