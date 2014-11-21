/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Press Association Limited
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
