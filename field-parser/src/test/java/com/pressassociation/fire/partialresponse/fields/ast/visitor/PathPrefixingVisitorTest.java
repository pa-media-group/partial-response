package com.pressassociation.fire.partialresponse.fields.ast.visitor;

import com.google.common.collect.ImmutableList;

import com.pressassociation.fire.partialresponse.fields.ast.AstNode;
import com.pressassociation.fire.partialresponse.fields.ast.Field;
import com.pressassociation.fire.partialresponse.fields.ast.Node;
import com.pressassociation.fire.partialresponse.fields.ast.Word;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

/**
 * Tests for the PathPrefixingVisitor
 *
 * @author Matt Nathan
 */
@RunWith(Parameterized.class)
public class PathPrefixingVisitorTest extends TransformingVisitorTestBase<PathPrefixingVisitor, AstNode> {

  @Parameterized.Parameters
  public static Collection<Object[]> parameters() {
    return ImmutableList.<Object[]>builder()
                        .add(args(new Word("prefix"), "items", "prefix/items"))
                        .add(args(new Word("prefix"), "items,type", "prefix/items,prefix/type"))
                        .add(args(new Word("prefix"), "items,type,href", "prefix/items,prefix/type,prefix/href"))
                        .add(args(new Word("prefix"), "items(id)", "prefix/items(id)"))
                        .add(args(new Word("prefix"), "items/id", "prefix/items/id"))
                        .build();
  }

  protected static Object[] args(Field prefix, String source, String expected) {
    return genArgs(prefix, source, expected);
  }

  private final Node prefix;

  public PathPrefixingVisitorTest(Node prefix, String source, String expected) {
    super(source, expected);
    this.prefix = prefix;
  }

  @Override
  protected PathPrefixingVisitor createVisitor() {
    return new PathPrefixingVisitor(prefix);
  }
}
