package com.pressassociation.pr.filter.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.pressassociation.fire.partialresponse.fields.match.Matcher;

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
