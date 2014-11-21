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

import com.google.common.base.Strings;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Encapsulates the logic for filtering a request against a partial response.
 *
 * @author Matt Nathan
 */
public class JacksonRequestParamFilter {
  /**
   * Default field name for the partial response field.
   */
  public static final String DEFAULT_FIELD_NAME = "fields";

  private final Provider<UriInfo> uriInfo;
  private final String fieldName;

  /**
   * Create a new JacksonRequestParamFilter using the {@link #DEFAULT_FIELD_NAME default} field name of {@code fields}.
   */
  @Inject
  public JacksonRequestParamFilter(Provider<UriInfo> uriInfo) {
    this(uriInfo, DEFAULT_FIELD_NAME);
  }

  /**
   * Create a new JacksonRequestParamFilter that uses the given {@code fieldName}.
   */
  public JacksonRequestParamFilter(Provider<UriInfo> uriInfo, String fieldName) {
    this.uriInfo = checkNotNull(uriInfo);
    this.fieldName = checkNotNull(fieldName);
  }

  /**
   * Configure the given ObjectMapper to filter output based on a request parameter interpreted as a partial response
   * pattern.
   */
  public ObjectMapper configurePartialResponse(ObjectMapper mapper) {
    checkNotNull(mapper);
    MultivaluedMap<String, String> queryParameters = uriInfo.get().getQueryParameters();
    String fieldValue = queryParameters.getFirst(fieldName);
    if (!Strings.isNullOrEmpty(fieldValue)) {
      JacksonFilters.filterAllOutput(mapper, fieldValue);
    }
    return mapper;
  }
}
