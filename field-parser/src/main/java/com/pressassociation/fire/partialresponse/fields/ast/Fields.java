package com.pressassociation.fire.partialresponse.fields.ast;

import com.pressassociation.fire.partialresponse.fields.ast.visitor.AstVisitor;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Generated JavaDoc Comment.
 *
 * @author Matt Nathan
 */
public class Fields extends AstNode {
  private final Field field;
  private final AstNode next;

  public Fields(Field field, AstNode next) {
    this.field = checkNotNull(field);
    this.next = checkNotNull(next);
  }

  public Field getField() {
    return field;
  }

  public AstNode getNext() {
    return next;
  }

  @Override
  public void apply(AstVisitor visitor) {
    visitor.visitFields(this);
  }
}
