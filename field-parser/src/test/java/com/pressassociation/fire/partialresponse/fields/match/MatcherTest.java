package com.pressassociation.fire.partialresponse.fields.match;

import com.google.common.base.Function;
import com.google.common.base.Functions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link Matcher}.
 *
 * @author Matt Nathan
 */
public class MatcherTest {
  /**
   * This test checks that the example in the readme actually does what we want it to.
   */
  @Test
  public void testReadmeExamples() {
    Matcher matcher = Matcher.of("name,address,spouse(name,address),children/*");

    // matches all of these Leafs
    assertTrue(matcher.matches("/name"));
    assertTrue(matcher.matches("/address"));
    assertTrue(matcher.matches("/address/houseNumber"));
    assertTrue(matcher.matches("/spouse/name"));
    assertTrue(matcher.matches("/spouse/address"));
    assertTrue(matcher.matches("/children"));
    assertTrue(matcher.matches("/children/any/path"));

    // doesn't match any of these
    assertFalse(matcher.matches("/spouse"));
    assertFalse(matcher.matches("/unknown"));
    assertFalse(matcher.matches("/spouse/children"));
  }

  @Test
  public void testMatchesAll() {
    assertTrue(Matcher.of("*").matchesAll());
    assertTrue(Matcher.all().matchesAll());
    assertFalse(Matcher.of("all").matchesAll());
    assertFalse(Matcher.of("all/*").matchesAll());
  }

  @Test
  public void testTransform() {
    assertEquals("*", Matcher.all().transform(Functions.constant("foo")).patternString());
    assertSame(Matcher.all(), Matcher.all().transform(Functions.constant("bar")));
    assertEquals("identity/path", Matcher.of("identity/path").transform(Functions.<String>identity()).patternString());
    assertEquals("foo(foo),*", Matcher.of("any(thing),*").transform(Functions.constant("foo")).patternString());
    assertEquals("FOO/*,BAR/*(BAZ)", Matcher.of("foo/*,bar/*(baz)").transform(upperCase()).patternString());
  }

  private Function<String, String> upperCase() {
    return new Function<String, String>() {
      @Override
      public String apply(String input) {
        return input.toUpperCase();
      }
    };
  }
}
