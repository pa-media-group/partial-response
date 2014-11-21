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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.pressassociation.pr.match.Matcher;

/**
 * Set of utilities for filtering Jackson output based on partial response.
 *
 * @author Matt Nathan
 */
public class JacksonFilters {

  private static final String FILTER_ID = "PartialResponse";

  /**
   * Filter all serialised output via the given ObjectMapper with the given pattern.
   */
  public static ObjectMapper filterAllOutput(ObjectMapper mapper, CharSequence pattern) {
    return filterAllOutput(mapper, Matcher.of(pattern));
  }

  /**
   * Filter all serialised output via the given ObjectMapper with the given matcher.
   */
  public static ObjectMapper filterAllOutput(ObjectMapper mapper, Matcher matcher) {
    mapper.setFilters(new SimpleFilterProvider().addFilter(FILTER_ID, new JacksonMatcherFilter(matcher)));
    mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
      @Override
      public Object findFilterId(Annotated a) {
        return FILTER_ID;
      }
    });
    return mapper;
  }

  private JacksonFilters() {}
}
