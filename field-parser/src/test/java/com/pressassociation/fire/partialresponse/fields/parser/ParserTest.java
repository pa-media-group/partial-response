package com.pressassociation.fire.partialresponse.fields.parser;

import com.google.common.collect.ImmutableList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test that the parsed pattern becomes the same as what we expect
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
