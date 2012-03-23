package com.pressassociation.fire.partialresponse.fields.ast.visitor;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Generated JavaDoc Comment.
 *
 * @author Matt Nathan
 */
public class TransformingVisitorTest {
  @Test
  public void testToString() {
    TransformingVisitor<String> subject = new TransformingVisitor<String>() {
      @Override
      public String getResult() {
        return "expected";
      }
    };
    assertEquals("expected", subject.toString());
  }
}