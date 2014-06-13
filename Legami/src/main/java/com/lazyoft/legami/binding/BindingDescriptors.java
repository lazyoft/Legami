package com.lazyoft.legami.binding;

import java.util.ArrayList;

public class BindingDescriptors extends ArrayList<BindingDescriptor> {
    public static final BindingDescriptors empty = new BindingDescriptors();

    public BindingDescriptor getLast() {
        if(size() > 0)
            return get(size() - 1);
        return null;
    }
}
