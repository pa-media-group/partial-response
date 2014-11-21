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

package com.pressassociation.pr.parser;

import com.google.common.collect.ImmutableList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test that the parsed pattern becomes the same as what we expect.
 *
 * @author Matt Nathan
 */
@RunWith(Parameterized.class)
public class ParserTest {

  @Parameterized.Parameters
  public static Collection<Object[]> parameters() {
    return ImmutableList.<Object[]>builder()
        // these are the examples given in the google documentation
        .add(correct("items"))
        .add(correct("etag,items"))
        .add(correct("items/title"))
        .add(correct("context/facets/label"))
        .add(correct("items/pagemap/*/title"))
        .add(correct("title"))
        .add(correct("author/uri"))
        .add(correct("links/*/href"))
        .add(correct("items(title,author/uri)"))
        .add(correct("kind,items(title,characteristics/length)"))
        .add(correct("items/pagemap/*"))
        .add(correct("items(id)"))
        .add(correct("items/id"))
        .add(correct("items(id,altId)/authority"))
        .add(incorrect(""))
        .add(incorrect("**"))
        .add(incorrect("*/"))
        .add(incorrect("/name"))
        .add(incorrect("//name"))
        .add(incorrect("//"))
        .add(incorrect("()"))
        .add(incorrect("items()"))
        .add(incorrect("items(id)*"))
        .add(incorrect("items(id"))
        .add(incorrect("items(id*"))
        .build();
  }

  private static Object[] correct(String s) {
    return new Object[]{s, true};
  }

  private static Object[] incorrect(String s) {
    return new Object[]{s, false};
  }

  private final String pattern;
  private final boolean correct;

  public ParserTest(String pattern, boolean correct) {
    this.correct = correct;
    this.pattern = pattern;
  }

  @Test
  public void testParse() {
    if (correct) {
      assertEquals(pattern, new Parser().parse(pattern).toString());
    } else {
      try {
        new Parser().parse(pattern);
        fail("Expecting an exception for pattern: " + pattern);
      } catch (IllegalArgumentException ignore) {
        // expected
      }
    }
  }
}
