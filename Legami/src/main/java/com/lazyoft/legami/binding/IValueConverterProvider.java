package com.lazyoft.legami.binding;

import com.lazyoft.legami.accessors.IValueConverter;

public interface IValueConverterProvider {
    IValueConverter get(String name);
}
