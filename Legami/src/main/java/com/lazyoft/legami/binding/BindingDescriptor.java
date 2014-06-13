package com.lazyoft.legami.binding;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class BindingDescriptor {
    public String[] sourceParts;
    public String[] targetParts;
    public IProperty target;
    public IProperty source;
    public Object parameter;
    public boolean converted;

    public BindingDescriptor() {
        sourceParts = new String[0];
        targetParts = new String[0];
    }
}

class BindingCache {
    Map<Integer, BindingDescriptors> descriptorsMap;

    public void addDescriptors(int viewId, BindingDescriptors descriptors) {
        descriptorsMap.put(viewId, descriptors);
    }

    public BindingDescriptors descriptorsFor(int viewId) {
        if(descriptorsMap.containsKey(viewId))
            return descriptorsMap.get(viewId);
        return BindingDescriptors.empty;
    }
}

class BindingHelper {
    Map<Object, Object> cache = new WeakHashMap<Object, Object>();

    public void changed(Object source, String property) {
        if(cache.containsKey(source)) {
            // get value for property
            //
            // save it in cache
        }
    }
}

class DependencyCalculator {


    // Source - Target: key
    // Something: value

}