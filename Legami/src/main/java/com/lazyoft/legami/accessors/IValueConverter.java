package com.lazyoft.legami.accessors;

import java.util.Locale;

public interface IValueConverter {
	Object convert(Object value, Class targetType, Object param);
	Object convertBack(Object value, Class targetType, Object param);
}

