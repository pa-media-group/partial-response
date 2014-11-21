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

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link FilteredObjectMapperResolver}.
 */
public class FilteredObjectMapperResolverTest {

  @Test
  public void testGetContext() {
    JacksonRequestParamFilter filter = mock(JacksonRequestParamFilter.class);
    ObjectMapper mapperResponse = new ObjectMapper();
    ArgumentCaptor<ObjectMapper> mapper = ArgumentCaptor.forClass(ObjectMapper.class);
    when(filter.configurePartialResponse(mapper.capture())).thenReturn(mapperResponse);
    ObjectMapper context = new FilteredObjectMapperResolver(filter).getContext(ObjectMapper.class);
    assertSame(mapperResponse, context);
    verify(filter).configurePartialResponse(mapper.getValue());
  }
}
