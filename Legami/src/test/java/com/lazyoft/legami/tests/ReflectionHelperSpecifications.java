package com.lazyoft.legami.tests;

import com.lazyoft.legami.helpers.ReflectionHelper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(org.junit.experimental.runners.Enclosed.class)
public class ReflectionHelperSpecifications {
    static ReflectionHelper helper;

    @BeforeClass
    public static void setUp() {
        helper = new ReflectionHelper();
    }

    public static class when_asking_for_public_properties {
        @Test
        public void should_return_field_instances() {
            assertThat(helper.getField(TestClass.class, "text")).isNotNull();
        }

        @Test
        public void should_return_getter_instances() {
            assertThat(helper.getGetter(TestClass.class, "Encapsulated")).isNotNull();
        }

        @Test
        public void should_return_setter_instances() {
            assertThat(helper.getSetter(TestClass.class, "Encapsulated")).isNotNull();
        }
    }

    public static class when_asking_for_private_properties {
        @Test
        public void should_not_return_private_fields() {
            assertThat(helper.getField(TestClass.class, "hidden")).isNull();
        }

        @Test
        public void should_not_return_private_getters() {
            assertThat(helper.getGetter(TestClass.class, "Hidden")).isNull();
        }

        @Test
        public void should_not_return_private_setters() {
            assertThat(helper.getSetter(TestClass.class, "Hidden")).isNull();
        }
    }

    public static class when_asking_for_nonexistent_properties {
        @Test
        public void should_not_return_fields() {
            assertThat(helper.getField(TestClass.class, "nonexistent")).isNull();
        }

        @Test
        public void should_not_return_getters() {
            assertThat(helper.getGetter(TestClass.class, "NonExistent")).isNull();
        }

        @Test
        public void should_not_return_setters() {
            assertThat(helper.getSetter(TestClass.class, "NonExistent")).isNull();
        }
    }
}
