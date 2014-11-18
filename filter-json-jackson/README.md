Partial Response - Jackson JSON Filter
======================================

This project adds easy JSON filtering to [Jackson](http://jackson.codehaus.org/) projects.

Usage
-----

To use this in your Maven project simply include the dependency

```xml
<dependency>
  <groupId>com.pressassociation.partial-response</groupId>
  <artifactId>filter-json-jackson</artifactId>
  <version>1.0</version>
</dependency>
```

Then make use of the filter by configuring your `ObjectMapper` with an instance of `JacksonMatcherFilter`.

There are two ways to configure your `ObjectMapper`, the first is to explicitly setup a `@JsonFilter` type filter for
your target type. This approach is somewhat limited when considering the intended use case for partial-response. The
alternative is to configure all filters to be partial response and create a new `ObjectMapper` each time.

### Filter everything example

You can configure Jackson to filter any object using `JacksonFilters` like this:

```java
// configures the given ObjectMapper so all written json is affected by the given output pattern
ObjectMapper mapper = JacksonFilters.filterAllOutput(new ObjectMapper(), "count,*/name");

// marshal the json which will use the filter configured above due to the @JsonFilter annotation
String filteredJson = mapper.writeValueAsString(new MyJsonObject());
```

### Explicit example

You can configure Jackson to filter the objects by associating a `JacksonMatcherFilter` with an `@JsonFilter` id. Be
sure to annotate all classes in the entire java json structure so they all get filtered by the configured
`JacksonMatcherFilter`.

```java
// create the ObjectMapper, somehow
ObjectMapper mapper = new ObjectMapper();

// the matcher we will be using
Matcher matcher = Matcher.of("count,*/name");

// create a filter provider that uses the above matcher, the "filter" id matches the one provided
// in the @JsonFilter annotations. Note that every object in the java object graph that jackson will
// be serialising will need the annotation pointing to the same filter
SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider()
    .addFilter("filter", new JacksonMatcherFilter(matcher));

// configure the filter provider to include our filter
mapper.setFilters(simpleFilterProvider);

// marshal the json which will use the filter configured above due to the @JsonFilter annotation
String filteredJson = mapper.writeValueAsString(new MyJsonObject());
```
