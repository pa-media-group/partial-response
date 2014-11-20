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
