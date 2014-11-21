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

package com.pressassociation.pr.match;

import com.google.common.base.Function;
import com.google.common.base.Functions;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link Matcher}.
 *
 * @author Matt Nathan
 */
@RunWith(JUnitParamsRunner.class)
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
  public void testEmptyIsNone() {
    assertSame(Matcher.none(), Matcher.of(""));
  }

  @Test
  public void testMatchesAll() {
    assertTrue(Matcher.of("*").matchesAll());
    assertTrue(Matcher.all().matchesAll());
    assertFalse(Matcher.of("all").matchesAll());
    assertFalse(Matcher.of("all/*").matchesAll());
    assertFalse(Matcher.of("").matchesAll());
    assertFalse(Matcher.none().matchesAll());
  }

  @Test
  @Parameters({
      "a",
      "a/b",
      "foo/bar/baz"
  })
  public void testNoneMatchesNothing(CharSequence path) {
    assertFalse(Matcher.none().matches(path));
  }

  @Test
  @Parameters({
      "root",
      "some",
      "some/path",
      "some/path/element",
      "mid",
      "mid/anything",
      "mid/anything/with/path",
      "mid/value",
      "sub",
      "sub/name",
      "sub/value",
      "sub/name/text",
      "sub/value/text"
  })
  public void testMatchesParent(CharSequence matchingParent) {
    Matcher matcher = Matcher.of("root,some/path/element,mid/*/value,sub(name,value)/text");
    assertTrue("matchesParent(" + matchingParent + ") is false", matcher.matchesParent(matchingParent));
  }

  @Test
  @Parameters({
      "bad",
      "bad/with",
      "bad/with/path",
      "root/sub",
      "some/path/element/extra",
      "some/path/error",
      "sub/unknown",
      "sub/value/random",
      "sub/name/random",
      "sub/name/text/another",
      "sub/value/text/another"
  })
  public void testNotMatchesParent(CharSequence nonMatchingParent) {
    Matcher matcher = Matcher.of("root,some/path/element,mid/*/value,sub(name,value)/text");
    assertFalse("matchesParent(" + nonMatchingParent + ") is true", matcher.matchesParent(nonMatchingParent));
  }

  @Test
  public void testMatchesParentAll() {
    assertTrue(Matcher.all().matchesParent("my"));
    assertTrue(Matcher.all().matchesParent("my/node"));
    assertTrue(Matcher.all().matchesParent("my/node/continues"));
  }

  @Test
  @Parameters({
      "a",
      "a/b",
      "foo/bar/baz"
  })
  public void testNoneMatchesNothingParent(CharSequence path) {
    assertFalse(Matcher.none().matchesParent(path));
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
