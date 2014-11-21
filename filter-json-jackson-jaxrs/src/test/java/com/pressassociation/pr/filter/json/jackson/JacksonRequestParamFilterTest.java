/*
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
 */

package com.pressassociation.pr.filter.json.jackson;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.pressassociation.pr.match.Matcher;

import org.jboss.resteasy.spi.ResteasyUriInfo;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URI;

import javax.annotation.Nullable;
import javax.inject.Provider;
import javax.ws.rs.core.UriInfo;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link JacksonRequestParamFilter}.
 */
@RunWith(JUnitParamsRunner.class)
public class JacksonRequestParamFilterTest {

  /**
   * Parameter encapsulation for variation testing.
   */
  public static class Params {

    public static Params applies(String uri, CharSequence pattern) {
      return create(uri, pattern);
    }

    public static Params absent(String uri) {
      return create(uri, null);
    }

    private static Params create(String uri, @Nullable CharSequence pattern) {
      Optional<Matcher> matcher = pattern == null ? Optional.<Matcher>absent() : Optional.of(Matcher.of(pattern));
      return new Params("http://test/?" + uri, matcher);
    }

    final String uri;
    final Optional<Matcher> matcher;

    public Params(String uri, Optional<Matcher> matcher) {
      this.uri = checkNotNull(uri);
      this.matcher = checkNotNull(matcher);
    }

    public UriInfo getUriInfo() {
      return new ResteasyUriInfo(URI.create(uri));
    }

    public Provider<UriInfo> scopedUriInfo() {
      return new Provider<UriInfo>() {
        @Override
        public UriInfo get() {
          return getUriInfo();
        }
      };
    }

    @Override
    public String toString() {
      if (matcher.isPresent()) {
        return "URI " + uri + " should apply partial response of " + matcher.get();
      } else {
        return "URI " + uri + " should have no partial response applied";
      }
    }
  }

  /**
   * Test object for ObjectMapper tests.
   */
  private static class TestObject {}

  @Test
  @Parameters(method = "params")
  public void testConfigurePartialResponse(Params params) {
    JacksonRequestParamFilter subject = new JacksonRequestParamFilter(params.scopedUriInfo());
    ObjectMapper mapper = subject.configurePartialResponse(new ObjectMapper());

    if (params.matcher.isPresent()) {
      assertConfiguredWithMatcher(mapper, params.matcher.get());
    } else {
      assertConfiguredWithoutMatcher(mapper);
    }
  }

  @SuppressWarnings("UnusedDeclaration")
  private Iterable<Params> params() {
    return ImmutableList.of(
        Params.absent(""),
        Params.absent("fields="),
        Params.absent("fields"),
        Params.absent("random=foo/bar"),
        Params.applies("fields=foo/bar", "foo/bar"),
        Params.applies("fields=*", "*"),
        Params.applies("fields=a,b", "a,b")
    );
  }

  private void assertConfiguredWithMatcher(ObjectMapper mapper, Matcher matcher) {
    AnnotationIntrospector introspector = mapper.getSerializationConfig().getAnnotationIntrospector();
    Object filterId = introspector.findFilterId((Annotated) AnnotatedClass.construct(
        TestObject.class, introspector, mapper.getDeserializationConfig()));
    assertNotNull(filterId);

    PropertyFilter propertyFilter =
        mapper.getSerializationConfig().getFilterProvider().findPropertyFilter(filterId, new TestObject());
    assertNotNull(propertyFilter);
    assertTrue(propertyFilter instanceof JacksonMatcherFilter);
    assertEquals(matcher, ((JacksonMatcherFilter) propertyFilter).getMatcher());
  }

  private void assertConfiguredWithoutMatcher(ObjectMapper mapper) {
    AnnotationIntrospector introspector = mapper.getSerializationConfig().getAnnotationIntrospector();
    Object filterId = introspector.findFilterId((Annotated) AnnotatedClass.construct(
        TestObject.class, introspector, mapper.getDeserializationConfig()));
    assertNull(filterId);
  }
}
