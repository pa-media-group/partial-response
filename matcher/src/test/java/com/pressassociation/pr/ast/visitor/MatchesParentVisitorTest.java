package com.pressassociation.pr.ast.visitor;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

/**
 * Tests for {@link MatchesParentVisitor} to make sure it matches where it should and doesn't match where it shouldn't.
 *
 * @author Matt Nathan
 */
@RunWith(Parameterized.class)
public class MatchesParentVisitorTest extends TransformingVisitorTestBase<MatchesParentVisitor, Boolean> {

  @Parameterized.Parameters(name = "Matcher.of(\"{1}\").matchesParent(\"{0}\") == {2}")
  public static Collection<Object[]> parameters() {
    ImmutableList.Builder<Object[]> builder = ImmutableList.builder();

    // Ensure any path matches just a wildcard
    builder.add(has("item", "*"))
           .add(has("item/any", "*"))
           .add(has("item/any/other", "*"));
    // make sure that a path that starts with the same word matches
    builder.add(has("item", "item"))
           .add(not("item/any", "item"))
           .add(not("item/any/other", "item"))
           .add(not("not", "item"))
           .add(not("not/matched", "item"))
           .add(not("not/matched/here", "item"));
    // make sure that a path that starts with the same word matches
    builder.add(has("item", "item/any"))
           .add(has("item/any", "item/any"))
           .add(not("item/any/other", "item/any"))
           .add(not("item/not", "item/any"))
           .add(not("not", "item/any"))
           .add(not("not/matched", "item/any"))
           .add(not("not/matched/here", "item/any"));
    // simple path with wildcard at the end
    builder.add(has("item", "item/*"))
           .add(has("item/any", "item/*"))
           .add(has("item/any/other", "item/*"))
           .add(not("not", "item/*"))
           .add(not("not/matched", "item/*"))
           .add(not("not/matched/here", "item/*"));
    // simple path with wildcard at the start
    builder.add(has("item/any", "*/any"))
           .add(has("item/any/other", "*/other"))
           .add(has("not", "*/not"))
           .add(has("not/matched", "*/not"))
           .add(has("not/matched", "*/item"))
           .add(has("not/matched/here", "*/item"));
    // simple path with wildcard in the middle
    builder.add(has("item/any/other", "item/*/other"))
           .add(has("item/any/middle/other", "item/*/other"))
           .add(has("start", "start/*/end"))
           .add(has("start/end", "start/*/end"))
           .add(not("not", "start/*/end"))
           .add(not("end", "start/*/end"));
    // simple fields tests
    builder.add(has("item", "item,other"))
           .add(has("other", "item,other"))
           .add(has("first", "first,item,other"))
           .add(has("item", "first,item,other"))
           .add(has("other", "first,item,other"))
           .add(has("any", "*,item"))
           .add(has("any", "item,*"))
           .add(not("missing", "item,other"))
           .add(not("one/two", "item,other"));
    // slightly more complicated fields tests containing paths
    builder.add(has("item", "item/any,other"))
           .add(has("item/any", "item/any,other"))
           .add(has("other", "item/any,other"))
           .add(has("other", "item/any,other/any"))
           .add(has("other/any", "item/any,other/any"))
           .add(not("other/not", "item/any,other"))
           .add(not("item/not", "item/any,other/not"))
           .add(not("item/other", "item/any,other/not"))
           .add(not("item/item", "item/any,other/not"));
    // sub selection tests
    builder.add(has("item", "item(any)"))
           .add(has("item/any", "item(any)"))
           .add(has("item", "item(any,other)"))
           .add(has("item/any", "item(any,other)"))
           .add(has("item/other", "item(any,other)"))
           .add(has("item", "item(two/three)"))
           .add(has("item/two", "item(two/three)"))
           .add(has("item/two/three", "item(two/three)"))
           .add(has("item", "item(two)/three"))
           .add(has("item/two", "item(two)/three"))
           .add(has("item/two/three", "item(two)/three"))
           .add(has("item", "item(*)/three"))
           .add(has("item/a", "item(*)/three"))
           .add(has("item/a/b/three", "item(*)/three"))
           .add(has("item/a/b/not", "item(*)/three"))
           .add(has("item/two/three", "item(two,a/b)/three"))
           .add(has("item/a", "item(two,a/b)/three"))
           .add(has("item/a/b", "item(two,a/b)/three"))
           .add(has("item/a/b/three", "item(two,a/b)/three"))
           .add(has("item/a/b/three", "*(b)/three"))
           .add(has("random/path", "*(b)/three"))
           .add(not("item/not", "item(any)"))
           .add(not("item/any/not", "item(any)/other"))
           .add(not("item/not/other", "item(any)/other"));
    // About as complicated as I can make it
    builder.add(has("a", "a/b(*/d,c)/not,a(1,2(f,g))"))
           .add(has("a/b", "a/b(*/d,c)/not,a(1,2(f,g))"))
           .add(has("a/b/any", "a/b(*/d,c)/not,a(1,2(f,g))"))
           .add(has("a/1", "a/b(*/d,c)/not,a(1,2(f,g))"))
           .add(has("a/2", "a/b(*/d,c)/not,a(1,2(f,g))"))
           .add(has("a/2/f", "a/b(*/d,c)/not,a(1,2(f,g))"))
           .add(has("a/2/g", "a/b(*/d,c)/not,a(1,2(f,g))"))
           .add(not("not", "a/b(*/d,c)/not,a(1,2(f,g))"))
           .add(not("a/not", "a/b(*/d,c)/not,a(1,2(f,g))"))
           .add(not("a/1/f", "a/b(*/d,c)/not,a(1,2(f,g))"))
           .add(not("a/2/not", "a/b(*/d,c)/not,a(1,2(f,g))"))
           .add(not("a/2/f/not", "a/b(*/d,c)/not,a(1,2(f,g))"));
    return builder.build();
  }

  private static Object[] has(CharSequence path, String source) {
    return args(path, source, true);
  }

  private static Object[] not(CharSequence path, String source) {
    return args(path, source, false);
  }

  private static Object[] args(CharSequence path, String source, boolean expected) {
    return genArgs(path, source, String.valueOf(expected));
  }

  private final Iterable<String> pathParts;

  public MatchesParentVisitorTest(CharSequence pathParts, String source, String expected) {
    super(source, expected);
    this.pathParts = Splitter.on('/').split(pathParts);
  }

  @Override
  protected MatchesParentVisitor createVisitor() {
    return new MatchesParentVisitor(pathParts);
  }
}
