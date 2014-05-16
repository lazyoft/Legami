package com.lazyoft.legami.tests;

import com.lazyoft.legami.accessors.AccessorResolverBase;
import com.lazyoft.legami.accessors.FieldAccessorResolver;
import com.lazyoft.legami.accessors.IAccessor;
import com.lazyoft.legami.accessors.IAccessorResolver;
import com.lazyoft.legami.accessors.IChainedAccessorResolver;
import com.lazyoft.legami.accessors.MapAccessorResolver;
import com.lazyoft.legami.accessors.PropertyAccessorResolver;
import com.lazyoft.legami.helpers.IPropertyHelper;

import junit.framework.TestCase;

import java.lang.reflect.Method;
import java.util.HashMap;

import rx.functions.Func2;

public class AccessorResolverSpecifications extends TestCase {
    IAccessorResolver subject;
    Func2<Object, String, IAccessor> factory;
    boolean factoryCalled;

    public class TestObject {
        public String field;
        private String property;

        public String getProperty() { return property; }
        public void setProperty(String value) { property = value; }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        factory = new Func2<Object, String, IAccessor>() {
            @Override
            public IAccessor call(Object o, String s) {
                factoryCalled = true;
                return null;
            }
        };
    }

    public void testFieldAccessorResolver_when_resolving_successfully_it_should_invoke_the_factory_function() {
        subject = new FieldAccessorResolver(factory);
        subject.resolve(new TestObject(), "field");
        assertTrue(factoryCalled);
    }

    public void testPropertyAccessorResolver_when_resolving_successfully_it_should_invoke_the_factory_function() {
        subject = new PropertyAccessorResolver(factory, new IPropertyHelper() {
            @Override
            public Method getGetter(Class objectClass, String path) {
                return Object.class.getMethods()[0];
            }

            @Override
            public Method getSetter(Class objectClass, String path) {
                return null;
            }
        });
        subject.resolve(new TestObject(), "Property");
        assertTrue(factoryCalled);
    }

    public void testMapAccessorResolver_when_resolving_successfully_it_should_invoke_the_factory_function() {
        subject = new MapAccessorResolver(factory);
        subject.resolve(new HashMap<String, Object>(), "key");
        assertTrue(factoryCalled);
    }

    public void testChainedAccessorResolver_when_resolving_should_delegate_to_the_next_in_the_chain_if_it_cannot_solve() {
        final boolean[] firstCalled = new boolean[1];
        final boolean[] secondCalled = new boolean[1];
        IChainedAccessorResolver first = new AccessorResolverBase(factory) {
            @Override
            protected IAccessor internalResolve(Object source, String path, Func2<Object, String, IAccessor> factory) {
                firstCalled[0] = true;
                return null;
            }
        };
        IChainedAccessorResolver second = new AccessorResolverBase(factory) {
            @Override
            protected IAccessor internalResolve(Object source, String path, Func2<Object, String, IAccessor> factory) {
                secondCalled[0] = true;
                return new IAccessor() { public Object get() { return null; }  public void set(Object value) { } };
            }
        };
        first.setNext(second);
        first.resolve(new Object(), "path");

        assertTrue(firstCalled[0]);
        assertTrue(secondCalled[0]);
    }

    public void testChainedAccessorResolver_when_resolving_should_stop_at_the_first_resolving_in_the_chain() {
        final boolean[] firstCalled = new boolean[1];
        final boolean[] secondCalled = new boolean[1];

        IChainedAccessorResolver first = new AccessorResolverBase(factory) {
            @Override
            protected IAccessor internalResolve(Object source, String path, Func2<Object, String, IAccessor> factory) {
                firstCalled[0] = true;
                return new IAccessor() { public Object get() { return null; }  public void set(Object value) { } };
            }
        };
        IChainedAccessorResolver second = new AccessorResolverBase(factory) {
            @Override
            protected IAccessor internalResolve(Object source, String path, Func2<Object, String, IAccessor> factory) {
                secondCalled[0] = true;
                return new IAccessor() { public Object get() { return null; }  public void set(Object value) { } };
            }
        };
        first.setNext(second);
        first.resolve(new Object(), "path");

        assertTrue(firstCalled[0]);
        assertFalse(secondCalled[0]);
    }

}