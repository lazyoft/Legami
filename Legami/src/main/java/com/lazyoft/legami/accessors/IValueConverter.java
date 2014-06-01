package com.lazyoft.legami.accessors;

public interface IValueConverter {
	Object convert(Object value, Object param);
	Object convertBack(Object value, Object param);
}

