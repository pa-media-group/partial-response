package com.pressassociation.fire.partialresponse.fields.ast.visitor;

import com.pressassociation.fire.partialresponse.fields.ast.AstNode;

/**
 * A visitor that produces some kind of output from the visitation of the ast.
 *
 * @author Matt Nathan
 */
public abstract class TransformingVisitor<T> extends AstVisitor {
  public T applyTo(AstNode node) {
    node.apply(this);
    return getResult();
  }

  public abstract T getResult();

  @Override
  public String toString() {
    return getResult().toString();
  }
}
