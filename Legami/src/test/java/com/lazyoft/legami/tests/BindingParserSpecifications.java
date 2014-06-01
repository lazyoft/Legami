package com.lazyoft.legami.tests;

import com.lazyoft.legami.parsing.BindingParser;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(org.junit.experimental.runners.Enclosed.class)
public class BindingParserSpecifications {
    static BindingParser bindingParser;

    @BeforeClass
    public static void setUp() {
        bindingParser = new BindingParser();
    }

    public static class when_asked_to_parse_an_invalid_expression {

        @Test
        public void should_fail_if_the_assignment_is_incomplete() {
            assertThat(bindingParser.parse("foo := ").hasError()).isTrue();
        }

        @Test
        public void should_fail_if_the_operator_is_unrecognized() {
            assertThat(bindingParser.parse("foo += bar").hasError()).isTrue();
        }

        @Test
        public void should_fail_if_there_is_no_identifier_at_the_start() {
            assertThat(bindingParser.parse(":= bar").hasError()).isTrue();
        }

        @Test
        public void should_fail_if_the_constant_assignment_is_incomplete() {
            assertThat(bindingParser.parse("foo := #").hasError()).isTrue();
        }

        @Test
        public void should_fail_if_the_conversion_is_missing_a_parenthesis() {
            assertThat(bindingParser.parse("foo := convert(bar").hasError()).isTrue();
        }

        @Test
        public void should_fail_the_conversion_if_missing_the_parameter_with_a_comma() {
            assertThat(bindingParser.parse("foo := convert(bar,)").hasError()).isTrue();
        }

        @Test
        public void should_fail_the_conversion_if_missing_the_identifier_in_a_constant_parameter() {
            assertThat(bindingParser.parse("foo := convert(bar.baz, #)").hasError()).isTrue();
        }

        @Test
        public void should_fail_multiple_bindings_not_enclosed_in_angular_brackets() {
            assertThat(bindingParser.parse("{foo := bar} baz := goo").hasError()).isTrue();
        }
    }

    public static class when_asked_to_parse_a_valid_expression {
        @Test
        public void should_parse_simple_assignments() {
            assertThat(bindingParser.getTokenized("foo := bar")).isEqualTo("Binding[foo := Path[Identifier[bar]]]");
        }

        @Test
        public void should_parse_path_assignments() {
            assertThat(bindingParser.getTokenized("foo := bar.baz")).isEqualTo("Binding[foo := Path[Identifier[bar], Identifier[baz]]]");
        }

        @Test
        public void should_ignore_ending_path_specifiers_in_path() {
            assertThat(bindingParser.getTokenized("foo := bar.baz.")).isEqualTo("Binding[foo := Path[Identifier[bar], Identifier[baz]]]");
        }

        @Test
        public void should_parse_constant_assignments() {
            assertThat(bindingParser.getTokenized("foo := #constantValue")).isEqualTo("Binding[foo := Constant[constantValue]]");
        }

        @Test
        public void should_parse_simple_conversions() {
            assertThat(bindingParser.getTokenized("foo := convert(bar)")).isEqualTo("Binding[foo := Conversion[convert(Path[Identifier[bar]])]]");
        }

        @Test
        public void should_parse_conversions_with_string_parameters() {
            assertThat(bindingParser.getTokenized("foo := convert(bar.baz, /string literal/)")).isEqualTo("Binding[foo := Conversion[convert(Path[Identifier[bar], Identifier[baz]], String[string literal])]]");
        }

        @Test
        public void should_parse_conversions_with_escaped_strings_parameters() {
            assertThat(bindingParser.getTokenized("foo := convert(bar.baz, /string\\/literal/)")).isEqualTo("Binding[foo := Conversion[convert(Path[Identifier[bar], Identifier[baz]], String[string/literal])]]");
        }

        @Test
        public void should_parse_conversions_with_escaped_strings_parameters_preserving_real_escaped_text() {
            assertThat(bindingParser.getTokenized("foo := convert(bar.baz, /string\\/literal\\nNew line/)")).isEqualTo("Binding[foo := Conversion[convert(Path[Identifier[bar], Identifier[baz]], String[string/literal\\nNew line])]]");
        }

        @Test
        public void should_parse_conversions_with_constant_parameters() {
            assertThat(bindingParser.getTokenized("foo := convert(bar.baz, #constantValue)")).isEqualTo("Binding[foo := Conversion[convert(Path[Identifier[bar], Identifier[baz]], Constant[constantValue])]]");
        }

        @Test
        public void should_parse_multiple_bindings_if_enclosed_in_angular_brackets() {
            assertThat(bindingParser.getTokenized("{foo := bar} {baz =: goo}")).isEqualTo("Binding[foo := Path[Identifier[bar]]], Binding[baz =: Path[Identifier[goo]]]");
        }

        @Test
        public void should_parse_multiple_bindings_if_enclosed_in_angular_brackets_regardless_of_whitespace() {
            assertThat(bindingParser.getTokenized("    {foo := bar} {baz =: goo}  ")).isEqualTo("Binding[foo := Path[Identifier[bar]]], Binding[baz =: Path[Identifier[goo]]]");
        }
    }
}