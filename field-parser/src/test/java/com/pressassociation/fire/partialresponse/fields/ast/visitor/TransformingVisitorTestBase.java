package com.pressassociation.fire.partialresponse.fields.ast.visitor;

import com.pressassociation.fire.partialresponse.fields.ast.AstNode;
import com.pressassociation.fire.partialresponse.fields.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * base class to make testing transforming visitors easier
 *
 * @author Matt Nathan
 */
public abstract class TransformingVisitorTestBase<V extends TransformingVisitor<T>, T> {

  protected static Object[] args(String source, String expected) {
    return genArgs(source, expected);
  }

  protected static Object[] genArgs(Object... args) {
    return args;
  }

  private final String source;
  private final String expected;
  private Parser parser;

  protected TransformingVisitorTestBase(String source, String expected) {
    this.source = source;
    this.expected = expected;
  }

  @Before
  public void setUp() {
    parser = new Parser();
  }

  @Test
  public void testTransformation() {
    AstNode sourceNode = parser.parse(source);
    T resultNode = createVisitor().applyTo(sourceNode);
    assertEquals("For source " + source, expected, toString(resultNode));
  }

  protected String toString(T resultNode) {
    return resultNode.toString();
  }

  protected abstract V createVisitor();
}
