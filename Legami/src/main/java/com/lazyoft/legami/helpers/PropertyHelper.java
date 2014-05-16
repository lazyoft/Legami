package com.lazyoft.legami.helpers;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PropertyHelper implements IPropertyHelper {
    private static final String GET = "get";
    private static final String SET = "set";
    private static final String IS = "is";
    private static final Map<Class, Map<String, Method>> memoizedGetters = new HashMap<Class, Map<String, Method>>();
    private static final Map<Class, Map<String, Method>> memoizedSetters = new HashMap<Class, Map<String, Method>>();

    public Method getGetter(Class objectClass, String path) {
        if(memoizedGetters.containsKey(objectClass) && memoizedGetters.get(objectClass).containsKey(path))
            return memoizedGetters.get(objectClass).get(path);

        Method result = null;
        Map<String, Method> getters = new HashMap<String, Method>();
        memoizedGetters.put(objectClass, getters);

        for(Method method : objectClass.getMethods())
            if(isGetter(method)) {
                String name = method.getName();
                if(name.equals(GET + path) || name.equals(IS + path))
                    result = method;
                getters.put(name.startsWith(GET) ? name.substring(GET.length()) : name.substring(IS.length()), method);
            }
        return result;
    }

    public Method getSetter(Class objectClass, String path) {
        if(memoizedSetters.containsKey(objectClass) && memoizedSetters.get(objectClass).containsKey(path))
            return memoizedSetters.get(objectClass).get(path);

        Method result = null;
        Map<String, Method> getters = new HashMap<String, Method>();
        memoizedSetters.put(objectClass, getters);

        for(Method method : objectClass.getMethods())
            if(isSetter(method)) {
                String name = method.getName();
                if(name.equals(SET + path))
                    result = method;
                getters.put(name.substring(SET.length()), method);
            }
        return result;
    }

    private static boolean isGetter(Method method){
        return (method.getName().startsWith(GET) || method.getName().startsWith(IS))
                && method.getParameterTypes().length == 0
                && !void.class.equals(method.getReturnType());
    }

    private static boolean isSetter(Method method){
        return method.getName().startsWith(SET)
                && method.getParameterTypes().length == 1;
    }
}
