package com.pressassociation.pr.ast.visitor;

import com.pressassociation.pr.ast.AstNode;

/**
 * A visitor that produces some kind of output from the visitation of the ast.
 *
 * @param <T> The result type
 * @author Matt Nathan
 */
public abstract class TransformingVisitor<T> extends AstVisitor {
  /**
   * Apply this visitor to the given AST returning the result.
   */
  public T applyTo(AstNode node) {
    node.apply(this);
    return getResult();
  }

  /**
   * Return the result of visiting the AST.
   */
  public abstract T getResult();

  @Override
  public String toString() {
    return getResult().toString();
  }
}
