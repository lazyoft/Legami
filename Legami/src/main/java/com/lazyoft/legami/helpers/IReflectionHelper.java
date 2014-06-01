package com.lazyoft.legami.helpers;

import com.lazyoft.legami.accessors.FieldAccessor;
import com.lazyoft.legami.accessors.IAccessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public interface IReflectionHelper {
    Method getGetter(Class objectClass, String path);
    Method getSetter(Class objectClass, String path);
    Field getField(Class objectClass, String path);
}
