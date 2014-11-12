package com.pressassociation.fire.partialresponse.fields.match;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link Matcher}.
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
    assertTrue(matcher.matches(Leaf.fromPath("/name")));
    assertTrue(matcher.matches(Leaf.fromPath("/address")));
    assertTrue(matcher.matches(Leaf.fromPath("/address/houseNumber")));
    assertTrue(matcher.matches(Leaf.fromPath("/spouse/name")));
    assertTrue(matcher.matches(Leaf.fromPath("/spouse/address")));
    assertTrue(matcher.matches(Leaf.fromPath("/children")));
    assertTrue(matcher.matches(Leaf.fromPath("/children/any/path")));

    // doesn't match any of these
    assertFalse(matcher.matches(Leaf.fromPath("/spouse")));
    assertFalse(matcher.matches(Leaf.fromPath("/unknown")));
    assertFalse(matcher.matches(Leaf.fromPath("/spouse/children")));
  }
}
