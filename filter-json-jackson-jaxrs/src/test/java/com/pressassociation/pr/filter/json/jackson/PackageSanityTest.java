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

import com.google.common.testing.AbstractPackageSanityTests;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.pressassociation.pr.match.Matcher;

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
