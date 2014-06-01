package com.lazyoft.legami.tests;

import com.lazyoft.legami.accessors.Accessor;
import com.lazyoft.legami.accessors.ConvertingAccessorDecorator;
import com.lazyoft.legami.accessors.EmptyAccessor;
import com.lazyoft.legami.accessors.FieldAccessor;
import com.lazyoft.legami.accessors.IAccessor;
import com.lazyoft.legami.accessors.IValueConverter;
import com.lazyoft.legami.accessors.IdentityAccessor;
import com.lazyoft.legami.accessors.MapAccessor;
import com.lazyoft.legami.accessors.PropertyAccessor;
import com.lazyoft.legami.helpers.IReflectionHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.junit.experimental.runners.Enclosed.class)
public class AccessorsSpecifications {
    public static class when_requesting_a_field {
        FieldAccessor accessor;
        IReflectionHelper helper;
        TestClass testClass;

        @Before
        public void setUp() throws NoSuchFieldException {
            testClass = new TestClass();
            testClass.text = "some text";
            testClass.number = 42;
            helper = mock(IReflectionHelper.class);

            when(helper.getField(TestClass.class, "text")).thenReturn(TestClass.class.getField("text"));
            when(helper.getField(TestClass.class, "number")).thenReturn(TestClass.class.getField("number"));
            when(helper.getField(TestClass.class, "nonexistent")).thenReturn(null);
            when(helper.getField(TestClass.class, "throwing")).thenThrow(new RuntimeException());
            accessor = new FieldAccessor(helper);
        }

        @Test
        public void should_return_field_values() throws IllegalAccessException {
            assertThat(accessor.get("text", testClass)).isEqualTo("some text");
            verify(helper, times(1)).getField(TestClass.class, "text");
        }

        @Test
        public void should_set_field_values() {
            assertThat(accessor.set("text", testClass, "new value")).isTrue();
            verify(helper, times(1)).getField(TestClass.class, "text");
            assertThat(testClass.text).isEqualTo("new value");
        }

        @Test
        public void should_return_noValue_if_the_field_is_not_present() {
            assertThat(accessor.get("nonexistent", testClass)).isSameAs(Accessor.noValue);
            verify(helper, times(1)).getField(TestClass.class, "nonexistent");
        }

        @Test
        public void should_return_noValue_if_an_exception_is_thrown_when_retrieving_the_value() {
            assertThat(accessor.get("throwing", testClass)).isEqualTo(Accessor.noValue);
            verify(helper, times(1)).getField(TestClass.class, "throwing");
        }

        @Test
        public void should_not_set_the_field_if_an_exception_is_thrown_when_setting_the_value() {
            assertThat(accessor.set("number", testClass, "invalid value")).isTrue();
            verify(helper, times(1)).getField(TestClass.class, "number");
            assertThat(testClass.number).isEqualTo(42);
        }
    }

    public static class when_requesting_a_property {
        PropertyAccessor accessor;
        IReflectionHelper helper;
        TestClass testClass;

        @Before
        public void setUp() throws NoSuchMethodException {
            testClass = new TestClass();
            testClass.setEncapsulated("some text");
            testClass.setAnswer(42);
            helper = mock(IReflectionHelper.class);

            when(helper.getGetter(TestClass.class, "Encapsulated")).thenReturn(TestClass.class.getMethod("getEncapsulated"));
            when(helper.getSetter(TestClass.class, "Encapsulated")).thenReturn(TestClass.class.getMethod("setEncapsulated", String.class));
            when(helper.getGetter(TestClass.class, "Answer")).thenReturn(TestClass.class.getMethod("getAnswer"));
            when(helper.getSetter(TestClass.class, "Answer")).thenReturn(TestClass.class.getMethod("setAnswer", int.class));
            when(helper.getGetter(TestClass.class, "Nonexistent")).thenReturn(null);
            when(helper.getSetter(TestClass.class, "Nonexistent")).thenReturn(null);
            when(helper.getGetter(TestClass.class, "Throwing")).thenThrow(new RuntimeException());
            when(helper.getSetter(TestClass.class, "Throwing")).thenThrow(new RuntimeException());

            accessor = new PropertyAccessor(helper);
        }

        @Test
        public void should_return_property_values() throws IllegalAccessException {
            assertThat(accessor.get("Encapsulated", testClass)).isEqualTo("some text");
            verify(helper, times(1)).getGetter(TestClass.class, "Encapsulated");
        }

        @Test
        public void should_set_field_values() {
            assertThat(accessor.set("Encapsulated", testClass, "new value")).isTrue();
            verify(helper, times(1)).getSetter(TestClass.class, "Encapsulated");
            assertThat(testClass.getEncapsulated()).isEqualTo("new value");
        }

        @Test
        public void should_return_noValue_if_the_property_is_not_present() {
            assertThat(accessor.get("Nonexistent", testClass)).isSameAs(Accessor.noValue);
            verify(helper, times(1)).getGetter(TestClass.class, "Nonexistent");
        }

        @Test
        public void should_return_noValue_if_an_exception_is_thrown_when_retrieving_the_value() {
            assertThat(accessor.get("Throwing", testClass)).isEqualTo(Accessor.noValue);
            verify(helper, times(1)).getGetter(TestClass.class, "Throwing");
        }

        @Test
        public void should_not_set_the_property_if_an_exception_is_thrown_when_setting_the_value() {
            assertThat(accessor.set("Answer", testClass, "invalid value")).isTrue();
            verify(helper, times(1)).getSetter(TestClass.class, "Answer");
            assertThat(testClass.getAnswer()).isEqualTo(42);
        }
    }

    public static class when_requesting_self {
        IdentityAccessor accessor;
        TestClass testClass;

        @Before
        public void setUp() {
            testClass = new TestClass();
            accessor = new IdentityAccessor();
        }

        @Test
        public void should_return_self() {
            assertThat(accessor.get("this", testClass)).isSameAs(testClass);
        }

        @Test
        public void should_not_allow_to_set_the_value() {
            Object somethingElse = new Object();
            assertThat(accessor.set("this", testClass, somethingElse)).isTrue();
        }
    }

    public static class when_requesting_map_keys {
        MapAccessor accessor;
        Map<String, String> testClass;

        @Before
        public void setUp() {
            testClass = new HashMap<String, String>();
            testClass.put("existent", "existent value");
            accessor = new MapAccessor();
        }

        @Test
        public void should_return_the_map_value_if_present() {
            assertThat(accessor.get("existent", testClass)).isEqualTo("existent value");
        }

        @Test
        public void should_return_noValue_if_the_map_value_is_not_present() {
            assertThat(accessor.get("nonexistent", testClass)).isSameAs(Accessor.noValue);
        }

        @Test
        public void should_set_the_value_on_the_map_regardless_of_its_previous_presence() {
            accessor.set("existent", testClass, "changed value");
            assertThat(testClass.get("existent")).isEqualTo("changed value");
            accessor.set("nonexistent", testClass, "new value");
            assertThat(testClass.get("nonexistent")).isEqualTo("new value");
        }

        @Test
        public void should_return_noValue_if_the_source_is_not_a_map() {
            TestClass notAMap = new TestClass();
            notAMap.text = "some text";
            assertThat(accessor.get("text", notAMap)).isSameAs(Accessor.noValue);
        }

        @Test
        public void should_not_set_the_value_if_the_source_is_not_a_map() {
            TestClass notAMap = new TestClass();
            notAMap.text = "some text";
            accessor.set("text", notAMap, "changed text");
            assertThat(notAMap.text).isEqualTo("some text");
        }
    }

    public static class when_requesting_from_an_empty_accessor {
        TestClass testClass;

        @Before
        public void setUp() {
            testClass = new TestClass();
            testClass.text = "some text";
        }

        @Test
        public void should_always_return_noValue() {
            assertThat(EmptyAccessor.instance.get("text", testClass)).isSameAs(Accessor.noValue);
        }

        @Test
        public void should_never_set_the_value() {
            EmptyAccessor.instance.set("text", testClass, "changed text");
            assertThat(testClass.text).isEqualTo("some text");
        }
    }

    public static class when_decorating_an_accessor_with_a_converter {
        ConvertingAccessorDecorator accessor;
        IAccessor baseAccessor;
        IValueConverter valueConverter;
        TestClass testClass;

        @Before
        public void setUp() {
            baseAccessor = mock(IAccessor.class);
            valueConverter = mock(IValueConverter.class);
            testClass = new TestClass();

            when(valueConverter.convert("some text", "param")).thenReturn("converted value");
            when(valueConverter.convert("throw", "param")).thenThrow(new RuntimeException());
            when(valueConverter.convert("throwConvert", "param")).thenReturn("throw");

            when(valueConverter.convertBack("converted value", "param")).thenReturn("some text");
            when(valueConverter.convertBack("throwConvert", "param")).thenThrow(new RuntimeException());

            when(baseAccessor.get("text", testClass)).thenReturn("some text");
            when(baseAccessor.get("throwConvert", testClass)).thenReturn("throw");
            when(baseAccessor.get("throw", testClass)).thenThrow(new RuntimeException());
            when(baseAccessor.set("text", testClass, "some text")).thenReturn(true);
            when(baseAccessor.set("throw", testClass, "some text")).thenThrow(new RuntimeException());

            accessor = new ConvertingAccessorDecorator(baseAccessor, valueConverter, "param");
        }

        @Test
        public void should_request_a_conversion_with_the_base_value_and_return_it() {
            assertThat(accessor.get("text", testClass)).isEqualTo("converted value");
            verify(baseAccessor, times(1)).get("text", testClass);
            verify(valueConverter, times(1)).convert("some text", "param");
        }

        @Test
        public void should_request_a_conversion_back_when_setting_the_value() {
            assertThat(accessor.set("text", testClass, "converted value")).isTrue();
            verify(baseAccessor, times(1)).set("text", testClass, "some text");
            verify(valueConverter, times(1)).convertBack("converted value", "param");
        }

        @Test
        public void should_return_noValue_if_there_is_an_error_in_the_conversion() {
            assertThat(accessor.get("throwConvert", testClass)).isSameAs(Accessor.noValue);
            verify(baseAccessor, times(1)).get("throwConvert", testClass);
            verify(valueConverter, times(1)).convert("throw", "param");
        }

        @Test
        public void should_return_noValue_if_there_is_an_error_in_the_base_accessor() {
            assertThat(accessor.get("throw", testClass)).isSameAs(Accessor.noValue);
            verify(baseAccessor, times(1)).get("throw", testClass);
            verify(valueConverter, never()).convert(anyObject(), anyObject());
        }

        @Test
        public void should_not_set_any_value_if_there_is_an_error_in_the_conversion() {
            accessor.set("text", testClass, "throwConvert");
            verify(baseAccessor, never()).get(anyString(), anyObject());
            verify(valueConverter, times(1)).convertBack("throwConvert", "param");
        }

        @Test
        public void should_not_set_any_value_if_there_is_an_error_in_the_base_accessor() {
            accessor.set("throw", testClass, "some text");
            verify(baseAccessor, never()).get(anyString(), anyObject());
            verify(valueConverter, never()).convert(anyObject(), anyObject());
        }
    }
}
