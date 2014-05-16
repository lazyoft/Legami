package com.lazyoft.legami.tests;

import com.lazyoft.legami.accessors.Accessor;
import com.lazyoft.legami.accessors.ConvertingAccessorDecorator;
import com.lazyoft.legami.accessors.FieldAccessor;
import com.lazyoft.legami.accessors.IAccessor;
import com.lazyoft.legami.accessors.IValueConverter;

import junit.framework.TestCase;

public class ConvertingAccessorSpecifications extends TestCase {
    public ConvertingAccessorDecorator subject;
    public Object assignment;
    public boolean convertCalled;
    public boolean convertBackCalled;

    public IAccessor base = new IAccessor() {
        @Override
        public Object get() {
            return "value";
        }

        @Override
        public void set(Object value) {
            assignment = value;
        }
    };

    public IAccessor noValue = new IAccessor() {
        @Override
        public Object get() {
            return Accessor.NoValue;
        }

        @Override
        public void set(Object value) {

        }
    };

    public IValueConverter converter = new IValueConverter() {
        @Override
        public Object convert(Object value, Class targetType, Object param) {
            convertCalled = true;
            return "convert " + value + " with param " + param;
        }

        @Override
        public Object convertBack(Object value, Class targetType, Object param) {
            convertBackCalled = true;
            return "convertBack " + value + " with param " + param;
        }
    };

    public void testWhen_requesting_a_value_it_should_invoke_the_converter_on_the_base_accessor_value() {
        subject = new ConvertingAccessorDecorator(base, converter, Object.class, "42");

        assertEquals("convert value with param 42", subject.get());
        assertTrue(convertCalled);
        assertFalse(convertBackCalled);
    }

    public void testWhen_setting_a_value_it_should_invoke_the_converter_on_the_base_accessor_value() {
        subject = new ConvertingAccessorDecorator(base, converter, Object.class, "42");

        subject.set("backValue");
        assertEquals("convertBack backValue with param 42", assignment);
        assertFalse(convertCalled);
        assertTrue(convertBackCalled);
    }

    public void testWhen_setting_a_NoValue_it_should_not_invoke_the_converter() {
        subject = new ConvertingAccessorDecorator(base, converter, Object.class, "42");

        subject.set(Accessor.NoValue);
        assertEquals(Accessor.NoValue, assignment);
        assertFalse(convertCalled);
        assertFalse(convertBackCalled);
    }

    public void testWhen_getting_a_NoValue_from_the_base_accessor_it_should_not_invoke_the_converter() {
        subject = new ConvertingAccessorDecorator(noValue, converter, Object.class, "42");

        assertEquals(Accessor.NoValue, subject.get());
        assertFalse(convertCalled);
        assertFalse(convertBackCalled);
    }
}