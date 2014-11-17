package com.pressassociation.pr.filter.json.jackson;

import com.google.common.testing.AbstractPackageSanityTests;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.pressassociation.fire.partialresponse.fields.match.Matcher;

import static org.mockito.Mockito.mock;

/**
 * Tests for equals, nulls and serialisation for classes in this package.
 *
 * @author Matt Nathan
 */
public class PackageSanityTest extends AbstractPackageSanityTests {
  @Override
  public void setUp() throws Exception {
    super.setUp();
    setDefault(JacksonRequestParamFilter.class, mock(JacksonRequestParamFilter.class));
    setDefault(Matcher.class, Matcher.all());
    setDefault(JsonGenerator.class, mock(JsonGenerator.class));
    setDefault(SerializerProvider.class, mock(SerializerProvider.class));
    setDefault(PropertyWriter.class, mock(PropertyWriter.class));
    setDefault(CharSequence.class, "*");
  }
}
