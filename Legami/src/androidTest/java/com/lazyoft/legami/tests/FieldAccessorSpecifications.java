package com.lazyoft.legami.tests;

import com.lazyoft.legami.accessors.Accessor;
import com.lazyoft.legami.accessors.FieldAccessor;

import junit.framework.TestCase;

public class FieldAccessorSpecifications extends TestCase {
    public class TestObject {
        public String field;
        private String inaccessible;
    }

    public TestObject testObject;
    public FieldAccessor subject;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testObject = new TestObject();
        testObject.field = "testValue";
        testObject.inaccessible = "inaccessibleValue";
    }

    public void testWhen_requesting_a_value_for_an_existing_public_field_it_should_return_the_value() throws Exception {
        subject = new FieldAccessor(testObject, "field");
        assertEquals("testValue", subject.get());
    }

    public void testWhen_requesting_a_value_for_an_existing_private_field_it_should_return_a_NoValue() throws Exception {
        subject = new FieldAccessor(testObject, "inaccessible");
        assertEquals(Accessor.NoValue, subject.get());
    }

    public void testWhen_setting_a_value_for_an_existing_public_field_it_should_set_the_value() throws Exception {
        subject = new FieldAccessor(testObject, "field");
        subject.set("newValue");
        assertEquals("newValue", testObject.field);
    }

    public void testWhen_setting_a_value_for_an_existing_private_field_it_should_not_set_the_value() throws Exception {
        subject = new FieldAccessor(testObject, "inaccessible");
        subject.set("newValue");
        assertEquals("inaccessibleValue", testObject.inaccessible);
    }

    public void testWhen_requesting_a_value_for_a_non_existing_field_it_should_return_a_NoValue() throws Exception {
        subject = new FieldAccessor(testObject, "nonexistent");
        assertEquals(Accessor.NoValue, subject.get());
    }
}