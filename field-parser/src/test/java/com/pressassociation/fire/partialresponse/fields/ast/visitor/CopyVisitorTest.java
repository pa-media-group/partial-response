package com.pressassociation.fire.partialresponse.fields.ast.visitor;

import com.google.common.collect.ImmutableList;
import com.pressassociation.fire.partialresponse.fields.ast.AstNode;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.assertNotSame;

/**
 * Tests for the CopyVisitor
 *
 * @author Matt Nathan
 */
@RunWith(Parameterized.class)
public class CopyVisitorTest extends TransformingVisitorTestBase<CopyVisitor, AstNode> {
  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    // can't put * on it's own here because Wildcard is a singleton
    return ImmutableList.of(
            args("item", "item"),
            args("item,items/id,other(id,value),*/title", "item,items/id,other(id,value),*/title"));
  }

  public CopyVisitorTest(String source, String expected) {
    super(source, expected);
  }

  @Override
  protected void checkResult(AstNode sourceNode, AstNode resultNode) {
    super.checkResult(sourceNode, resultNode);
    assertNotSame(sourceNode, resultNode);
  }

  @Override
  protected CopyVisitor createVisitor() {
    return new CopyVisitor();
  }
}
