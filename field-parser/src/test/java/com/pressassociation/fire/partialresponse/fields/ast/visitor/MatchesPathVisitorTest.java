package com.pressassociation.fire.partialresponse.fields.ast.visitor;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

/**
 * Tests for {@link MatchesPathVisitor} to make sure it matches where it should and doesn't match where it shouldn't.
 *
 * @author Matt Nathan
 */
@RunWith(Parameterized.class)
public class MatchesPathVisitorTest extends TransformingVisitorTestBase<MatchesPathVisitor, Boolean> {

  @Parameterized.Parameters
  public static Collection<Object[]> parameters() {
    ImmutableList.Builder<Object[]> builder = ImmutableList.builder();

    // [0] Ensure any path matches just a wildcard
    builder.add(has("item", "*"))
           .add(has("item/any", "*"))
           .add(has("item/any/other", "*"));
    // [3] make sure that a path that starts with the same word matches
    builder.add(has("item", "item"))
           .add(has("item/any", "item"))
           .add(has("item/any/other", "item"))
           .add(not("not", "item"))
           .add(not("not/matched", "item"))
           .add(not("not/matched/here", "item"));
    // [9] make sure that a path that starts with the same word matches
    builder.add(has("item/any", "item/any"))
           .add(has("item/any/other", "item/any"))
           .add(not("item", "item/any"))
           .add(not("item/not", "item/any"))
           .add(not("not", "item/any"))
           .add(not("not/matched", "item/any"))
           .add(not("not/matched/here", "item/any"));
    // [16] simple path with wildcard at the end
    builder.add(has("item", "item/*"))
           .add(has("item/any", "item/*"))
           .add(has("item/any/other", "item/*"))
           .add(not("not", "item/*"))
           .add(not("not/matched", "item/*"))
           .add(not("not/matched/here", "item/*"));
    // [22] simple path with wildcard at the start
    builder.add(has("item/any", "*/any"))
           .add(has("item/any/other", "*/other"))
           .add(not("not", "*/not"))
           .add(not("not/matched", "*/not"))
           .add(not("not/matched", "*/item"))
           .add(not("not/matched/here", "*/item"));
    // [28] simple path with wildcard in the middle
    builder.add(has("item/any/other", "item/*/other"))
           .add(has("item/any/middle/other", "item/*/other"))
           .add(not("not", "start/*/end"))
           .add(not("start", "start/*/end"))
           .add(not("start/end", "start/*/end"))
           .add(not("end", "start/*/end"));
    // [34] simple fields tests
    builder.add(has("item", "item,other"))
           .add(has("other", "item,other"))
           .add(has("first", "first,item,other"))
           .add(has("item", "first,item,other"))
           .add(has("item", "first,item,other"))
           .add(has("any", "*,item"))
           .add(has("any", "item,*"))
           .add(has("other", "first,item,other"))
           .add(not("missing", "item,other"))
           .add(not("one/two", "item,other"));
    // [44] slightly more complicated fields tests containing paths
    builder.add(has("item/any", "item/any,other"))
           .add(has("other", "item/any,other"))
           .add(has("other/any", "item/any,other/any"))
           .add(not("item/not", "item/any,other/not"))
           .add(not("item/other", "item/any,other/not"));
    // [49] sub selection tests
    builder.add(has("item/any", "item(any)"))
           .add(has("item/any", "item(any,other)"))
           .add(has("item/other", "item(any,other)"))
           .add(has("item/two/three", "item(two/three)"))
           .add(has("item/two/three", "item(two)/three"))
           .add(has("item/a/b/three", "item(*)/three"))
           .add(has("item/two/three", "item(two,a/b)/three"))
           .add(has("item/a/b/three", "item(two,a/b)/three"))
           .add(has("item/a/b/three", "*(b)/three"))
           .add(not("item/not", "item(any)"))
           .add(not("item/any/not", "item(any)/other"))
           .add(not("item/not/other", "item(any)/other"));
    // [61] About as complicated as I can make it
    builder.add(has("a/b/c/d/e/f", "a/b(*/d,c)/not,a(1,2,*/d)/*(f,g)"))
           .add(not("a/b/c/d/e/other", "a/b(*/d,c)/not,a(1,2,b(c/not),*/d)/*(f,g)"));
    // [63] Bugs that have been found through use
    builder.add(has("li/notablyAssociatedWith/label", "li(label,notablyAssociatedWith(label))"));
    return builder.build();
  }

  @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
  private static Object[] has(CharSequence path, String source) {
    return args(path, source, true);
  }

  private static Object[] not(CharSequence path, String source) {
    return args(path, source, false);
  }

  private static Object[] args(CharSequence path, String source, boolean expected) {
    return genArgs(Splitter.on('/').split(path), source, String.valueOf(expected));
  }

  private final Iterable<String> pathParts;

  public MatchesPathVisitorTest(Iterable<String> pathParts, String source, String expected) {
    super(source, expected);
    this.pathParts = pathParts;
  }

  @Override
  protected MatchesPathVisitor createVisitor() {
    return new MatchesPathVisitor(pathParts);
  }
}
