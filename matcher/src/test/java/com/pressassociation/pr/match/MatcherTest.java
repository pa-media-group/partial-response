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
import com.google.common.collect.ImmutableList;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static junitparams.JUnitParamsRunner.$;
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

  @Test
  @Parameters(method = "rebaseWildcardParams")
  public void testRebaseWildcards(Matcher source, CharSequence path, Matcher result) throws Exception {
    assertEquals(result, source.rebase(path));
  }

  @Test
  @Parameters(method = "rebaseSingleEntryParams")
  public void testRebaseSingleEntry(Matcher source, CharSequence path, Matcher result) throws Exception {
    assertEquals(result, source.rebase(path));
  }

  @Test
  @Parameters(method = "rebaseSingleFieldParams")
  public void testRebaseSingleField(Matcher source, CharSequence path, Matcher result) throws Exception {
    assertEquals(result, source.rebase(path));
  }

  @Test
  @Parameters(method = "rebaseSingleFieldWildcardsParams")
  public void testRebaseSingleFieldWildcards(Matcher source, CharSequence path, Matcher result) throws Exception {
    assertEquals(result, source.rebase(path));
  }

  @Test
  @Parameters(method = "rebaseMultipleFieldsParams")
  public void testRebaseMultipleFields(Matcher source, CharSequence path, Matcher result) throws Exception {
    System.out.println(source + ".narrow(" + path + ") => " + result);
    assertEquals(result, source.rebase(path));
  }

  @Test
  @Parameters(method = "rebaseSubSelectionParams")
  public void testRebaseSubSelection(Matcher source, CharSequence path, Matcher result) throws Exception {
    assertEquals(result, source.rebase(path));
  }

  @Test
  @Parameters(method = "rebaseSubSelectionNestedParams")
  public void testRebaseSubSelectionNested(Matcher source, CharSequence path, Matcher result) throws Exception {
    assertEquals(result, source.rebase(path));
  }

  @Test
  @Parameters(method = "rebaseJavadocParams")
  public void testRebaseJavadocParams(Matcher source, CharSequence path, Matcher result) throws Exception {
    assertEquals(result, source.rebase(path));
  }

  @SuppressWarnings("UnusedDeclaration")
  private Iterable<Object[]> rebaseWildcardParams() {
    ImmutableList.Builder<Object[]> args = ImmutableList.builder();

    // wildcards
    args.add(
        $(Matcher.all(), "anything", Matcher.all()),
        $(Matcher.all(), "any/path", Matcher.all())
    );

    return args.build();
  }

  @SuppressWarnings("UnusedDeclaration")
  private Iterable<Object[]> rebaseSingleEntryParams() {
    ImmutableList.Builder<Object[]> args = ImmutableList.builder();

    // single entry source
    args.add(
        $(Matcher.of("simple"), "simple", Matcher.all()),
        $(Matcher.of("simple"), "simple/path", Matcher.all()),
        $(Matcher.of("simple"), "unknown", Matcher.none())
    );

    return args.build();
  }

  @SuppressWarnings("UnusedDeclaration")
  private Iterable<Object[]> rebaseSingleFieldParams() {
    ImmutableList.Builder<Object[]> args = ImmutableList.builder();

    // single field source
    args.add(
        $(Matcher.of("a/path"), "a", Matcher.of("path")),
        $(Matcher.of("a/path"), "a/path", Matcher.all()),
        $(Matcher.of("a/path"), "unknown", Matcher.none()),
        $(Matcher.of("a/path"), "a/unknown", Matcher.none()),
        $(Matcher.of("a/b/c"), "a", Matcher.of("b/c")),
        $(Matcher.of("a/b/c"), "a/b", Matcher.of("c")),
        $(Matcher.of("a/b/c"), "a/b/c", Matcher.all())
    );

    return args.build();
  }

  @SuppressWarnings("UnusedDeclaration")
  private Iterable<Object[]> rebaseSingleFieldWildcardsParams() {
    ImmutableList.Builder<Object[]> args = ImmutableList.builder();

    // simple field, wildcards
    args.add(
        $(Matcher.of("a/*"), "a", Matcher.all()),
        $(Matcher.of("a/*"), "a/b", Matcher.all()),
        $(Matcher.of("a/*"), "a/b/c", Matcher.all()),
        $(Matcher.of("a/b/*"), "a", Matcher.of("b/*")),
        $(Matcher.of("a/b/*"), "a/b", Matcher.all()),
        $(Matcher.of("a/b/*"), "a/b/c", Matcher.all()),
        $(Matcher.of("*/a"), "a", Matcher.of("a,*/a")),
        $(Matcher.of("*/a"), "anything", Matcher.of("a,*/a")),
        $(Matcher.of("*/a"), "any/path", Matcher.of("a,*/a")),
        $(Matcher.of("*/a/b"), "a", Matcher.of("a/b,*/a/b")),
        $(Matcher.of("*/a/b"), "anything", Matcher.of("a/b,*/a/b")),
        $(Matcher.of("*/a/b"), "any/path", Matcher.of("a/b,*/a/b")),
        $(Matcher.of("*/a/b"), "any/a", Matcher.of("b,*/b")),
        $(Matcher.of("*/a/b"), "any/path/a", Matcher.of("b,*/b")),
        $(Matcher.of("*/a/b"), "any/path/a/b", Matcher.all()),
        $(Matcher.of("a/*/b"), "a", Matcher.of("*/b")),
        $(Matcher.of("a/*/b"), "a/any", Matcher.of("b,*/b")),
        $(Matcher.of("a/*/b"), "a/b", Matcher.of("b,*/b")),
        $(Matcher.of("a/*/b"), "a/any/path", Matcher.of("b,*/b")),
        $(Matcher.of("a/*/b"), "a/x/b", Matcher.all()),
        $(Matcher.of("a/*/b"), "a/b/b", Matcher.all())
    );

    return args.build();
  }

  @SuppressWarnings("UnusedDeclaration")
  private Iterable<Object[]> rebaseMultipleFieldsParams() {
    ImmutableList.Builder<Object[]> args = ImmutableList.builder();

    // multiple fields
    args.add(
        $(Matcher.of("a,b,c"), "a", Matcher.all()),
        $(Matcher.of("a,b,c"), "b", Matcher.all()),
        $(Matcher.of("a,b,c"), "c", Matcher.all()),
        $(Matcher.of("a,b/c"), "a", Matcher.all()),
        $(Matcher.of("a,b/c"), "b", Matcher.of("c")),
        $(Matcher.of("a,b/c"), "b/c", Matcher.all()),
        $(Matcher.of("a,b/c"), "d", Matcher.none())
    );

    return args.build();
  }

  @SuppressWarnings("UnusedDeclaration")
  private Iterable<Object[]> rebaseSubSelectionParams() {
    ImmutableList.Builder<Object[]> args = ImmutableList.builder();

    // sub-selections
    args.add(
        $(Matcher.of("a(b,c)"), "a", Matcher.of("b,c")),
        $(Matcher.of("a(b,c)"), "a/b", Matcher.all()),
        $(Matcher.of("a(b,c)"), "a/c", Matcher.all()),
        $(Matcher.of("a(b,c)"), "a/c/d", Matcher.all()),
        $(Matcher.of("a(b,c)/d"), "a", Matcher.of("b/d,c/d")),
        $(Matcher.of("a(b,c)/d"), "a/b", Matcher.of("d")),
        $(Matcher.of("a(b,c)/d"), "a/c", Matcher.of("d")),
        $(Matcher.of("a(b,c)/d"), "a/b/d", Matcher.all()),
        $(Matcher.of("a(b,c)/d"), "a/c/d", Matcher.all()),
        $(Matcher.of("a(b,c)/d"), "a/d", Matcher.none()),
        $(Matcher.of("a(b,c)/d"), "a/foo", Matcher.none()),
        $(Matcher.of("a(b,c)/d"), "a/b/foo", Matcher.none()),
        $(Matcher.of("a(b,c)/d"), "a/c/foo", Matcher.none())
    );

    return args.build();
  }

  @SuppressWarnings("UnusedDeclaration")
  private Iterable<Object[]> rebaseSubSelectionNestedParams() {
    ImmutableList.Builder<Object[]> args = ImmutableList.builder();

    // sub-selection nested
    args.add(
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a", Matcher.of("b/i,c(d,e/f)/h/i")),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/b", Matcher.of("i")),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/b/i", Matcher.all()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/c", Matcher.of("d/h/i,e/f/h/i")),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/c/d", Matcher.of("h/i")),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/c/d/h", Matcher.of("i")),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/c/d/h/i", Matcher.all()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/c/d/h/i/foo", Matcher.all()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/c/e", Matcher.of("f/h/i")),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/c/e/f", Matcher.of("h/i")),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/c/e/f/h", Matcher.of("i")),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/c/e/f/h/i", Matcher.all()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/c/e/f/h/i/any", Matcher.all()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "b", Matcher.none()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "foo", Matcher.none()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/d", Matcher.none()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/h", Matcher.none()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/i", Matcher.none()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/foo", Matcher.none()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/b/c", Matcher.none()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/b/h", Matcher.none()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/b/foo", Matcher.none()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/b/h/foo", Matcher.none()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/b/h/foo/i", Matcher.none()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/c/b", Matcher.none()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/c/h", Matcher.none()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/c/f", Matcher.none()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/c/i", Matcher.none()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/c/foo", Matcher.none()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/c/d/foo", Matcher.none()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/c/e/foo", Matcher.none()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/c/e/f/foo", Matcher.none()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/c/e/f/h/foo", Matcher.none()),
        $(Matcher.of("a(b,c(d,e/f)/h)/i"), "a/c/e/f/h/foo/i", Matcher.none())
    );

    return args.build();
  }

  @SuppressWarnings("UnusedDeclaration")
  private Iterable<Object[]> rebaseJavadocParams() {
    ImmutableList.Builder<Object[]> args = ImmutableList.builder();

    // javadoc examples
    args.add(
        $(Matcher.of("count,items(age,name/displayName),*/id"), "count", Matcher.all()),
        $(Matcher.of("count,items(age,name/displayName),*/id"), "items", Matcher.of("age,name/displayName,id,*/id")),
        $(Matcher.of("count,items(age,name/displayName),*/id"), "items/name", Matcher.of("displayName,id,*/id")),
        $(Matcher.of("count,items(age,name/displayName),*/id"), "random/path", Matcher.none())
    );

    return args.build();
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
