package com.lazyoft.legami.helpers;

import com.lazyoft.legami.accessors.IAccessor;

public interface IPathDescriptor {
    Class getType();
    IAccessor getAccessor();
}
