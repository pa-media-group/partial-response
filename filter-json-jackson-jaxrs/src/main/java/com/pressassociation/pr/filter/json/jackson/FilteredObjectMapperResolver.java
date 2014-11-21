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

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>Standard ContextResolver for resolving an ObjectMapper than filters output based on a partial response field.
 *
 * <p>This class can be used as is to provide default partial response filtering of all json responses. If, however,
 * you need more control over the creation of the ObjectMapper consider using {@link JacksonRequestParamFilter}
 * directly instead of overriding this class to add your functionality.
 *
 * @author Matt Nathan
 */
@Provider
@Singleton
public class FilteredObjectMapperResolver implements ContextResolver<ObjectMapper> {

  private final JacksonRequestParamFilter paramFilter;

  @Inject
  public FilteredObjectMapperResolver(JacksonRequestParamFilter paramFilter) {
    this.paramFilter = checkNotNull(paramFilter);
  }

  @Override
  public ObjectMapper getContext(Class<?> type) {
    checkNotNull(type);
    return paramFilter.configurePartialResponse(new ObjectMapper());
  }
}
