package com.pressassociation.fire.partialresponse.fields.ast;

import com.pressassociation.fire.partialresponse.fields.ast.visitor.AstVisitor;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Generated JavaDoc Comment.
 *
 * @author Matt Nathan
 */
public class Path extends Field {
  private final Node node;
  private final Field field;

  public Path(Node node, Field field) {
    this.node = checkNotNull(node);
    this.field = checkNotNull(field);
  }

  public Node getNode() {
    return node;
  }

  public Field getField() {
    return field;
  }

  @Override
  public void apply(AstVisitor visitor) {
    visitor.visitPath(this);
  }
}
