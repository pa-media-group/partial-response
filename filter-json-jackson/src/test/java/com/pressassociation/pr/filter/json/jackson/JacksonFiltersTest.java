package com.pressassociation.pr.filter.json.jackson;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.pressassociation.pr.match.Matcher;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link JacksonFilters}.
 */
@RunWith(JUnitParamsRunner.class)
public class JacksonFiltersTest {

  /**
   * Different overloaded methods under test.
   */
  public static enum OverloadedFilterAllOutput {
    CHAR_SEQUENCE {
      @Override
      public ObjectMapper filterAllOutput(ObjectMapper mapper, CharSequence input) {
        return JacksonFilters.filterAllOutput(mapper, input);
      }
    },

    MATCHER {
      @Override
      public ObjectMapper filterAllOutput(ObjectMapper mapper, CharSequence input) {
        return JacksonFilters.filterAllOutput(mapper, Matcher.of(input));
      }
    };

    public abstract ObjectMapper filterAllOutput(ObjectMapper mapper, CharSequence input);
  }

  /**
   * Test object for ObjectMapper tests.
   */
  private static class TestObject {}

  @Test
  @Parameters(method = "methods")
  public void testFilterAllOutput(OverloadedFilterAllOutput method) {
    ObjectMapper mapper = method.filterAllOutput(new ObjectMapper(), "foo/bar");
    AnnotationIntrospector introspector = mapper.getSerializationConfig().getAnnotationIntrospector();
    Object filterId = introspector.findFilterId((Annotated) AnnotatedClass.construct(
        TestObject.class, introspector, mapper.getDeserializationConfig()));
    assertNotNull(filterId);

    PropertyFilter propertyFilter =
        mapper.getSerializationConfig().getFilterProvider().findPropertyFilter(filterId, new TestObject());
    assertNotNull(propertyFilter);
    assertTrue(propertyFilter instanceof JacksonMatcherFilter);
    assertEquals(Matcher.of("foo/bar"), ((JacksonMatcherFilter) propertyFilter).getMatcher());
  }

  @SuppressWarnings("UnusedDeclaration")
  private OverloadedFilterAllOutput[] methods() {
    return OverloadedFilterAllOutput.values();
  }
}
