package com.lazyoft.legami.tests;

import com.lazyoft.legami.accessors.IAccessor;
import com.lazyoft.legami.accessors.IValueConverter;
import com.lazyoft.legami.binding.IProperty;
import com.lazyoft.legami.binding.IResourceProvider;
import com.lazyoft.legami.binding.IValueConverterProvider;
import com.lazyoft.legami.binding.TokenVisitor;
import com.lazyoft.legami.binding.VisitResultList;
import com.lazyoft.legami.parsing.BindingParser;
import com.lazyoft.legami.parsing.IBindingParser;
import com.lazyoft.legami.parsing.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.junit.experimental.runners.Enclosed.class)
public class TokenVisitorSpecifications {
    public static class when_requesting_to_visit_simple_expressions {
        TokenVisitor visitor;
        IAccessor accessorChain;
        IValueConverterProvider valueConverterProvider;
        IValueConverter valueConverter;
        IResourceProvider resourceProvider;
        IBindingParser parser;
        Object sourceObject;
        Object resource;

        @Before
        public void setUp() {
            sourceObject = new Object();
            resource = new Object();

            accessorChain = mock(IAccessor.class);
            valueConverterProvider = mock(IValueConverterProvider.class);
            valueConverter = mock(IValueConverter.class);
            resourceProvider = mock(IResourceProvider.class);
            parser = new BindingParser();

            when(valueConverterProvider.get(anyString())).thenReturn(valueConverter);
            when(accessorChain.get(anyString(), eq(sourceObject))).thenReturn(sourceObject);
            when(resourceProvider.get(anyString())).thenReturn(resource);

            visitor = new TokenVisitor(accessorChain, valueConverterProvider, resourceProvider);
        }

        @Test
        public void should_return_the_binding_expression_corresponding_to_the_tokens() {
            VisitResultList result = whenVisiting("source := target");
            assertThat(result.getCurrentResult().converted).isFalse();

            verifyProperty(result.getCurrentResult().source, "source");
            verifyProperty(result.getCurrentResult().target, "target");
        }

        @Test
        public void should_return_the_binding_expression_composing_a_path_if_present() {
            VisitResultList result = whenVisiting("source := target.sub1.sub2.sub3");
            assertThat(result.getCurrentResult().converted).isFalse();

            verifyProperty(result.getCurrentResult().source, "source");
            verifyProperty(result.getCurrentResult().target, "target", "sub1", "sub2", "sub3");
        }

        @Test
        public void should_return_the_binding_expression_composing_a_conversion_if_present() {
            VisitResultList result = whenVisiting("source := convert(target.sub1)");
            assertThat(result.getCurrentResult().converted).isTrue();
            assertThat(result.getCurrentResult().parameter).isNull();

            verifyProperty(result.getCurrentResult().source, "source");
            verifyProperty(result.getCurrentResult().target, "target", "sub1");
            verifyConverterCall("convert", null);
        }

        @Test
        public void should_return_the_binding_expression_composing_a_conversion_with_a_string_parameter_if_present() {
            VisitResultList result = whenVisiting("source := convert(target.sub1, /string literal/)");
            assertThat(result.getCurrentResult().converted).isTrue();
            assertThat(result.getCurrentResult().parameter).isEqualTo("string literal");

            verifyProperty(result.getCurrentResult().source, "source");
            verifyProperty(result.getCurrentResult().target, "target", "sub1");
            verifyConverterCall("convert", "string literal");
        }

        @Test
        public void should_return_the_binding_expression_composing_a_conversion_with_a_resource_if_present() {
            VisitResultList result = whenVisiting("source := convert(target.sub1, #resource)");
            assertThat(result.getCurrentResult().converted).isTrue();
            assertThat(result.getCurrentResult().parameter).isSameAs(resource);

            verifyProperty(result.getCurrentResult().source, "source");
            verifyProperty(result.getCurrentResult().target, "target", "sub1");
            verifyConverterCall("convert", resource);
            verify(resourceProvider, times(1)).get("resource");
        }

        @Test
        public void should_return_the_binding_expression_composing_an_assignment_to_a_resource_if_specified() {
            VisitResultList result = whenVisiting("source := #resource");
            assertThat(result.getCurrentResult().converted).isFalse();

            verifyProperty(result.getCurrentResult().source, "source");
            verify(resourceProvider, times(1)).get("resource");
            assertThat(result.getCurrentResult().target.get(sourceObject)).isSameAs(resource);
        }

        @Test
        public void should_return_a_binding_expression_for_every_binding_expression_present() {
            VisitResultList result = whenVisiting("{source := target} {otherSource := otherTarget} {last := lastTarget}");
            assertThat(result.getResults().size()).isEqualTo(3);
        }

        private VisitResultList whenVisiting(String expression) {
            Token root = parser.parse(expression).getRoot();
            return visitor.visit(root, null);
        }

        private void verifyProperty(IProperty property, String ...propertyNames) {
            assertThat(property).isNotNull();

            property.get(sourceObject);
            for(String propertyName: propertyNames)
                verify(accessorChain, atLeastOnce()).get(propertyName, sourceObject);

            property.set(sourceObject, "new value");
            String[] slice = Arrays.copyOfRange(propertyNames, 0, propertyNames.length - 1);
            for(String propertyName: slice)
                verify(accessorChain, atLeastOnce()).get(propertyName, sourceObject);
            verify(accessorChain, atMost(1)).set(propertyNames[propertyNames.length - 1], sourceObject, "new value");
        }

        private void verifyConverterCall(String conversion, Object param) {
            verify(valueConverterProvider, atLeastOnce()).get(conversion);
            verify(valueConverter, atLeastOnce()).convert(anyObject(), eq(param));
        }
    }
}
