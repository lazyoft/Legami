package com.lazyoft.legami.tests;

import com.lazyoft.legami.accessors.Accessor;
import com.lazyoft.legami.accessors.PropertyAccessor;
import com.lazyoft.legami.helpers.PropertyHelper;

import junit.framework.TestCase;

public class PropertyAccessorSpecifications extends TestCase {
    public class TestObject {
        private String inaccessible;
        private String property;

        public String getProperty() { return property; }
        public void setProperty(String value) { property = value; }

        private String getInaccessible() { return inaccessible; }
        private void setInaccessible(String value) { inaccessible = value; }
    }

    public TestObject testObject;
    public PropertyAccessor subject;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testObject = new TestObject();
        testObject.property = "testValue";
        testObject.inaccessible = "inaccessibleValue";
    }

    public void testWhen_requesting_a_value_for_an_existing_public_property_it_should_return_the_value() throws Exception {
        subject = new PropertyAccessor(testObject, "Property", new PropertyHelper());
        assertEquals("testValue", subject.get());
    }

    public void testWhen_requesting_a_value_for_an_existing_private_property_it_should_return_a_NoValue() throws Exception {
        subject = new PropertyAccessor(testObject, "Inaccessible", new PropertyHelper());
        assertEquals(Accessor.NoValue, subject.get());
    }

    public void testWhen_setting_a_value_for_an_existing_public_property_it_should_set_the_value() throws Exception {
        subject = new PropertyAccessor(testObject, "Property", new PropertyHelper());
        subject.set("newValue");
        assertEquals("newValue", testObject.getProperty());
    }

    public void testWhen_setting_a_value_for_an_existing_private_property_it_should_not_set_the_value() throws Exception {
        subject = new PropertyAccessor(testObject, "Inaccessible", new PropertyHelper());
        subject.set("newValue");
        assertEquals("inaccessibleValue", testObject.getInaccessible());
    }

    public void testWhen_requesting_a_value_for_a_non_existing_property_it_should_return_a_NoValue() throws Exception {
        subject = new PropertyAccessor(testObject, "Nonexistent", new PropertyHelper());
        assertEquals(Accessor.NoValue, subject.get());
    }
}