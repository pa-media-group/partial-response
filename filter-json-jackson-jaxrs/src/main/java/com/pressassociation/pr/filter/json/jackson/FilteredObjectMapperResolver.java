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
