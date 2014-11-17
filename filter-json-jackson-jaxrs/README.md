Partial Response - JAX-RS Jackson JSON Filter
=============================================

This project makes it easy to add partial response filtering to your JAX-RS based REST API.

If your REST api is built using JAX-RS 2.0 and uses Jackson to do the JSON serialisation from Java POJOs then this
library should be able to help you.

Usage
-----

To use this library in your Maven project simply include the dependency

```xml
<dependency>
  <groupId>com.pressassociation.partial-response</groupId>
  <artifactId>filter-json-jackson-jaxrs</artifactId>
  <version>1.0</version>
</dependency>
```

Depending on your JAX-RS implementation you may also need to configure your JAX-RS environment so that it is aware of
the `FilteredObjectMapperResolver`. In general this involves including an instance of this class in your `Application`
providers list.

The `FilteredObjectMapperResolver` is annotated with JSR-330 `@Inject` annotations as a mechanism to resolve it's
dependencies, if you are using a container or environment that doesn't support JSR-330 then you will need to configure
the `FilteredObjectMapperResolver` manually. You may also need to do this if you plan on changing the default `fields`
request parameter that is used to populate the partial response pattern from the incoming request.

### Advanced Usage

If you are already customising the `ObjectMapper` for your JAX-RS Jackson application then you should look at the
`JacksonRequestParamFilter`. This class encapsulates the configuration of `ObjectMapper` for re-use without the baggage
brought by a full JAX-RS filter. To use you should obtain an instance of `JacksonRequestParamFilter` via your favourite
DI method and call its `configurePartialResponse` method on the `ObjectMapper` instance.

```java
@Provider
@Singleton
public class IndentedObjectMapperResolver implements ContextResolver<ObjectMapper> {

  private final JacksonRequestParamFilter paramFilter;

  @Inject
  public IndentedObjectMapperResolver(JacksonRequestParamFilter paramFilter) {
    this.paramFilter = checkNotNull(paramFilter);
  }

  @Override
  public ObjectMapper getContext(Class<?> type) {
    ObjectMapper mapper = new ObjectMapper();

    // do my own customisation
    mapper.enable(SerializationFeature.INDENT_OUTPUT);

    // configure the partial response filter
    paramFilter.configurePartialResponse(mapper);

    return mapper;
  }
}
```
